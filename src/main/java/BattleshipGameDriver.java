import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
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
