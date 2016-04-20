package sample.AI;

import sample.Connect4Board;
import sample.Player;
import sample.Tile;
import sample.states.BoardState;

/**
 * AI Class uses minimax with a depth of 6 by default.
 */
public abstract class AI extends Player {
//    ObjectProperty<Point> nextMove;
    public AI(Connect4Board board, Tile tile) {
        super(board, tile);
        board.currentState.addListener((o, oldVal, newVal) -> {
            this.AITurn(oldVal, newVal);
        });
        this.AITurn(board.currentState.get(), board.currentState.get());
    }

    protected abstract void AITurn(BoardState oldVal, BoardState newVal);
}
