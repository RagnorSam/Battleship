public class Ship {
    int length;
    boolean horizontal = false; // when this is false the ship is vertical
    Player player;
    int location;
    Ship(Player player, int length, int location){
        this.player = player;
        this.length = length;
        this.location = location;
    }

}