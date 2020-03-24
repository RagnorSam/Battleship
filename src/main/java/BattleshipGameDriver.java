//import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;

public class BattleshipGameDriver extends Application {
    BorderPane mainPane = new BorderPane();    //Main game pane
    BorderPane leftPane = new BorderPane();    //Timer, Announcement, Move History
    BorderPane midPane = new BorderPane();     //Display of both Boards
    VBox rightPane = new VBox();         //Display ships' status for both players
    GridPane enemyGridPane = new GridPane();
    GridPane myGridPane = new GridPane();
    Player[] players = new Player[2];
    Board board1 = new Board();           //Player1's board
    Board board2 = new Board();           //Player2's board

    GameTimer gtimer = new GameTimer(); //time for how long the game takes

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
        BackgroundImage backgroundI = new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundI);
        menuPane.setBackground(background);

        //Button functionality
        buttons.getChildren().addAll(host,join);
        menuPane.setCenter(buttons);
        host.setOnAction(e -> {
            //Start server instance
            //Connect to server
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

    //create the "starting screen"
    private void makeGameScreen(){
        players[0] = new Player();
        mainPane.setMinSize(620, 720);
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
            players[0].setTurn(true);
            gtimer.startTime(00); //start timer after name has been entered
            displayBoard();
        });
        nameField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                players[0].setName(nameField.getText());
                players[0].setTurn(true);
                gtimer.startTime(00); //start timer after name has been entered
                displayBoard();
            }
        });

        mainPane.setCenter(midPane);

        //leftPane
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black");
        gtimer = new GameTimer();
        Label timer = new Label(gtimer.getTotalTime().get());
        gtimer.getTotalTime().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(() -> timer.setText(gtimer.getTotalTime().get()));
            }
        });
        timerPane.getChildren().add(timer);
        leftPane.setTop(timerPane);

        StackPane textAnnouncementPane = new StackPane();
        textAnnouncementPane.setStyle("-fx-border-color: black");
        Label textAnnouncement = new Label("Text Announcement here");
        textAnnouncementPane.getChildren().add(textAnnouncement);
        leftPane.setCenter(textAnnouncementPane);

        ScrollPane historyPane = new ScrollPane();
        historyPane.setStyle("-fx-border-color: black");
        leftPane.setBottom(historyPane);
        mainPane.setLeft(leftPane);

        //rightPane
        rightPane.setStyle("-fx-border-color: black");
        rightPane.getChildren().addAll(new Label("Enemy Ships here"));
        rightPane.getChildren().addAll(new Label("My Ships here"));
        mainPane.setRight(rightPane);

        Scene scene = new Scene(mainPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Battleship");
        stage.show();

        //Gameplay
        players[1] = new Player("player 2");

        players[0].attack(players[1]);
    }

    //show your board and enemy's board
    private void displayBoard(){
        enemyGridPane.setPadding(new Insets(5, 5, 5, 5));
        enemyGridPane.setAlignment(Pos.CENTER);
        myGridPane.setPadding(new Insets(5, 5, 5, 5));
        myGridPane.setAlignment(Pos.CENTER);
        midPane.getChildren().removeAll();
        HBox border = new HBox(new Label(""));
        border.setStyle("-fx-background-color: black");
        midPane.setCenter(border);

        enemyGridPane.setHgap(5);
        enemyGridPane.setVgap(5);
        myGridPane.setVgap(5);
        myGridPane.setHgap(5);

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                enemyGridPane.add(players[1].board.board[i][k], i, k);
                myGridPane.add(players[0].board.board[i][k], i, k);
            }
        }
        midPane.setTop(enemyGridPane);
        midPane.setBottom(myGridPane);
    }

    @Override
    public void stop(){
        gtimer.stopTime(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}