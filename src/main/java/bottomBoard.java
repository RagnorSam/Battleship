//Board for the player to place their ships
public class bottomBoard extends Board {
    public bottomBoard() {
        super();
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                Square temp = board[i][j];
                temp.setOnMouseClicked(e -> {
                    System.out.println("Placing ship at (" + temp.getX()  + "," + temp.getY() + ")");
                });
                //board[i][j].setDisable(true);
            }
        }
    }
    @Override
    protected void move() {

    }
}
