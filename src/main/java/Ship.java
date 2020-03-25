import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

public class Ship implements Serializable {
    Player player;
    public boolean isSet;
    public int shipSize;
    public int shipLives;
    public int[] shipLocation;
    public int squareSize = 10;
    public boolean isAlive = true;
    public boolean isHorizontal = true; // when this is false the ship is vertical
    public Image shipPicture;
    /*
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

     */



    Ship(Player player, int shipSize, String name){
        this.player = player;
        this.shipSize = shipSize;
        this.shipLives = shipSize;
        this.isSet = false;
        try {
            this.shipPicture = new Image(new FileInputStream("Boat Pictures/" + name + ".png"),
                    35*(shipSize),35,false,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Ship(int shipSize, String name){
        this.player = null;
        this.shipSize = shipSize;
        this.shipLives = shipSize;
        this.isSet = false;
        try {
            this.shipPicture = new Image(new FileInputStream("Boat Pictures/" + name + ".png"),
                    35*(shipSize),35,false,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setShipLocation(Ship ship){
        if(this.isHorizontal==true){
            for(int i = 0; i<this.shipSize; i++){
                this.shipLocation[i] =1;
            }
        }
        else{
            for(int i = 0; i<this.shipSize; i++){
                this.shipLocation[i] = 1;
            }
        }


    }

    public boolean checkShipLives(Ship ship){
        if(this.shipLives <= 0){
            isAlive = false;
            return false;
        }
        return true;
    }

    public void hit(){
        this.shipLives--;
    }
}


/* this should be added to the player class,
    private Ship[] fleet;
    private Ship ship1;
    private Ship ship2;
    private Ship ship3;
    private Ship ship4;
    private Ship ship5;

    // create fleet
    createFleet(Player player){
        this.ship1 = Ship(player,2,shipPicture1);
        fleet[0] = this.ship1 ;

        this.ship2 = Ship(player,3,shipPicture2);
        fleet[1] = this.ship2 ;

        this.ship3 = Ship(player,3,shipPicture3);
        fleet[2] = this.ship3 ;

        this.ship4 = Ship(player,4,shipPicture4);
        fleet[3] = this.ship4 ;

        this.ship5 = Ship(player,5,shipPicture5);
        fleet[5] = this.ship5 ;
    }

    // run function that will allow you to place the ships
    //player class should place the ships after creating them
    public placeShip(Player player, Ship ship){

        // upload image
        Pane pane = new Pane();
        final ImageView selectedImage = new ImageView();
        Image image1 = new Image(new File("boat5.png").toURI().toString(),  100, 100, true, true);
        selectedImage.setImage(image1);
        pane.getChildren().addAll(selectedImage);

        // move boat image in multiple direction and allow image to rotate
        // call this function only when you are placing boats. need to add button that says placing boat
        // and done placing boats. Then call this function: setShipLocation(Ship ship)
        //if (isPlacingBoats == true){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                int shipAngle = 0;
                if (event.getCode() == KeyCode.RIGHT) {
                    selectedImage.setLayoutX(selectedImage.getLayoutX() + 10);
                } else if (event.getCode() == KeyCode.LEFT) {
                    selectedImage.setLayoutX(selectedImage.getLayoutX() - 10);
                } else if (event.getCode() == KeyCode.DOWN){
                    selectedImage.setLayoutY(selectedImage.getLayoutY() + 10);
                } else if (event.getCode() == KeyCode.UP){
                    selectedImage.setLayoutY(selectedImage.getLayoutY() - 10);
                } else if (event.getCode() == KeyCode.H){
                    isHorizontal = true;
                    shipAngle = 0;
                    selectedImage.setRotate(shipAngle);
                } else if (event.getCode() == KeyCode.V){
                    isHorizontal = false;
                    shipAngle -= 90;
                    selectedImage.setRotate(shipAngle);
                }
            }
        });

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


 */