import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
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

    @Override
    public void start(Stage stage) throws Exception {
        makeGameScreen();

        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.show();
        //Gameplay
        players[1] = new Player("erkjv");

        players[0].attack(board2,players[1]);
    }

    //create the "starting screen"
    private void makeGameScreen(){
        players[0] = new Player();
        mainPane.setMinSize(620, 720);
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
            players[0].setTurn(true);
            players[1].setTurn(false);
            makeBoard();
        });
        nameField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                players[0].setName(nameField.getText());
                players[0].setTurn(true);
                players[1].setTurn(false);
                makeBoard();
            }
        });

        mainPane.setCenter(midPane);

        //leftPane
        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black");
        Label timer = new Label("Timer here");
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
        Player me = new Player("A");
        Player enemy = new Player("B");

        rightPane.setStyle("-fx-border-color: black");
        rightPane.getChildren().addAll(new Label("Enemy Ships here"), new Label(enemy.getName()));
        rightPane.getChildren().addAll(new Label("My Ships here"), new Label(me.getName()));
        mainPane.setRight(rightPane);
    }

    //Set your ships on your board
    private void makeBoard(){
        midPane.getChildren().removeAll();
        midPane.setCenter(myGridPane);

        //set the pieces
        players[0].setShips();

        //After pieces are set
        displayBoard();
    }

    //show your board and enemy's board
    private void displayBoard(){
        enemyGridPane.setPadding(new Insets(5,5,5,5));
        enemyGridPane.setAlignment(Pos.CENTER);
        myGridPane.setPadding(new Insets(5,5,5,5));
        myGridPane.setAlignment(Pos.CENTER);
        midPane.getChildren().removeAll();
        HBox border = new HBox(new Label(""));
        border.setStyle("-fx-background-color: black");
        midPane.setCenter(border);

        enemyGridPane.setHgap(5);
        enemyGridPane.setVgap(5);
        myGridPane.setVgap(5);
        myGridPane.setHgap(5);

        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++){
                enemyGridPane.add(board2.board[i][k], i, k);
                myGridPane.add(board1.board[i][k], i, k);
            }
        }
        midPane.setTop(enemyGridPane);
        midPane.setBottom(myGridPane);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
