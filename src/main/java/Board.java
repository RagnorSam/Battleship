import javafx.scene.layout.GridPane;

public class Board extends GridPane {
    Board(){
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++) {
                this.add(new Square(), i, k);
            }
        }
    }
}
