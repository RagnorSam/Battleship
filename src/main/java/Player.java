public class Player {
    protected Boolean turn = false;
    Board board;
    String name;
    int count = 0;
    Ship[] fleet = new Ship[5];

    Player(){
        this.board = new Board();
        FileInputStream file = null;
        fleet[0] = new Ship(2, "Boat2");
        fleet[1] = new Ship(3, "Boat3a");
        fleet[2] = new Ship(3, "Boat3b");
        fleet[3] = new Ship(4, "Boat4");
        fleet[4] = new Ship(5, "Boat5");
    }
    Player(String name){
        this.name = name;
        this.board = new Board();
        FileInputStream file = null;
        fleet[0] = new Ship(2, "Boat2");
        fleet[1] = new Ship(3, "Boat3a");
        fleet[2] = new Ship(3, "Boat3b");
        fleet[3] = new Ship(4, "Boat4");
        fleet[4] = new Ship(5, "Boat5");
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

    public void setShips(VBox pane, ImageView[] ships){
        for(ImageView s:ships){
            s.setOnMouseDragged(e -> {
                if(count < 5) {
                    s.setTranslateX(e.getX() + s.getTranslateX());
                    s.setTranslateY(e.getY() + s.getTranslateY());
                    e.consume();
                    System.out.println(s.getTranslateX()/35);
                }
            });
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
