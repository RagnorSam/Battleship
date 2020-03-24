import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.File;

public class Ship {
    Player player;
    public int shipSize;
    public int shipLives;
    public int[] shipLocation;
    public int squareSize = 10;
    public boolean isAlive = true;
    public boolean isHorizontal; // when this is false the ship is vertical
    public Image shipPicture;


    Ship(Player player, int shipSize, Image shipPicture){
        this.player = player;
        this.shipSize = shipSize;
        this.shipLives = shipSize;
        this.shipPicture = shipPicture;
    }

    public void setShipLocation(Ship ship){
        if(this.isHorizontal==true){
            for(int i = 0; i<this.shipSize; i++){
                this.shipLocation[i] = {this.shipPicture.getLayoutX()+squareSize,  this.shipPicture.getLayoutY()};
            }
        }
        else{
            for(int i = 0; i<this.shipSize; i++){
                this.shipLocation[i] = {this.shipPicture.getLayoutX(),  this.shipPicture.getLayoutY()-squareSize};
            }
        }
    }

    public boolean checkShipLives(Ship ship){
        if(this.shipLives <= 0){
            isAlive = false;
            return false;
        }
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

    }
    }

    */