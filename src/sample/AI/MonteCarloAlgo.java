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
    private MonteCarloAI currentPlayer;
    private BoardState board;

    public MonteCarloAlgo(MonteCarloAI currentPlayer, BoardState board) {
        this.currentPlayer = currentPlayer;
        this.board = board;
    }


    @Override
    protected Point call() throws Exception {
        Point bestMove = null;
        double bestValue = -1;
        for (Point move: board.getAllMoves()) {
            BoardState testState = board.set(move);
            double testValue = /*currentPlayer.workerAI.selectionFunction(testState);*/ 0;
            if (testValue > bestValue) {
                bestValue = testValue;
                bestMove = move;
            }
        }
        assert bestMove != null;
        return bestMove;
    }
}


