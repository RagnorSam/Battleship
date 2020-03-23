import javafx.scene.layout.GridPane;

public abstract class Board{
    Square[][] board = new Square[10][10];
    Board(){
        boolean prep = true;           //Preparing your own board
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++) {
                Square square = new Square(i,k);
                board[i][k] = square;
            }
        }
    }
    abstract protected void move();
}

