import javafx.application.Platform;

public class Player {
    String name;

    Player(){}
    Player(String name){
        this.name = name;
    }

    protected String getName(){
        return this.name;
    }

    public void attack(Board board){
       for(int i = 0; i < board.size(); i++){
           for(Square s: board.getRow(i)){
               s.setOnMouseClicked(e -> {
                   System.out.println("gr");
               });
           }
       }
    }

    public void setName(String name){
        this.name = name;
    }
}
