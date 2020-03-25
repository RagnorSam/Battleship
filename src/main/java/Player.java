import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Player {
    protected Boolean turn = false;
    Board board;
    String name;
    int count = 0;
    int shipNum = 0;
    Ship[] fleet = new Ship[5];
    Map<ImageView, Integer> mapShips = new HashMap<>(); //To recognize which ship was clicked

    Player() {
        this.board = new Board();
        fleet[0] = new Ship(this,2, "Boat2");
        fleet[1] = new Ship(this,3, "Boat3a");
        fleet[2] = new Ship(this,3, "Boat3b");
        fleet[3] = new Ship(this,4, "Boat4");
        fleet[4] = new Ship(this,5, "Boat5");
    }

    Player(String name) {
        this.name = name;
        this.board = new Board();
        fleet[0] = new Ship(this,2, "Boat2");
        fleet[1] = new Ship(this,3, "Boat3a");
        fleet[2] = new Ship(this,3, "Boat3b");
        fleet[3] = new Ship(this,4, "Boat4");
        fleet[4] = new Ship(this,5, "Boat5");
    }

    protected String getName() {
        return this.name;
    }

    public void attack(Player player2, DataOutputStream out, DataInputStream in) {
        for (int i = 0; i < this.board.size(); i++) {
            for (Square s : player2.board.getRow(i)) {
                s.setOnMouseClicked(e -> {
                    if(!s.getIsHit()) { //stop player from clicking buttons that are already pressed
                        if (count >= 5) {
                            if (!turn) {
                                return;
                            }
                            System.out.println(this.name + " attack " + e.getTarget());
                            if (s.hasShip()) {
                                System.out.println("HIT!!");
                                s.setStyle("-fx-background-color: red");
                                //Implement the damage later
                                //ship.hit();
                            } else {
                                s.setStyle("-fx-background-color: grey");
                            }
                            s.setIsHit(true);

                            //player2.setTurn(true);
                            //this.turn = false;

                            //Run other player's turn (using server) (for testing purposes)
                            try {
                                //Send data
                                out.writeUTF(name);
                                out.writeInt(s.getX());
                                out.writeInt(s.getY());

                                //Check if valid move
                                //Receive attack info
                                int serverAtkX = in.readInt();
                                int serverAtkY = in.readInt();
                                Square temp = board.board[serverAtkX][serverAtkY];

                                //Check for hit
                                if (temp.hasShip()) {
                                    System.out.println("HIT!!");
                                    temp.setStyle("-fx-background-color: red");
                                } else {
                                    temp.setStyle("-fx-background-color: grey");
                                }
                            } catch (IOException err) {
                                System.out.println("fatal error");
                            }
                        } else {
                            System.out.println(this.name + " Fix Your Ships!");
                        }
                    }
                });
            }
        }
    }

    public void setShips(Scene scene, ImageView[] ships, GridPane pane) {
        // place the ships imageview in map
        for(ImageView s: ships){
            mapShips.put(s, shipNum);
            shipNum++;
        }

        System.out.println(mapShips.get(ships[0]));
        for(ImageView s: ships){
            s.setOnMouseClicked(e -> {
                // get the ship that has been clicked
                int setNum = mapShips.get(e.getTarget());
                // set the isHorizontal for rotation
                scene.setOnKeyPressed(ex -> {
                    if(ex.getCode() == KeyCode.R){
                        fleet[setNum].isHorizontal = !fleet[setNum].isHorizontal;
                    }
                });
                // Iterate over the board
                for (int i = 0; i < this.board.size(); i++) {
                    for (Square sq : this.board.getRow(i)) {
                        sq.setOnMouseClicked(ex -> {
                            // Check if clicked button has a ship already
                            if (sq.hasShip()) {
                                System.out.println("Already has a ship on it");
                                return;
                            }
                            // The ship has been set so cannot set it again
                            if(fleet[setNum].isSet){
                                return;
                            }
                            // Check if you have set all 5 ships
                            if (count < 5) {
                                //Check to see if the location has an error
                                for(int k = 0; k < fleet[setNum].shipSize; k++){
                                    if(fleet[setNum].isHorizontal){
                                        //The try is to see if the ship will be placed outside of the board
                                        try{
                                            //This is to see if the ship is placed on top of another
                                            if(this.board.getRow(sq.getY())[sq.getX()+k].hasShip()){
                                                System.out.println("Colliding");
                                                return;
                                            }
                                        }
                                        catch(ArrayIndexOutOfBoundsException a){
                                            System.out.println("Out Of Bounds");
                                            return;
                                        }
                                    }
                                    else{
                                        //The try is to see if the ship will be placed outside of the board
                                        try {
                                            //This is to see if the ship is placed on top of another
                                            if (this.board.getCol(sq.getX())[sq.getY() + k].hasShip()) {
                                                System.out.println("Collide");
                                                return;
                                            }
                                        }
                                        catch(ArrayIndexOutOfBoundsException a){
                                            System.out.println("Out of Bounds");
                                            return;
                                        }
                                    }
                                }

                                System.out.println(this.name + " ship #" + count + " set at square "
                                        + sq.getX() + " " + sq.getY());
                                fleet[setNum].isSet = !fleet[setNum].isSet;

                                //Load the ship Image as Background in the buttons
                                for(int k = 0; k < fleet[setNum].shipSize; k++){
                                    //horizontal ship placement
                                    if(fleet[setNum].isHorizontal){
                                        BackgroundImage backgroundI = new BackgroundImage(fleet[setNum].shipPicture,
                                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                                new BackgroundPosition(Side.LEFT, 0 -(30*k),false,
                                                        Side.BOTTOM,0.5,true),
                                                BackgroundSize.DEFAULT);
                                        Background background = new Background(backgroundI);
                                        this.board.getRow(sq.getY())[sq.getX()+k].setShip(true);
                                        this.board.getRow(sq.getY())[sq.getX()+k].setBackground(background);
                                        System.out.println("hello");
                                    }
                                    // vertical ship placement
                                    else {
                                        BackgroundImage backgroundI = new BackgroundImage(fleet[setNum].shipPictureH,
                                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                                new BackgroundPosition(Side.RIGHT, 0.5,true,
                                                        Side.TOP, 0 -(k*30),false),
                                                BackgroundSize.DEFAULT);
                                        Background background = new Background(backgroundI);
                                        this.board.getCol(sq.getX())[sq.getY()+k].setShip(true);
                                        this.board.getCol(sq.getX())[sq.getY()+k].setBackground(background);
                                        System.out.println("hi");
                                    }
                                }
                                if (count == 5) {
                                    this.turn = true;
                                }
                                this.count++;
                            } else {
                                System.out.println(this.name + " Your ships are set. Go to war!");
                            }
                        });
                    }
                }
            });
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTurn() {
        this.turn = !turn;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    public Boolean getTurn() {
        return this.turn;
    }
}
