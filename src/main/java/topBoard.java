//Board the player uses to attack the other player
public class topBoard extends Board {
    public topBoard() {
        super();
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                Square temp = board[i][j];
                temp.setOnMouseClicked(e -> {
                    System.out.println("Attacking (" + temp.getX()  + "," + temp.getY() + ")");
                });
                //board[i][j].setDisable(true);
            }
        }
    }
    @Override
    protected void move() {

    }
}
