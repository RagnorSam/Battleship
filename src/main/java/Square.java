import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Square extends Pane {
    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-border-color: black");
        this.getChildren().add(new Text("hi"));
    }

}
