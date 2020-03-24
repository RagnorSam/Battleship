import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Ship {
    int length;
    boolean horizontal = false; // when this is false the ship is vertical
    Image img;
    int life;
    String name;

    Ship(int length, String name){
        this.length = length;
        this.name = name;
        try {
            this.img = new Image(new FileInputStream("Boat Pictures/" + name + ".png"),
                    35*(length),35,false,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.life = length;
    }

    public void hit(){
        this.life--;
    }

    public boolean isDestroyed(){
        if(this.life == 0) {
            return true;
        }
        else{
            return false;
        }
    }
    public Image getImg(){
        return this.img;
    }
    public String getName(){
        return this.name;
    }

}