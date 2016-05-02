package sample.AI.MonteCarlo;

import com.sun.istack.internal.Nullable;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nick on 4/26/2016.
 */
public class MonteCarloWorker implements Runnable {
    public BoardState initialState;
    private @Nullable Point nextMove;
    private static Random rng = new Random();
    private Tile tile;
    public HashMap<BoardState, AtomicInteger> plays, wins;

    public synchronized void setNextMove(Point nextMove) {
        this.nextMove = nextMove;
    }

    public MonteCarloWorker(BoardState initialState, Tile tile) {
        this.initialState = initialState;
        this.tile = tile;
        this.plays = new HashMap<>();
        this.wins = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("running");
        while (initialState.winner == Tile.EMPTY && initialState.getAllMoves().length > 0 && !Thread.interrupted()) {
            if (nextMove != null && !isLeaf(initialState)) {
                initialState = initialState.set(nextMove);
                nextMove = null;
            }
            montecarlo(initialState);
        }
        System.out.println("All over!");
        System.out.println(initialState.winner);
    }

    public void montecarlo(BoardState initialState) {
        ArrayList<BoardState> parentStates = new ArrayList<>();
        if (!plays.containsKey(initialState)) plays.put(initialState, new AtomicInteger(0));
        if (!wins.containsKey(initialState)) wins.put(initialState, new AtomicInteger(0));

        BoardState selectedState = initialState;
        parentStates.add(selectedState);
        while (!isLeaf(selectedState) && selectedState.getAllMoves().length > 0) {
            selectedState = select(selectedState);
            parentStates.add(selectedState);
        }
        if (plays.get(selectedState).intValue() > 25)
            expand(selectedState);

        boolean won = simulate(selectedState);
        // Back Propagation
        for (BoardState parent : parentStates) {
            plays.get(parent).incrementAndGet();
            if (won) wins.get(parent).incrementAndGet();
        }
    }

    private boolean simulate(BoardState selectedState) {
        while (selectedState.winner == Tile.EMPTY && selectedState.getAllMoves().length > 0) {
            Point[] allMoves = selectedState.getAllMoves();
            selectedState = selectedState.set(allMoves[rng.nextInt(allMoves.length)]);
        }
        return selectedState.winner == tile;
    }

    private void expand(BoardState selectedState) {
        for (Point move: selectedState.getAllMoves()) {
            BoardState nextState = selectedState.set(move);
            plays.put(nextState, new AtomicInteger(0));
            wins.put(nextState, new AtomicInteger(0));
        }
    }

    private BoardState select(BoardState selectedState) {
        if (selectedState.getAllMoves().length == 0) return selectedState;
        Point[] allMoves = selectedState.getAllMoves();
        return selectedState.set(allMoves[rng.nextInt(allMoves.length)]);
    }

    private boolean isLeaf(BoardState selectedState) {
        for (Point move : selectedState.getAllMoves()) {
            if (!plays.containsKey(selectedState.set(move))) return true;
        }
        return false;
    }

    public Point getBest() {
        Point bestMove = null;
        double bestScore = -1;
        for (Point nextMove : initialState.getAllMoves()) {
            BoardState nextState = initialState.set(nextMove);
            double _wins = wins.get(nextState).doubleValue(), _plays = plays.get(nextState).doubleValue();
            System.out.println("Plays: " + _plays + " Wins: " + _wins);
            double score = _wins / _plays + 1;
            if (score > bestScore || bestMove == null) {
                bestMove = nextMove;
                bestScore = score;
            }
        }
        return bestMove;
    }
}
