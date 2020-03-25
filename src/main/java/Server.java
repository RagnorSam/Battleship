import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {

    private TextArea ta = new TextArea();
    private int clientNo = 0;
    Socket socket;
    Thread handleClient = null;
    Stage window = null;

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
        window = primaryStage; //testing

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                ta.appendText("Server started at "
                        + new Date() + '\n');

                while (true) {
                    // Listen for a new connection request
                    socket = serverSocket.accept();

                    // Increment clientNo
                    clientNo++;

                    Platform.runLater( () -> {
                        // Display the client number
                        ta.appendText("Starting thread for client " + clientNo +
                                " at " + new Date() + '\n');

                        // Find the client's host name, and IP address
                        InetAddress inetAddress = socket.getInetAddress();
                        ta.appendText("Client " + clientNo + "'s host name is "
                                + inetAddress.getHostName() + "\n");
                        ta.appendText("Client " + clientNo + "'s IP Address is "
                                + inetAddress.getHostAddress() + "\n");
                    });

                    //Create and start a new thread for the connection
                    handleClient = new Thread(new HandleAClient(socket));
                    handleClient.start();
                }
            }
            catch (SocketException e) {
                if(socket.isClosed()) {
                    System.out.println("Connection Closed.");
                }
            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }

    public void stop() throws Exception{
        handleClient.stop();
        socket.close();
        window.close();
        System.exit(1);
    }

    // Define the thread class for handling new connection
    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket
        int gameState = 0; //keeps track of games stages (e.g. placing ships, attacking each other)

        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                //Create data input/output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                ObjectOutputStream osOut = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream osIn = new ObjectInputStream(socket.getInputStream());

                ArrayList<Coordinate> alreadyPicked = new ArrayList<>();

                //Continuously serve the client
                while (true) {
                    if(gameState == 0) { //Placing ships
                        try {
                            //Read in board
                            Board board;
                            board = (Board) osIn.readObject();

                            //System.out.println("read board"); //delete
                            //Read in ships
                        /*
                        try {
                            Ship[] ships = new Ship[5];
                            for (int i = 0; i < ships.length; i++) {
                                ships[i] = (Ship) osIn.readObject();
                            }
                        } catch(ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                         */
                            System.out.println("Read data"); //delete

                            //Create ships
                            Ship[] ships = new Ship[5];
                            ships[0] = new Ship(2, "Boat2");
                            ships[1] = new Ship(3, "Boat3a");
                            ships[2] = new Ship(3, "Boat3b");
                            ships[3] = new Ship(4, "Boat4");
                            ships[4] = new Ship(5, "Boat5");

                            //Place ships on the board
                            for (Ship i : ships) {
                                int orientation = (int) Math.random() * 2; //select random orientation
                                int xOffset = 0, yOffset = 0; //stops ship from being placed where it extends off the board
                                Square[] boardSection;
                                if (orientation == 0) { //horizontal
                                    xOffset = i.shipSize - 1;
                                } else { //vertical
                                    yOffset = i.shipSize - 1;
                                }
                                boolean valid = false;
                                do {
                                    int sourceX = (int) Math.random() * (10 - xOffset);
                                    int sourceY = (int) Math.random() * (10 - yOffset);
                                    int startCoord;
                                    if (orientation == 0) {
                                        boardSection = board.getRow(sourceX);
                                        startCoord = sourceX;
                                    }
                                    else {
                                        boardSection = board.getCol(sourceY);
                                        startCoord = sourceY;
                                    }

                                    valid = isValidMove(boardSection,i.shipSize,startCoord);

                                } while (valid == false);
                            }
                            osOut.writeObject(board);
                            gameState = 1;
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(gameState == 1) { //Ships placed, exchanging turns
                        //Output attacker info
                        String attacker = inputFromClient.readUTF();
                        int attackX = inputFromClient.readInt();
                        int attackY = inputFromClient.readInt();

                        int targetX, targetY;
                        //Handle if invalid move has been made
                        boolean valid;
                        do {
                            valid = true;
                            //Pick random square
                            targetX = (int) (Math.random() * 10);
                            targetY = (int) (Math.random() * 10);

                            //Check if valid
                            if (alreadyPicked.size() == 0) {
                                valid = true;
                            } else {
                                for (int i = 0; i < alreadyPicked.size(); i++) {
                                    if (targetX == alreadyPicked.get(i).getX() && targetY == alreadyPicked.get(i).getY()) {
                                        //System.out.println("already hit"); //delete
                                        valid = false;
                                        break;
                                    }
                                }
                            }
                        } while (!valid);

                        outputToClient.writeInt(targetX);
                        outputToClient.writeInt(targetY);

                        alreadyPicked.add(new Coordinate(targetX, targetY));

                        Platform.runLater(() -> {
                            ta.appendText(attacker + " attacked (" + attackX + "," + attackY + ")\n");
                            //ta.appendText("Attacking square (" + targetX + "," + targetY + ")\n"); //delete
                        });
                    }
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }

        private boolean isValidMove(Square[] boardSection, int shipSize, int startCoord) {
            int increment = startCoord;
            for(int i = 0; i < shipSize; i++) {
                if(boardSection[increment].hasShip()) {
                    return false; //exit if collision with other ship occurs when attempting to place ship
                }
                increment ++;
            }
            //Place the ship if no potential collisions detected
            increment = startCoord;
            for(int i = 0; i < shipSize; i++) {
                boardSection[increment].setShip(true);
                increment ++;
            }
            return true;
        }
    }

    class Coordinate {
        private int x;
        private int y;
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX() { return this.x; }
        public int getY() { return this.y; }
    }
}