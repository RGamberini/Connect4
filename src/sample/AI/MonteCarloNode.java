package sample.AI;

import com.sun.istack.internal.Nullable;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Nick on 4/19/2016.
 */
public class MonteCarloNode {
    public @Nullable Point initialPoint;
    public BoardState initialState;
    public HashMap<Point, MonteCarloNode> children;
    private Tile tile;
    private static Random rng = new Random();
    public double wins, plays;

    public MonteCarloNode(@Nullable Point initialPoint, BoardState initialState, Tile tile) {
        this.initialPoint = initialPoint;
        this.initialState = initialState;
        this.tile = tile;

        wins = 0;
        plays = 0;
    }

    public double selectionFunction() {
        double _plays = plays + 1;
        double firstHalf = (wins / _plays), secondHalf = Math.sqrt((2 * Math.log(_plays)) / _plays);
        return Double.max(firstHalf + secondHalf, firstHalf - secondHalf);
    }

    public MonteCarloNode select() {
        assert !this.isLeaf();
        if (this.children.size() == 0) return this;

//        if (rng.nextInt(10) < 5) {
            Object[] values = children.values().toArray();
            return (MonteCarloNode) values[rng.nextInt(values.length)];
//        }
    }

    public MonteCarloNode bestChild() {
        double bestValue = -1;
        MonteCarloNode bestNode = null;
        for (MonteCarloNode child : this.children.values()) {
            double testValue = child.selectionFunction();
            if (testValue > bestValue) {
                bestValue = testValue;
                bestNode = child;
            }
        }
        return bestNode;
    }

    public MonteCarloNode worstChild() {
        double worstValue = Double.MAX_VALUE;
        MonteCarloNode worstNode = null;
        for (MonteCarloNode child : this.children.values()) {
            double testValue = child.selectionFunction();
            if (testValue < worstValue) {
                worstValue = testValue;
                worstNode = child;
            }
        }
        return worstNode;
    }


    public MonteCarloNode get(Point move) {
        if (isLeaf()) System.err.println("TRYING TO READ CHILDREN OF A LEAF");
        return children.get(move);
    }

    public void expand() {
        // Interesting enough this plays > 10 stops a huge memory leak from happening
        if (initialState.getAllMoves().length != 0) {
            children = new HashMap<>();
            for (Point move : initialState.getAllMoves()) {
                children.put(move, new MonteCarloNode(move, initialState.set(move), tile));
            }
        }
    }

    public boolean isLeaf() {
        return children == null;
    }

    public double simulate() {
        BoardState currentState = initialState;
        Point[] allMoves = currentState.getAllMoves();
        while (currentState.winner == Tile.EMPTY && allMoves.length > 0) {
            currentState = currentState.set(allMoves[rng.nextInt(allMoves.length)]);
            allMoves = currentState.getAllMoves();
        }
        if (currentState.winner == tile) return 1;
        else if (currentState.winner == Tile.EMPTY) return 1;
        else return 0;
    }
}
