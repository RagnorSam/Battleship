import javafx.scene.layout.Pane;

public class Square extends Pane {
    Boolean hasShip = false;
    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-background-color: blue");
        this.setStyle("-fx-border-color: black");
    }
}
