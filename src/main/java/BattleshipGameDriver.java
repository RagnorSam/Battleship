//import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.control.*;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileInputStream;


public class BattleshipGameDriver extends Application {
    BorderPane mainPane = new BorderPane();    //Main game pane
    BorderPane leftPane = new BorderPane();    //Timer, Announcement, Move History
    BorderPane midPane = new BorderPane();     //Display of both Boards
    VBox rightPane = new VBox();         //Display ships' status for both players
    GridPane enemyGridPane = new GridPane();
    GridPane myGridPane = new GridPane();
    Player[] players = new Player[2];
    GameTimer gtimer = new GameTimer();
    VBox textAnnouncementPane;
    Label textAnnouncement;
    ImageView[] myShips;
    ImageView[] enemyShips;
    VBox enemyVbox = new VBox();
    VBox myVbox = new VBox();
    Scene scene;

    //IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    Server server = null; //server for the game

    @Override
    public void start(Stage stage) throws Exception {
        mainMenu(stage);
    }

    //The main menu
    public void mainMenu(Stage stage) throws Exception {
        BorderPane menuPane = new BorderPane();
        menuPane.setMinSize(400,400);
        VBox buttons = new VBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        Button host = new Button("Host Game");

        //Background image
        Image image = new Image(new FileInputStream("src/images/menu_image.jpg"));
        BackgroundImage backgroundI = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundI);
        menuPane.setBackground(background);

        //Button functionality
        buttons.getChildren().add(host);
        menuPane.setCenter(buttons);
        host.setOnAction(e -> {
            //Start server instance
            server = new Server();
            server.start(new Stage());

            //Connect to server
            connect();

            System.out.println("Hosting game");
            ((Node)(e.getSource())).getScene().getWindow().hide();
            makeGameScreen();
        });

