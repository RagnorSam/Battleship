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

    public int[] attack(Player player2){
        int[] loc = new int[2];
        for(int i = 0; i < this.board.size(); i++){
            for(Square s: player2.board.getRow(i)){
                s.setOnMouseClicked(e -> {
                    if(count >= 5) {
                        if(!turn){
                            return;
                        }
                        System.out.println(this.name + " attack " + e.getTarget());
                        if(s.hasShip()){
                            System.out.println("HIT!!");
                            s.setStyle("-fx-background-color: red");
                        }
                        player2.setTurn(true);
                        this.turn = false;
                    }
                    else {
                        System.out.println(this.name + " Fix Your Ships!");
                    }
                });
            }
        }
        return loc;
    }

    public void setShips(){
        for(int i = 0; i < this.board.size(); i++){
            for(Square s: this.board.getRow(i)){
                s.setOnMouseClicked(e -> {
                    if(count < 5) {
                        if(s.hasShip()){
                            System.out.println("Already has a ship on it");
                            return;
                        }
                        System.out.println(this.name + " ship #" + count + " set at square " + s.getX() + " " + s.getY());
                        s.hasShip = true;
                        if (count==5){
                            this.turn = true;
                        }
                        this.count++;
                    }
                    else {

                        System.out.println(this.name + " Your ships are set. Go to war!");
                    }
                });
            }
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
