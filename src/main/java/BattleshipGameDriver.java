import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BattleshipGameDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Board board = new Board();
        Player player1 = new Player("Dimitri");

        Scene scene = new Scene(board);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
