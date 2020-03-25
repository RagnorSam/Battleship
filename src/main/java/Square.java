import javafx.scene.control.Button;

public class Square extends Button {
    private int x;
    private int y;
    private Boolean hasShip = false;
    private Boolean isHit = false;

    Square(){
        this.setMinSize(30,30);
        this.setStyle("-fx-border-color: black");
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
    public void setX(int x){ this.x = x; }
    public int getY()
    {
        return this.y;
    }
    public void setY(int y){ this.y = y; }
    public boolean hasShip(){ return hasShip; }
    public void setHasShip(boolean hasShip) { this.hasShip = hasShip;}
    public boolean getIsHit() { return this.isHit; }
    public void setIsHit(boolean isHit) { this.isHit = isHit; }
}
