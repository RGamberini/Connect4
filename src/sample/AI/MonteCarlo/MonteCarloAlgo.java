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
class MonteCarloAlgo extends Task<Point> {
    private MonteCarloWorker workerAI;
    private Tile currentPlayer;

    public MonteCarloAlgo(MonteCarloWorker workerAI, Tile currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.workerAI = workerAI;
    }


    @Override
    protected Point call() throws Exception {
        long start = System.currentTimeMillis();
        long deltaTime = System.currentTimeMillis() - start;
        if (deltaTime < 500)
            Thread.sleep(500 - deltaTime);
//        Point move = minimax(workerAI.initialNode, null, 4).space;
        System.out.println("Plays: " + workerAI.initialNode.bestChild().plays + " Wins: " + workerAI.initialNode.bestChild().wins);
        if (currentPlayer == Tile.PLAYER1) return workerAI.getBest();
        else return workerAI.initialNode.worstChild().initialPoint;
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


