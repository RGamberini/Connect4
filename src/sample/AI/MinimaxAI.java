package sample.AI;

import javafx.concurrent.Task;
import sample.Connect4Board;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;

/**
 * Created by Nick on 4/14/2016.
 */
public class MinimaxAI extends AI {
    public final AIType aiType = AIType.MINIMAX;
    public MinimaxAI(Connect4Board board, Tile tile) {
        super(board, tile);
    }

    @Override
    protected void AITurn(BoardState oldVal, BoardState newVal) {
        if (newVal.turn == tile && newVal.winner == Tile.EMPTY && newVal.getAllMoves().length > 0) {
            Task<Point> heuristic = new MinimaxAlgo(tile, newVal);
            heuristic.setOnSucceeded((event) -> board.set(heuristic.getValue()));
            new Thread(heuristic).start();
        }
    }

    @Override
    public AIType getAIType() {
        return AIType.MINIMAX;
    }
}
