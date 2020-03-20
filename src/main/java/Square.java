import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Square extends Pane {
    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-border-color: black");
        Text txt = new Text("hi");
        txt.setY(30);
        this.getChildren().add(txt);
    }

}
