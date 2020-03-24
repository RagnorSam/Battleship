import javafx.scene.control.Button;

public class Square extends Button {
    int x;
    int y;
    Boolean hasShip = false;
    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-background-color: white; -fx-border-color: black");
    }
    public boolean hasShip(){
        return hasShip;
    }
    public int getX(){ return this.x; }
    public int getY(){ return this.y; }
    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
}
