import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class Square extends Button {
    private int x;
    private int y;
    private Boolean hasShip = false;
    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-background-color: white; -fx-border-color: black");
    }

    Square(int x, int y) { //added for testing
        this();
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return this.x;
    }
    public int getY()
    {
        return this.y;
    }
    public boolean hasShip(){
        return hasShip;
    }
}