import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

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

    public void attack(Player player2){
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
