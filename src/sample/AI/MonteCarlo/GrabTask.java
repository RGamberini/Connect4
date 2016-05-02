package sample.AI.MonteCarlo;

import javafx.concurrent.Task;
import sample.AI.MinimaxAlgo;
import sample.AI.Move;
import sample.InvalidBoardException;
import sample.Tile;

import java.awt.*;

/**
 * Created by Nick on 3/27/2016.
 */
class GrabTask extends Task<Point> {
    private MonteCarloWorker workerAI;
    private Tile currentPlayer;

    public GrabTask(MonteCarloWorker workerAI, Tile currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.workerAI = workerAI;
    }


    @Override
    protected Point call() throws Exception {
        long start = System.currentTimeMillis();
        long deltaTime = System.currentTimeMillis() - start;
        if (deltaTime < 500)
            Thread.sleep(500 - deltaTime);
        return workerAI.getBest();
    }
}


