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

import static java.lang.Thread.sleep;

public class Server extends Application {

    private TextArea ta = new TextArea();
    private int clientNo = 0;
    Socket socket;
    ArrayList<Thread> threads = new ArrayList<Thread>();

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        //primaryStage.show();

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

                    // Create and start a new thread for the connection
                    Thread temp = new Thread(new HandleAClient(socket));
                    threads.add(temp);
                    temp.start();
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

    public void stop() {
        for(Thread i: threads) {
            i.stop();
        }
        System.exit(0);
    }

    // Define the thread class for handling new connection
    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket

        /** Construct a thread */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /** Run a thread */
        public void run() {
            try {
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());
                ArrayList<Coordinate> alreadyPicked = new ArrayList<>();

                // Continuously serve the client
                while (true) {
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
                        targetX = (int)(Math.random()*10);
                        targetY = (int)(Math.random()*10);

                        //Check if valid
                        if(alreadyPicked.size() == 0) {
                            valid = true;
                        }
                        else {
                            for(int i = 0; i < alreadyPicked.size(); i++) {
                                if(targetX == alreadyPicked.get(i).getX() && targetY == alreadyPicked.get(i).getY()) {
                                    //System.out.println("already hit"); //delete
                                    valid = false;
                                    break;
                                }
                            }
                        }
                    } while(!valid);

                    outputToClient.writeInt(targetX);
                    outputToClient.writeInt(targetY);

                    alreadyPicked.add(new Coordinate(targetX,targetY));

                    Platform.runLater(() -> {
                        ta.appendText(attacker + " attacked (" + attackX + "," + attackY + ")\n");
                        //ta.appendText("Attacking square (" + targetX + "," + targetY + ")\n"); //delete
                    });
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
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