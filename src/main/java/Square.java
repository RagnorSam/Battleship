import javafx.scene.layout.Pane;

public class Square extends Pane {
    private Boolean hasShip = false;
    private int x;
    private int y;

    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-background-color: blue");
        this.setStyle("-fx-border-color: black");
    }

    Square(int x, int y) { //delete this if not storing coordinates here
        this();
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
