import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Player {
    protected Boolean turn = false;
    Board board;
    String name;
    int count = 0;
    int shipNum = 0;
    int shipsDead = 0; // total# of dead ships
    Boolean isDead = false;
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

    // append message to BattleshipHistory.txt
    public void Print(String message){
        try {
            // Create a file
            File file = new File("BattleshipHistory.txt");
            // enable appending to an already existing file
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            // Write  message to the file
            pr.println(message);
            // Close the file

            pr.close();
            br.close();
            fr.close();

        } catch (IOException ex) {
            System.out.println("Failed to write to file");
        }
    }

    protected String getName() {
        return this.name;
    }

    public void attack(Player player2, DataOutputStream out, DataInputStream in, TextArea ta, BorderPane mainPane) {
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        for (int i = 0; i < this.board.size(); i++) {
            for (Square s : player2.board.getRow(i)) {
                s.setOnMouseClicked(e -> {
                    if (!s.getIsHit()) { //stop player from clicking buttons that are already pressed
                        if (count >= 5) {
                            if (!turn) {
                                return;
                            }
                            ta.appendText('\n' + this.name + " attacks (" + s.getX() + ", " + s.getY() + ")");
                            Print(this.name + " attacks(" + s.getX() + ", " + s.getY() +")");
                            if (s.hasShip()) {
                                ta.appendText('\n' + "HIT!!");
                                Print("HIT!!");
                                //s.setStyle("-fx-background-color: red");
                                //get hit
                                player2.fleet[s.whichShip].hit();
                                Image image = null;
                                try {
                                    image = new Image(new FileInputStream("Boat Pictures/hit.png"));
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }

                                BackgroundImage backgroundI = new BackgroundImage(image,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                        BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                                Background background = new Background(backgroundI);
                                s.setBackground(background);

                                // Verify if fleet is defeated
                                if (player2.shipsDead >= 5) {
                                    mainPane.setCenter(pane);
                                    ta.appendText('\n' + "You Win");
                                    Print("You Win");
                                    pane.getChildren().add(new Label ("You Win"));
                                    printWin(pane);
                                    return;
                                }
                            } else {
                                ta.appendText('\n' + "miss");
                                Print("miss");
                                s.setStyle("-fx-background-color: lightBlue");
                            }
                            s.setIsHit(true);

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
                                ta.appendText('\n' + player2.getName() + " attacks (" + s.getX() + ","+ s.getY() + ")");
                                Print(player2.getName() + " attacks(" + s.getX() + ","+ s.getY() + ")");

                                //Check for hit
                                if (temp.hasShip()) {
                                    ta.appendText('\n' + "Just Hit!!");
                                    Print("Hit!!");
                                    //temp.setStyle("-fx-background-color: red");
                                    //get hit
                                    this.fleet[s.whichShip].hit();

                                    Image image2 = null;
                                    try {
                                        image2 = new Image(new FileInputStream("Boat Pictures/hit.png"));
                                    } catch (FileNotFoundException ex) {
                                        ex.printStackTrace();
                                    }

                                    BackgroundImage background2 = new BackgroundImage(image2,BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                                    Background background2a = new Background(background2);
                                    temp.setBackground(background2a);


                                    // Verify if fleet is defeated
                                    if (this.shipsDead >= 5) {
                                        mainPane.setCenter(pane);
                                        pane.getChildren().add(new Label("YOU LOSE"));
                                        printWin(pane);
                                        return;
                                    }
                                } else {
                                    ta.appendText('\n' + "miss");
                                    Print("miss");
                                    temp.setStyle("-fx-background-color: lightBlue");
                                }
                            } catch (IOException err) {
                                System.out.println("fatal error");
                            }
                        } else {
                            ta.appendText('\n' + this.name + " Fix Your Ships!");
                        }
                    }
                });
            }
        }
    }

    public void setShips(Scene scene, ImageView[] ships, TextArea ta) {
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
                ta.appendText('\n' + "Ship " + setNum + " selected");
                Print("Ship " + setNum + " selected");

                // set the isHorizontal for rotation
                scene.setOnKeyPressed(ex -> {
                    if(ex.getCode() == KeyCode.R){
                        fleet[setNum].isHorizontal = !fleet[setNum].isHorizontal;
                        if(fleet[setNum].isHorizontal){
                            ta.appendText('\n' + "Horizontal");
                            Print("Horizontal");
                        }
                        else{
                            ta.appendText('\n' + "Vertical");
                            Print("Vertical");
                        }
                    }
                });
                // Iterate over the board
                for (int i = 0; i < this.board.size(); i++) {
                    for (Square sq : this.board.getRow(i)) {
                        sq.setOnMouseClicked(ex -> {
                            // The ship has been set so cannot set it again
                            if(fleet[setNum].isSet){
                                return;
                            }
                            // Check if clicked button has a ship already
                            if (sq.hasShip()) {
                                ta.appendText('\n' + "Already has a ship on it");
                                Print("Already has a ship on it");
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
                                                ta.appendText('\n' + "Collision");
                                                return;
                                            }
                                        }
                                        catch(ArrayIndexOutOfBoundsException a){
                                            ta.appendText('\n' + "Out of Bounds");
                                            return;
                                        }
                                    }
                                    else{
                                        //The try is to see if the ship will be placed outside of the board
                                        try {
                                            //This is to see if the ship is placed on top of another
                                            if (this.board.getCol(sq.getX())[sq.getY() + k].hasShip()) {
                                                ta.appendText('\n' + "Collision");
                                                return;
                                            }
                                        }
                                        catch(ArrayIndexOutOfBoundsException a){
                                            ta.appendText('\n' + "Out of Bounds");
                                            return;
                                        }
                                    }
                                }

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
                                        this.board.getRow(sq.getY())[sq.getX()+k].setHasShip(true);
                                        this.board.getRow(sq.getY())[sq.getX()+k].setBackground(background);

                                        // assign Ship to the square
                                        this.board.getRow(sq.getY())[sq.getX()+k].setWhichShip(setNum);
                                    }
                                    // vertical ship placement
                                    else {
                                        BackgroundImage backgroundI = new BackgroundImage(fleet[setNum].shipPictureH,
                                                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                                new BackgroundPosition(Side.RIGHT, 0.5,true,
                                                        Side.TOP, 0 -(k*30),false),
                                                BackgroundSize.DEFAULT);
                                        Background background = new Background(backgroundI);
                                        this.board.getCol(sq.getX())[sq.getY()+k].setHasShip(true);
                                        this.board.getCol(sq.getX())[sq.getY()+k].setBackground(background);

                                        // assign Ship to the square
                                        this.board.getCol(sq.getX())[sq.getY()+k].setWhichShip(setNum);
                                    }
                                }
                                if (count == 5) {
                                    this.turn = true;
                                }
                                this.count++;
                            } else {
                                ta.appendText('\n' + "Your ships are set, ready up!");
                                Print("Your ships are set, ready up!");}
                        });
                    }
                }
            });
        }
    }
    public void setAIShips() {
        int[] shipSizes = {2,3,3,4,5};          //array of sizes of each ship
        Map<Integer,Integer[][]> shipInfo = new HashMap<>();
        Integer loc[][];
        Boolean flag;
        for(int s = 0; s < 5; s++) {
            flag = true;
            int x = (int) (Math.random()*10);       //generate x
            int y = (int) (Math.random()*10);       //generate y
            int horiz = (int) (Math.random()*2);    //generate horizontal
            loc = new Integer[5][2];
            System.out.println("ship: " + s + " horizontal: " + horiz);
            for (int i = 0; i < shipSizes[s]; i++) {
                //check for collision
                if (horiz == 0) {
                    try {
                        if (this.board.getRow(y)[x + i].hasShip()) {
                            System.out.println("Has Ship: x = " + x);
                            s--;
                            flag = false;
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException l) {
                        System.out.println("Out of Bounds: x = " + x);
                        s--;
                        flag = false;
                        break;
                    }
                } else if (horiz == 1) {
                    try {
                        if (this.board.getCol(x)[y + i].hasShip()) {
                            System.out.println("Has Ship: y = " + y);
                            s--;
                            flag = false;
                            break;
                        }
                    } catch (ArrayIndexOutOfBoundsException l) {
                        System.out.println("Out of Bounds: y = " + y);
                        s--;
                        flag = false;
                        break;
                    }
                }
            }
            if(flag){
                for(int i = 0; i < shipSizes[s]; i++){
                    if(horiz == 0){
                        loc[i][0] = x + i;
                        loc[i][1] = y;
                        this.board.getRow(y)[x+i].setHasShip(true);
                        this.board.getRow(y)[x+i].setWhichShip(s);

                    }
                    else{
                        loc[i][0] = x;
                        loc[i][1] = y + i;
                        this.board.getCol(x)[y+i].setHasShip(true);
                        this.board.getCol(x)[y+i].setWhichShip(s);
                    }
                }
                shipInfo.put(s, loc);
                count++;
            }
        }
        for(Integer[][] i:shipInfo.values()) {
            System.out.println(i[0][0] + " " + i[0][1]);
            Print(i[0][0] + " " + i[0][1]);
            System.out.println(i[1][0] + " " + i[1][1]);
            Print(i[1][0] + " " + i[1][1]);
            System.out.println(i[2][0] + " " + i[2][1]);
            Print(i[2][0] + " " + i[2][1]);
            System.out.println(i[3][0] + " " + i[3][1]);
            Print(i[3][0] + " " + i[3][1]);
            System.out.println(i[4][0] + " " + i[4][1]);
            Print(i[4][0] + " " + i[4][1]);
        }
    }

    public void printWin(VBox pane){
        Button exitGame = new Button("Exit Game");
        exitGame.setAlignment(Pos.CENTER);
        pane.getChildren().add(exitGame);

        exitGame.setOnMouseClicked(e -> {
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.close();
        });
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
