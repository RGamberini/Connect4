package sample.AI.MonteCarlo;

import com.sun.istack.internal.Nullable;
import sample.AI.AI;
import sample.AI.PlayerType;
import sample.Connect4Board;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;

/**
 * Created by Nick on 4/14/2016.
 */
public class MonteCarloAI extends AI {
    public MonteCarloWorker workerAI;

    public MonteCarloAI(Connect4Board board, Tile tile) {
        super(board, tile);
        if (workerAI == null)
            initializeWorker();
    }

    private void initializeWorker() {
        workerAI = new MonteCarloWorker(board.currentState.get(), tile);
        Thread monteCarloThread = new Thread(workerAI);
        monteCarloThread.setDaemon(true);
        monteCarloThread.start();
    }

    @Override
    protected void AITurn(BoardState oldVal, BoardState newVal) {
        if (workerAI != null) workerAI.setNextMove(getMove(oldVal, newVal));
        if (newVal.turn == tile && newVal.winner == Tile.EMPTY && newVal.getAllMoves().length > 0) {
            if (workerAI == null) initializeWorker();

            GrabTask grabTask = new GrabTask(workerAI, tile);
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

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.MONTECARLO;
    }
}
