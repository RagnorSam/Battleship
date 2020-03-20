import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Square extends Pane {
    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-background-color: blue");
        this.setStyle("-fx-border-color: black");
    }
}
