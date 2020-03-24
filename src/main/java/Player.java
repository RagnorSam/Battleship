import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Player {
    protected Boolean turn = false;
    Board board;
    String name;
    int count = 0;

    Player(){
        this.board = new Board();
    }
    Player(String name){
        this.name = name;
        this.board = new Board();
    }

    protected String getName(){
        return this.name;
    }

    public void attack(Player player2, DataOutputStream out, DataInputStream in){
        for(int i = 0; i < this.board.size(); i++){
            for(Square s: player2.board.getRow(i)){
                s.setOnMouseClicked(e -> {
                    if(count < 5){
                        System.out.println("Set your ships first");
                    }
                    else{
                        System.out.println(this.name + " attacks " + player2.getName() + " at " + e.getTarget());
                        player2.setTurn(true);
                        this.turn = false;

                        //Run other player's turn (using server) (for testing purposes)
                        try {
                            //Send data
                            out.writeUTF(name);
                            out.writeInt(s.getX());
                            out.writeInt(s.getY());

                            //Receive attack info

                        } catch(IOException err) {
                            System.out.println("fatal error");
                        }
                    }
                });

            }
        }
        for(int i = 0; i < this.board.size(); i++){
            for(Square s: this.board.getRow(i)){
                s.setOnMouseClicked(e -> {
                    if(count < 5) {
                        System.out.println(this.name + " ship #" + count + " set at square " + e.getTarget());
                        this.count++;
                    }
                    else {
                        System.out.println("Your ships are set. Go to war!");
                    }
                });
            }
        }

    }

    public void setShips(){
        for(int i = 0; i < 5; i++){
            //set the ships
        }
    }

    public void setName(String name){
        this.name = name;
    }
    public void setTurn(){
        this.turn = !turn;
    }
    public void setTurn(Boolean turn){
        this.turn = turn;
    }

    public Boolean getTurn(){
        return this.turn;
    }
}