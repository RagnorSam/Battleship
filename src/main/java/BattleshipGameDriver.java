import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BattleshipGameDriver extends Application {
    BorderPane mainPane = new BorderPane();     //Main game pane
    BorderPane leftPane = new BorderPane();     //Timer, Announcement, Move History
    BorderPane midPane = new BorderPane();      //Display of both Boards
    VBox rightPane = new VBox();                //Display ships' status for both players
    GridPane enemyGridPane = new GridPane();
    GridPane myGridPane = new GridPane();
    Player[] players = new Player[2];
    GameTimer gtimer;
    StackPane textAnnouncementPane;
    ImageView[] ships;

    @Override
    public void start(Stage stage) throws Exception {
        makeGameScreen();

        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.show();
        //Gameplay


    }

    //create the "starting screen"
    private void makeGameScreen(){
        players[0] = new Player();
        players[1] = new Player("player 2");
        mainPane.setMinSize(700, 750);
        mainPane.setStyle("-fx-background-color: blue");
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
            displayBoard();
        });
        nameField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                players[0].setName(nameField.getText());
                displayBoard();
            }
        });

        mainPane.setCenter(midPane);

        //leftPane
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black");
        gtimer = new GameTimer();
        gtimer.startTime(00);
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
        rightPane.setStyle("-fx-border-color: black");
        rightPane.setAlignment(Pos.TOP_LEFT);
        rightPane.setSpacing(35);
        rightPane.setPadding(new Insets(5,5,5,5));
        Text lbl = new Text("Enemy Ships here");
        rightPane.getChildren().add(lbl);
        ships = new ImageView[10];
        int count = 0;
        for(Ship s: players[1].fleet){
            ImageView imgs = new ImageView(s.getImg());
            ships[count] = imgs;
            rightPane.getChildren().add(imgs);
            count++;
        }

        rightPane.getChildren().add(new Label("My Ships here"));

        for(Ship s: players[0].fleet){
            ImageView imgs = new ImageView(s.getImg());
            ships[count] = imgs;
            rightPane.getChildren().add(imgs);
            count++;
        }


        mainPane.setRight(rightPane);
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
        players[0].setShips(rightPane, ships);
        players[1].setShips(rightPane, ships);
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
                    checkWin(i);
                }
            }
        });
    }

    public void checkWin(int i){
        //Player 1 attacked last so check for win
        if(i%2 == 0){

        }
        //else check Player 2 for win
        else{

        }
    }

    @Override
    public void stop(){
        gtimer.stopTime(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
