import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BattleshipGameDriver extends Application {
    BorderPane mainPane;    //Main game pane
    Board board1;           //Player1's board
    Board board2;           //Player2's board
    BorderPane leftPane;    //Timer, Announcement, Move History
    BorderPane midPane;     //Display of both Boards
    VBox rightPane;         //Display ships' status for both players
    Player player1;
    Player player2;

    @Override
    public void start(Stage stage) throws Exception {
        makeGameScreen();

        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.show();
    }

    private void makeGameScreen(){
        board1 = new Board();
        board2 = new Board();
        mainPane = new BorderPane();
        leftPane = new BorderPane();
        midPane = new BorderPane();
        rightPane = new VBox();

        mainPane.setMinSize(535, 500);
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
            player1 = new Player(nameField.getText());
            makeBoard();
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

    private void makeBoard(){
        mainPane.setLeft(leftPane);
        mainPane.setCenter(midPane);
        HBox border = new HBox(new Label(""));
        border.setStyle("-fx-background-color: black");
        midPane.setCenter(border);
        //midPane.setTop(board1);
        //midPane.setBottom(board2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
