//import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.io.FileInputStream;


public class BattleshipGameDriver extends Application {
    //Panes to be used
    BorderPane mainPane = new BorderPane();     //Parent/root pane
    BorderPane leftPane = new BorderPane();     //Timer, Announcement, Move History
    BorderPane midPane = new BorderPane();      //Display of both Boards
    BorderPane rightPane = new BorderPane();    //Display ships' status for both players
    VBox textAnnouncementPane = new VBox();     //Display in game announcements(ie. whose turn, attack hit/miss)
    TextArea ta = new TextArea();
    //Players
    Player[] players = new Player[2];
    ImageView[] myShips;
    ImageView[] enemyShips;
    //Timer
    GameTimer gtimer = new GameTimer();
    //IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    Server server = null;                  //server for the game

    @Override
    public void start(Stage stage) throws Exception {
        mainMenu(stage);
    }

    //The main menu
    public void mainMenu(Stage stage) throws Exception {
        BorderPane menuPane = new BorderPane();
        menuPane.setMinSize(600,375);
        VBox buttons = new VBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        //buttons.setAlignment(Pos.BAS);
        Button play = new Button("Play Game");
        play.setStyle("-fx-background-color: lightBlue");
        play.setMaxWidth(100);
        play.setMaxHeight(50);

        //Background image
        Image image = new Image(new FileInputStream("Boat Pictures/WorldOfWarships2.jpg"));
        BackgroundImage backgroundI = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundI);
        menuPane.setBackground(background);

        //Button functionality
        buttons.getChildren().addAll(play);
        menuPane.setCenter(buttons);
        play.setOnAction(e -> {
            //Start server instance
            server = new Server();
            server.start(new Stage());

            //Connect to server
            connect();

            System.out.println("playing game");
            ((Node)(e.getSource())).getScene().getWindow().hide();
            makeGameScreen();
        });

        Scene scene = new Scene(menuPane);
        stage.setScene(scene);
        stage.show();
    }

    //Connect to the game server
    public void connect() {
        try {
            //Create a socket to connect to the server
            //Change play to be address
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
        //initialize players
        players[0] = new Player();
        players[1] = new Player("player 2");
        mainPane.setMinSize(660,700);
        mainPane.setStyle("-fx-background-color: lightBlue");
        leftPane.setStyle("-fx-background-color: lightGreen");
        rightPane.setStyle("-fx-background-color: lightBlue");

        //midPane
        TextField nameField = new TextField();
        nameField.setAlignment(Pos.CENTER);
        VBox text = new VBox(new Label("Enter your name"), nameField);
        text.setAlignment(Pos.TOP_CENTER);
        Button enterName = new Button("Enter");
        enterName.setAlignment(Pos.TOP_CENTER);
        text.getChildren().add(enterName);
        StackPane inputName = new StackPane(text);
        inputName.setAlignment(Pos.CENTER);
        midPane.setCenter(inputName);
        mainPane.setCenter(midPane);

        enterName.setOnMouseClicked(e -> {
            players[0].setName(nameField.getText());
            displayBoard();
        });
        nameField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                players[0].setName(nameField.getText());
                displayBoard();
            }
        });

        //leftPane
        leftPane.setMaxWidth(170);
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
        ta.setMinHeight(700); // set max height for announcements
        ta.setMaxWidth(leftPane.getMaxWidth());
        ScrollPane historyPane = new ScrollPane(ta);
        historyPane.setMaxWidth(leftPane.getMaxWidth());
        historyPane.setMinHeight(700);
        historyPane.setStyle("-fx-border-color: black");
        leftPane.setBottom(historyPane);
        mainPane.setLeft(leftPane);

        //rightPane
        VBox enemyVbox = new VBox();            //to hold the images for enemy ships
        VBox myVbox = new VBox();               //to hold the images for my ships
        mainPane.setRight(rightPane);
        rightPane.setTop(enemyVbox);
        rightPane.setBottom(myVbox);
        enemyVbox.setStyle("-fx-border-color: black");
        enemyVbox.setAlignment(Pos.TOP_LEFT);
        enemyVbox.setSpacing(20);
        enemyVbox.setPadding(new Insets(5,5,5,5));
        enemyVbox.setPrefHeight(304);
        myShips = new ImageView[5];
        enemyShips = new ImageView[5];
        int count = 0;
        for(Ship s: players[1].fleet){
            //load image
            ImageView imgs = new ImageView(s.shipPicture);
            imgs.setStyle("-fx-border-color: lightBlue");
            enemyShips[count] = imgs;
            enemyVbox.getChildren().add(imgs);
            count++;
        }
        count = 0;
        myVbox.setStyle("-fx-border-color: black");
        myVbox.setAlignment(Pos.TOP_LEFT);
        myVbox.setSpacing(20);
        myVbox.setPadding(new Insets(5,5,5,5));
        myVbox.setPrefHeight(304);
        for(Ship s: players[0].fleet){
            //Load image
            ImageView imgs = new ImageView(s.shipPicture);
            imgs.setStyle("-fx-border-color: black");
            myShips[count] = imgs;
            myVbox.getChildren().add(imgs);
            count++;
        }
        HBox border = new HBox(new Label(""));
        border.setStyle("-fx-background-color: lightBlue");
        rightPane.setCenter(border);

        //Create new window
        Scene scene = new Scene(mainPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Battleship");
        stage.show();
    }

    //show your board and enemy's board
    private void displayBoard() {
        //clear the mid pane
        midPane.getChildren().removeAll();
        //create my board and enemy board
        GridPane enemyGridPane = new GridPane();
        GridPane myGridPane = new GridPane();

        enemyGridPane.setGridLinesVisible(true);
        enemyGridPane.setAlignment(Pos.CENTER);
        myGridPane.setGridLinesVisible(true);
        myGridPane.setAlignment(Pos.CENTER);


        textAnnouncementPane.setAlignment(Pos.CENTER);
        Label lbl = new Label("Set your ships by choosing one,");
        lbl.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Label lbl2 = new Label("then choose a spot to place your");
        lbl2.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Label lbl3 = new Label("ship. When your done ready up!");
        lbl3.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Label lbl4 = new Label("Press R to rotate.");
        lbl4.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Button bt = new Button("READY");
        textAnnouncementPane.getChildren().addAll(lbl,lbl2,lbl3,lbl4,bt);
        midPane.setCenter(textAnnouncementPane);



//        HBox border = new HBox();
//        border.setStyle("-fx-background-color: lightBlue");
//        midPane.setCenter(border);
        midPane.setTop(enemyGridPane);
        midPane.setBottom(myGridPane);

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                enemyGridPane.add(players[1].board.board[i][k], i, k);
                myGridPane.add(players[0].board.board[i][k], i, k);
            }
        }

        players[0].setShips(mainPane.getScene(), myShips, ta);

        //ai sets their own board
        players[1].setAIShips();

        playGame();
    }
    Boolean gameOver = false;
    public void playGame(){
        textAnnouncementPane.getChildren().get(4).setOnMouseClicked(e -> {
            //check if all ships were placed on the board
            if(players[0].count >= 5 && players[1].count >= 5 && !gameOver) {
                gtimer.startTime(00);           //start timer after name has been entered
                textAnnouncementPane.getChildren().get(4).setVisible(false);
                textAnnouncementPane.getChildren().get(4).setStyle("-fx-background-color: red");
                textAnnouncementPane.getChildren().remove(0,5);
                //Game starts
                players[0].setTurn(true);
                players[0].attack(players[1],toServer,fromServer, ta, mainPane);
            }
        });
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
