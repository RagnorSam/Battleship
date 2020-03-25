import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Ship {

//                // beginning of file IO
//                java.io.File file = new java.io.File("BattleshipHistory.txt");
//                // Create a file
//                java.io.PrintWriter output = new java.io.PrintWriter(file);
//                // Write formatted output to the file
//                output.print("soesdensJohn T Smith ");
//                output.println(90);
//                output.print("Eric K Jones ");
//                output.println(85);
//
//                // Close the file
//                output.close();
//
//                // end of fileIO

    Player player;
    public boolean isSet;
    public int shipSize;
    public int shipLives;
    public boolean isAlive = true;
    public boolean isHorizontal = true; // when this is false the ship is vertical
    public Image shipPicture;
    public Image shipPictureH;

    Ship(Player player, int shipSize, String name){
        this.player = player;
        this.shipSize = shipSize;
        this.shipLives = shipSize;
        this.isSet = false;

        try {
            this.shipPicture = new Image(new FileInputStream("Boat Pictures/" + name + ".png"),
                    30*(shipSize),30,false,true);
            this.shipPictureH = new Image(new FileInputStream("Boat Pictures/" + name + " - Copy.png"),
                    30,30*(shipSize),false,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void hit(){
        shipLives--;
        System.out.println("life reduced to");

        System.out.println(shipLives);

        if(shipLives == 0){
            System.out.println("ship is dead");
            isAlive = false;
            player.shipsDead++;

        }
        else {
            isAlive = true;
            System.out.println("ship is not dead");
        }
    }
}
