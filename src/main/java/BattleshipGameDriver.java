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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.io.FileInputStream;
import java.util.Map;


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
        menuPane.setMinSize(400,400);
        VBox buttons = new VBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        Button host = new Button("Host Game");
        Button join = new Button("Join Game");

        //Background image
        Image image = new Image(new FileInputStream("src/images/menu_image.jpg"));
        BackgroundImage backgroundI = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundI);
        menuPane.setBackground(background);

        //Button functionality
        buttons.getChildren().addAll(host,join);
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
        join.setOnAction(e -> {
            //Connect to server only
            System.out.println("Join game");
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
        //initialize players
        players[0] = new Player();
        players[1] = new Player("player 2");
        mainPane.setMinSize(660,700);
        mainPane.setStyle("-fx-background-color: lightblue");
        leftPane.setStyle("-fx-background-color: Green");
        rightPane.setStyle("-fx-background-color: Green");

        //midPane
        TextField nameField = new TextField();
        VBox text = new VBox(new Label("Enter your name"), nameField);
        Button enterName = new Button("Enter");
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

        ta.setMaxWidth(leftPane.getMaxWidth());
        ScrollPane historyPane = new ScrollPane(ta);
        historyPane.setMaxWidth(leftPane.getMaxWidth());
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
        enemyVbox.setSpacing(10);
        enemyVbox.setPadding(new Insets(5,5,5,5));
        enemyVbox.setPrefHeight(304);
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
        border.setStyle("-fx-background-color: black");
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
        Label lbl = new Label("Set your ships by choosing");
        lbl.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Label lbl2 = new Label("one, then choose a spot to");
        lbl2.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Label lbl3 = new Label("place your ship. When your");
        lbl3.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Label lbl4 = new Label("done ready up!");
        lbl4.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Label lbl5 = new Label("Press R to rotate.");
        lbl5.setStyle("-fx-stroke-color: white; -fx-font-weight: bold");
        Button bt = new Button("READY");
        textAnnouncementPane.getChildren().addAll(lbl,lbl2,lbl3,lbl4,lbl5,bt);
        leftPane.setCenter(textAnnouncementPane);
        HBox border = new HBox();
        border.setStyle("-fx-background-color: black");
        midPane.setCenter(border);
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
    public void playGame(){
        //Gameplay
        textAnnouncementPane.getChildren().get(5).setOnMouseClicked(e -> {
            //check if all ships were placed on the board
            if(players[0].count >= 5 && players[1].count >= 5) {
                gtimer.startTime(00);           //start timer after name has been entered
                //remove the button
                textAnnouncementPane.getChildren().get(5).setVisible(false);
                textAnnouncementPane.getChildren().get(5).setStyle("-fx-background-color: red");
                textAnnouncementPane.getChildren().remove(0,6);
                //Game starts
                players[0].setTurn(true);
                Boolean gameOver = false;
                for(int i = 0; i < 200; i++){
                    players[0+(i%2)].attack(players[1-(i%2)],toServer,fromServer, ta);
                    gameOver = checkWin(i);
                    if(gameOver){
                        if(i%2 == 0) {
                            showGameOver(players[0].getName());
                        }
                        else{
                            showGameOver(players[1].getName());
                        }
                    }
                }
            }
        });
    }

    //Check to see if player has won
    public Boolean checkWin(int i){
        System.out.println("HI");
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

    public void showGameOver(String name){
        textAnnouncementPane.getChildren().removeAll();
        Label lbl = new Label("GAME OVER");
        lbl.setFont(Font.font(30));
        Label lbl2 = new Label(name +" WINS");
        lbl2.setFont(Font.font(25));
        textAnnouncementPane.getChildren().addAll(lbl,lbl2);
        Button exitGame = new Button("Exit Game");
        exitGame.setAlignment(Pos.CENTER);
        mainPane.setCenter(exitGame);
        exitGame.setOnMouseClicked(e -> {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.close();
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