        scene = new Scene(menuPane);
        stage.setScene(scene);
        stage.show();
    }

    //Connect to the game server
    public void connect() {
        try {
            //Create a socket to connect to the server
            //Change host to be address
            Socket socket = new Socket("localhost", 8000);

            //Create an input stream to receive data to the server
            fromServer = new DataInputStream(socket.getInputStream());

            //Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Failed to connect to server");
        }
    }

    //create the "starting screen"
    private void makeGameScreen() {
        players[0] = new Player();
        players[1] = new Player("player 2");
        mainPane.setMinSize(700, 750);
        mainPane.setStyle("-fx-background-color: lightblue");
        leftPane.setStyle("-fx-background-color: Green");
        rightPane.setStyle("-fx-background-color: Green");

        //midPane
        TextField nameField = new TextField();
        VBox text = new VBox(new Label("Enter your name"), nameField);
        Button enterName = new Button("Enter");
        text.getChildren().add(enterName);
        StackPane inputName = new StackPane(text);
        midPane.setCenter(inputName);

        enterName.setOnMouseClicked(e -> {
            players[0].setName(nameField.getText());
            gtimer.startTime(00);//start timer after name has been entered
            displayBoard();
        });
        nameField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                players[0].setName(nameField.getText());
                gtimer.startTime(00);//start timer after name has been entered
                displayBoard();
            }
        });

        mainPane.setCenter(midPane);

        //leftPane
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black");
        Label timer = new Label(gtimer.getTotalTime().get());
        gtimer.getTotalTime().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(() -> timer.setText(gtimer.getTotalTime().get()));
            }
        });
        timerPane.getChildren().add(timer);
        leftPane.setTop(timerPane);

        textAnnouncementPane = new VBox();
        textAnnouncementPane.setStyle("-fx-border-color: black");
        textAnnouncementPane.setAlignment(Pos.CENTER);
        textAnnouncement = new Label("Text Announcement here");
        textAnnouncementPane.getChildren().add(textAnnouncement);
        leftPane.setCenter(textAnnouncementPane);

        ScrollPane historyPane = new ScrollPane();
        historyPane.setStyle("-fx-border-color: black");
        leftPane.setBottom(historyPane);
        mainPane.setLeft(leftPane);

        //rightPane
        enemyVbox.setStyle("-fx-border-color: black");
        enemyVbox.setAlignment(Pos.TOP_LEFT);
        enemyVbox.setSpacing(35);
        enemyVbox.setPadding(new Insets(5,5,5,5));
        myShips = new ImageView[5];
        enemyShips = new ImageView[5];
        int count = 0;
        for(Ship s: players[1].fleet){
            //load image
            ImageView imgs = new ImageView(s.shipPicture);
            imgs.setStyle("-fx-border-color: black");
            enemyShips[count] = imgs;
            enemyVbox.getChildren().add(imgs);
            count++;
        }
        count = 0;
        rightPane.getChildren().add(enemyVbox);
        HBox border = new HBox(new Label(""));
        border.setStyle("-fx-background-color: black");
        rightPane.getChildren().add(border);
        rightPane.getChildren().add(myVbox);
        myVbox.setAlignment(Pos.TOP_LEFT);
        myVbox.setSpacing(35);
        myVbox.setPadding(new Insets(5,5,5,5));
        for(Ship s: players[0].fleet){
            //Load image
            ImageView imgs = new ImageView(s.shipPicture);
            imgs.setStyle("-fx-border-color: black");
            myShips[count] = imgs;
            myVbox.getChildren().add(imgs);
            count++;
        }

        mainPane.setRight(rightPane);

        //Create new window
        scene = new Scene(mainPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Battleship");
        stage.show();
    }

    //show your board and enemy's board
    private void displayBoard() {
        enemyGridPane.setGridLinesVisible(true);
        enemyGridPane.setAlignment(Pos.CENTER);
        myGridPane.setGridLinesVisible(true);
        myGridPane.setAlignment(Pos.CENTER);
        midPane.getChildren().removeAll();
        HBox border = new HBox(new Label(""));
        border.setStyle("-fx-background-color: black");
        midPane.setCenter(border);

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                enemyGridPane.add(players[1].board.board[i][k], i, k);
                myGridPane.add(players[0].board.board[i][k], i, k);
            }
        }
        midPane.setTop(enemyGridPane);
        midPane.setBottom(myGridPane);

        setShips();
    }

    public void setShips(){
        textAnnouncementPane.getChildren().removeAll();
        players[0].setShips(scene, myShips, myGridPane);
        players[1].setShips(scene, enemyShips, enemyGridPane);
        Button bt = new Button("READY");
        textAnnouncementPane.getChildren().add(bt);
        bt.setOnMouseClicked(e -> {
            if(players[0].count >= 5 && players[1].count >= 5) {
                bt.setVisible(false);
                bt.setStyle("-fx-background-color: red");
                players[0].setTurn(true);
                textAnnouncementPane.getChildren().removeAll();
                Boolean gameOver = false;
                for(int i =0; i < 200; i++){
                    players[0+(i%2)].attack(players[1-(i%2)],toServer,fromServer);
                    gameOver = checkWin(i);
                    if(gameOver){
                        //gameOver Screen
                        showGameOver();
                    }
                }
            }
        });
    }

    public Boolean checkWin(int i){
        Boolean check = true;
        //Player 1 attacked last so check for win
        if(i%2 == 0){
            for(Ship s:players[1].fleet){
                if(s.shipLives != 0){
                    check = false;
                    break;
                }
            }
        }
        //else check Player 2 for win
        else{
            for(Ship s:players[0].fleet){
                if(s.shipLives != 0){
                    check = false;
                    break;
                }
            }
        }
        return check;
    }

    public void showGameOver(){
        textAnnouncementPane.getChildren().removeAll();
        Label lbl = new Label("Game \n Over");
        lbl.setFont(Font.font(30));
        textAnnouncementPane.getChildren().add(lbl);
    }

    @Override
    public void stop(){
        gtimer.stopTime(0);
        if (server != null) {
            server.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
