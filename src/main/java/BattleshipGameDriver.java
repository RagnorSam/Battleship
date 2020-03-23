import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
        BorderPane midPane = new BorderPane();
        mainPane.setLeft(leftPane);
        mainPane.setCenter(midPane);
        makeBoardPane(midPane, board1, board2);

        StackPane timerPane = new StackPane();
        timerPane.setStyle("-fx-border-color: black");
        GameTimer gtimer = new GameTimer();
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

        VBox rightPane = new VBox();
        rightPane.setStyle("-fx-border-color: black");
        rightPane.getChildren().addAll(new Label("Enemy Ships here"), new Label(enemy.getName()));
        rightPane.getChildren().addAll(new Label("My Ships here"), new Label(me.getName()));

        mainPane.setRight(rightPane);

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
