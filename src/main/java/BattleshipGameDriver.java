import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BattleshipGameDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        Board board1 = new Board();
        Board board2 = new Board();
        BorderPane leftPane = new BorderPane();     //Timer, Announcement, Move History
        BorderPane rightPane = new BorderPane();    //Ships
        BorderPane midPane = new BorderPane();
        mainPane.setLeft(leftPane);
        mainPane.setRight(rightPane);
        mainPane.setCenter(midPane);
        makeBoardPane(midPane, board1, board2);

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
        Label history = new Label("History here");
        leftPane.setBottom(historyPane);

        Player me = new Player("A");
        Player enemy = new Player("B");

        VBox enemyShips = new VBox();
        enemyShips.setStyle("-fx-border-color: black");
        enemyShips.getChildren().addAll(new Label("Enemy Ships here"), new Label(enemy.getName()));
        rightPane.setTop(enemyShips);

        VBox myShips = new VBox();
        myShips.setStyle("-fx-border-color: black");
        myShips.getChildren().addAll(new Label("My Ships here"), new Label(me.getName()));
        rightPane.setBottom(myShips);

        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.show();
    }

    private void makeBoardPane(BorderPane pane,Board board1, Board board2){
        pane.setTop(board1);
        pane.setBottom(board2);
        return;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
