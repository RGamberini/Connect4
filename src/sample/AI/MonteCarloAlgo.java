package sample.AI;

import javafx.concurrent.Task;
import sample.Connect4Board;
import sample.InvalidBoardException;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nick on 3/27/2016.
 */
class MonteCarloAlgo extends Task<Point> {
    private MonteCarloAI.MonteCarloWorker workerAI;

    public MonteCarloAlgo(MonteCarloAI.MonteCarloWorker workerAI) {
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


