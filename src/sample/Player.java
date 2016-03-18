package sample;

/**
 * Created by Nick on 3/9/2016.
 */
public class Player {
    protected Connect4Board board;
    protected Tile tile;

    public Player(Connect4Board board, Tile tile) {
        this.board = board;
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }
}