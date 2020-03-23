import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class Square extends Button {
    Boolean hasShip = false;
    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-background-color: white");
    }
    public boolean hasShip(){
        return hasShip;
    }
}