import java.io.*;
import java.net.*;
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

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

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
                    new Thread(new HandleAClient(socket)).start();
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
        System.exit(1);
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

                // Continuously serve the client
                while (true) {
                    //Output attacker info
                    String attacker = inputFromClient.readUTF();
                    int attackX = inputFromClient.readInt();
                    int attackY = inputFromClient.readInt();

                    //Attack a random square
                    int targetX = (int)(Math.random()*9);
                    int targetY = (int)(Math.random()*9);
                    outputToClient.writeInt(targetX);
                    outputToClient.writeInt(targetY);

                    Platform.runLater(() -> {
                        ta.appendText(attacker + " attacked (" + attackX + "," + attackY + ")\n");
                        ta.appendText("Attacking square (" + targetX + "," + targetY + ")\n");
                    });
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}