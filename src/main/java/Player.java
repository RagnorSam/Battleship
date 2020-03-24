import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.crypto.Data;
import java.io.*;

public class Player {
    protected Boolean turn = false;
    Board board;
    String name;
    int count = 0;
    Ship[] fleet = new Ship[5];

    Player() {
        this.board = new Board();
        Image img = null;
        try {
            img = new Image(new FileInputStream("Boat Pictures/Boat2.png"));
            fleet[0] = new Ship(this,2, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat3a.png"));
            fleet[1] = new Ship(this,3, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat3b.png"));
            fleet[2] = new Ship(this,3, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat4.png"));
            fleet[3] = new Ship(this,4, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat5.png"));
            fleet[4] = new Ship(this,5, img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    Player(String name) {
        this.name = name;
        this.board = new Board();
        Image img = null;
        try {
            img = new Image(new FileInputStream("Boat Pictures/Boat2.png"));
            fleet[0] = new Ship(this,2, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat3a.png"));
            fleet[1] = new Ship(this,3, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat3b.png"));
            fleet[2] = new Ship(this,3, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat4.png"));
            fleet[3] = new Ship(this,4, img);
            img = new Image(new FileInputStream("Boat Pictures/Boat5.png"));
            fleet[4] = new Ship(this,5, img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected String getName() {
        return this.name;
    }

    public int[] attack(Player player2, DataOutputStream out, DataInputStream in) {
        int[] loc = new int[2];
        for (int i = 0; i < this.board.size(); i++) {
            for (Square s : player2.board.getRow(i)) {
                s.setOnMouseClicked(e -> {
                    if (count >= 5) {
                        if (!turn) {
                            return;
                        }
                        System.out.println(this.name + " attack " + e.getTarget());
                        if (s.hasShip()) {
                            System.out.println("HIT!!");
                            s.setStyle("-fx-background-color: red");
                        }
                        else {
                            s.setStyle("-fx-background-color: grey");
                        }
                        //player2.setTurn(true);
                        //this.turn = false;

                        //Run other player's turn (using server) (for testing purposes)
                        try {
                            //Send data
                            out.writeUTF(name);
                            out.writeInt(s.getX());
                            out.writeInt(s.getY());

                            //Receive attack info
                            int serverAtkX = in.readInt();
                            int serverAtkY = in.readInt();

                            //Check for hit
                            Square temp = board.board[serverAtkX][serverAtkY];
                            if(temp.hasShip()) {
                                System.out.println("HIT!!");
                                temp.setStyle("-fx-background-color: red");
                            }
                            else {
                                temp.setStyle("-fx-background-color: grey");
                            }
                        } catch (IOException err) {
                            System.out.println("fatal error");
                        }
                    } else {
                        System.out.println(this.name + " Fix Your Ships!");
                    }
                });
            }
        }
        return loc;
    }

    public void setShips() {
        for (int i = 0; i < this.board.size(); i++) {
            for (Square s : this.board.getRow(i)) {
                s.setOnMouseClicked(e -> {
                    if (count < 5) {
                        if (s.hasShip()) {
                            System.out.println("Already has a ship on it");
                            return;
                        }
                        System.out.println(this.name + " ship #" + count + " set at square " + s.getX() + " " + s.getY());
                        s.setShip(true);
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
