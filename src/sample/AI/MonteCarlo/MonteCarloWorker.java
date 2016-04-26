package sample.AI.MonteCarlo;

import com.sun.istack.internal.Nullable;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashSet;

/**
 * Created by Nick on 4/26/2016.
 */
public class MonteCarloWorker implements Runnable {
    MonteCarloNode initialNode;
    private @Nullable
    Point nextMove;

    public synchronized void setNextMove(Point nextMove) {
        this.nextMove = nextMove;
    }

    public MonteCarloWorker(BoardState initialState, Tile tile) {
        this.initialNode = new MonteCarloNode(null, initialState, tile);
    }

    @Override
    public void run() {
        int i = 0;
        DecimalFormat df = new DecimalFormat("##.##");
        System.out.println("running");
        while (initialNode.initialState.winner == Tile.EMPTY && initialNode.initialState.getAllMoves().length > 0 && !Thread.interrupted()) {
            if (nextMove != null && !initialNode.isLeaf()) {
                initialNode = initialNode.get(nextMove);
                nextMove = null;
            }
            montecarlo(initialNode);
            i++;
            if (i > 80000) {
                for (Point move : initialNode.initialState.getAllMoves()) {
                    MonteCarloNode testNode = initialNode.get(move);
                    double testValue = testNode.selectionFunction();
                    System.out.println("(" + move.x + ", " + move.y + ") value: " + df.format(testValue) + " Win/Play " +
                            testNode.wins + ":" + testNode.plays);
                }
                i = 0;
            }
        }
        System.out.println("All over!");
    }

    private void montecarlo(MonteCarloNode initialNode) {
        // Keep track of all the parents
        HashSet<MonteCarloNode> parentNodes = new HashSet<>();
        parentNodes.add(initialNode);
        /**
         * First selection
         */
        MonteCarloNode selectedNode = initialNode;
        assert selectedNode != null;
        while (!selectedNode.isLeaf()) {
            selectedNode = selectedNode.select();
            parentNodes.add(selectedNode);
        }

        /**
         * Expansion
         */
        if (selectedNode.plays > 60) selectedNode.expand();

        /**
         * Simulation
         */
        double won = selectedNode.simulate();
        // Back Propagation
        for (MonteCarloNode parent : parentNodes) {
            parent.plays++;
            parent.wins += won;
        }
    }

    public Point getBest() {
        return initialNode.bestChild().initialPoint;
    }
}
