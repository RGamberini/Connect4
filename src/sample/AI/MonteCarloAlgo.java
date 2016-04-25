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
    private Tile currentPlayer;

    public MonteCarloAlgo(MonteCarloAI.MonteCarloWorker workerAI, Tile currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.workerAI = workerAI;
    }


    @Override
    protected Point call() throws Exception {
        long start = System.currentTimeMillis();
        Point move = minimax(workerAI.initialNode, null, 4).space;
        long deltaTime = System.currentTimeMillis() - start;
        if (deltaTime < 500)
            Thread.sleep(500 - deltaTime);
        return move;
    }

    public Move minimax(MonteCarloNode state, Point changed, int depth) throws InvalidBoardException {
        if (depth == 0 || state.isLeaf() || state.initialState.winner == currentPlayer) {
            return new Move(state.initialPoint, state.selectionFunction());
        } else if (state.initialState.winner != Tile.EMPTY) {
            return new Move(state.initialPoint, Integer.MIN_VALUE);
        }
        else {
            if (state.initialState.turn == currentPlayer) {
                // Maximizing
                Move bestMove = new Move(null, Integer.MIN_VALUE);
                for (MonteCarloNode child: state.children.values()) {
                    Move toTest = minimax(child, child.initialPoint, depth - 1);
                    bestMove = MinimaxAlgo.max(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            } else {
                // Minimizing
                Move bestMove = new Move(null, Integer.MAX_VALUE);
                for (MonteCarloNode child: state.children.values()) {
                    Move toTest = minimax(child, child.initialPoint, depth - 1);
                    bestMove = MinimaxAlgo.min(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            }
        }
    }
}


