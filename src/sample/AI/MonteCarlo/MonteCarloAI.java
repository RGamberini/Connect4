package sample.AI.MonteCarlo;

import com.sun.istack.internal.Nullable;
import sample.AI.AI;
import sample.Connect4Board;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashSet;

/**
 * Created by Nick on 4/14/2016.
 */
public class MonteCarloAI extends AI {
    public MonteCarloWorker workerAI;
    private Thread monteCarloThread;

    public MonteCarloAI(Connect4Board board, Tile tile) {
        super(board, tile);
        workerAI = new MonteCarloWorker(board.currentState.get());
        monteCarloThread = new Thread(workerAI);
        monteCarloThread.setDaemon(true);
        monteCarloThread.start();
    }

    @Override
    protected void AITurn(BoardState oldVal, BoardState newVal) {
        if (workerAI != null) workerAI.setNextMove(getMove(oldVal, newVal));
        if (newVal.turn == tile && newVal.winner == Tile.EMPTY && newVal.getAllMoves().length > 0) {
            MonteCarloAlgo grabTask = new MonteCarloAlgo(workerAI, tile);
            grabTask.setOnSucceeded((event) -> board.set(grabTask.getValue()));
            new Thread(grabTask).start();
        }
    }

    private @Nullable Point getMove(BoardState oldVal, BoardState newVal) {
        if (oldVal != null)
            for (Point move: oldVal.getAllMoves())
                if (oldVal.get(move) != newVal.get(move))
                    return move;
        return null;
    }
}
