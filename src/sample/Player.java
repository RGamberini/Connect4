package sample;

import sample.AI.AIType;

/**
 * Obviously a Player who is not an AI.
 */
public class Player {
    protected final Connect4Board board;
    protected final Tile tile;

    public Player(Connect4Board board, Tile tile) {
        this.board = board;
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public AIType getAIType() { return AIType.HUMAN; }
}