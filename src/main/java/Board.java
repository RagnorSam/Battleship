public class Board {
    int size = 10;
    Square[][] board = new Square[size][size];

    Board() {
        boolean prep = true;           //Preparing your own board
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                Square square = new Square(i, k); //changed for testing
                board[i][k] = square;
            }
        }
    }

    public int size() {
        return this.size;
    }

    public Square[] getRow(int row) {
        Square[] temp = new Square[this.size];
        for (int i = 0; i < this.size; i++) {
            temp[i] = board[row][i];
        }
        return temp;
    }
    public Square[] getCol(int col) {
        Square[] temp = new Square[this.size];
        for (int i = 0; i < this.size; i++) {
            temp[i] = board[i][col];
        }
        return temp;
    }
}
