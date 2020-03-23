import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Player {
    protected Boolean turn = true;
    String name;

    Player(){}
    Player(String name){
        this.name = name;
    }

    protected String getName(){
        return this.name;
    }

    public void attack(Board board, Player player2){
        //?????
        if(turn) {
            for (int i = 0; i < board.size(); i++) {
                for (Square s : board.getRow(i)) {
                    s.setOnMouseClicked(e -> {
                        System.out.println("attack " + e.getTarget().toString());
                        System.out.println(player2.getTurn());
                        if(s.hasShip()){
                            player2.setTurn(turn);
                            this.turn = !turn;
                        }
                    });
                }
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