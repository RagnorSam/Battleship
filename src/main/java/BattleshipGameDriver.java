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
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileInputStream;


public class BattleshipGameDriver extends Application {
    BorderPane mainPane = new BorderPane();     //Main game pane
    BorderPane leftPane = new BorderPane();     //Timer, Announcement, Move History
    BorderPane midPane = new BorderPane();      //Display of both Boards
    BorderPane rightPane = new BorderPane();                //Display ships' status for both players
    GridPane enemyGridPane = new GridPane();
    GridPane myGridPane = new GridPane();
    Player[] players = new Player[2];
    GameTimer gtimer = new GameTimer();
    StackPane textAnnouncementPane;
    ImageView[] ships;
    VBox enemyVbox = new VBox();
    VBox myVbox = new VBox();

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

    //create the "strarting screen"
    private void makeGameScreen(){
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

        textAnnouncementPane = new StackPane();
        textAnnouncementPane.setStyle("-fx-border-color: black");
        Label textAnnouncement = new Label("Text Announcement here");
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
        ships = new ImageView[10];
        int count = 0;
        for(Ship s: players[1].fleet){
            //load image
            //ImageView imgs = new ImageView();
            //imgs.setStyle("-fx-border-color: black");
            //ships[count] = imgs;
            //enemyVbox.getChildren().add(imgs);
            count++;
        }
        rightPane.setTop(enemyVbox);
        HBox border = new HBox(new Label(""));
        border.setStyle("-fx-background-color: black");
        rightPane.setCenter(border);
        rightPane.setBottom(myVbox);
        myVbox.setAlignment(Pos.TOP_LEFT);
        myVbox.setSpacing(35);
        myVbox.setPadding(new Insets(5,5,5,5));
        for(Ship s: players[0].fleet){
            //Load image
            //ImageView imgs = new ImageView(s.getImg());
            //imgs.setStyle("-fx-border-color: black");
            //ships[count] = imgs;
            //myVbox.getChildren().add(imgs);
            count++;
        }

        mainPane.setRight(rightPane);
        Scene scene = new Scene(mainPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Battleship");
        stage.show();
    }

    //show your board and enemy's board
    private void displayBoard() {
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

        setShips();
    }

    public void setShips(){
        players[0].setShips(mainPane, ships);
        players[1].setShips(mainPane, ships);
        Button bt = new Button("READY");
        textAnnouncementPane.getChildren().add(bt);
        bt.setOnMouseClicked(e -> {
            if(players[0].count >= 5 && players[1].count >= 5) {
                bt.setStyle("-fx-background-color: red");
                players[0].setTurn(true);
                textAnnouncementPane.getChildren().removeAll();
                Boolean gameOver = false;
                for(int i =0; i < 200; i++){
                    players[0+(i%2)].attack(players[1-(i%2)]);
                    gameOver = checkWin(i);
                    if(gameOver){
                        //gameOver Screen
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

    @Override
    public void stop(){
        gtimer.stopTime(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
