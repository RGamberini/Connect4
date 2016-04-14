package sample.AI;

import javafx.concurrent.Task;
import sample.InvalidBoardException;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.*;

/**
 * Created by Nick on 3/27/2016.
 */
public class MonteCarlo extends Task<Point> {
    private Tile currentPlayer;
    private BoardState board;
    HashMap<BoardState, Integer> plays, wins;

    public MonteCarlo(BoardState board, Tile currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        wins = new HashMap<>();
        plays = new HashMap<>();
    }

    @Override
    protected Point call() throws Exception {
        return null;
    }

    public void montecarlo(BoardState inititalState) {
        /**
         * First selection
         */
        BoardState selectedState = inititalState;
        do {
            selectedState = selection(selectedState);
        } while (plays.containsKey(selectedState));

        /**
         * Expansion
         */
        plays.put(selectedState, 0);
        wins.put(selectedState, 0);



    }

//    private Point get_play() {
//        int maxDepth = 0;
//        Point[] moves = board.getAllMoves();
//
//        if (moves.length < 1) return null;
//        else if (moves.length == 1) return moves[0];
//
//        int games = 0;
//        try {
//            return run_simulation(board);
//        } catch (InvalidBoardException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private boolean simulateAndBackPropagate(BoardState initialState) {
        try {
            Point[] allMoves = initialState.getAllMoves();
            Random rng = new Random();
            BoardState nextState = new BoardState(initialState, initialState.getNextTurn());
            nextState.set(allMoves[rng.nextInt(allMoves.length)], initialState.turn);

            if(nextState.checkForGameOver())
                return nextState.turn == currentPlayer;
            else return simulateAndBackPropagate(nextState);
        } catch (InvalidBoardException e) {
            e.printStackTrace();
        }

    }

//    private Point run_simulation(BoardState state) throws InvalidBoardException {
//        Point[] allMoves = state.getAllMoves();
//        ShuffleArray(allMoves);
//        Set<BoardState> visitedStates = new HashSet<>();
//
//        boolean expand = true;
//        for (Point play: allMoves) {
//            BoardState newState = new BoardState(state, state.getNextTurn());
//            newState.set(play, state.turn);
//
//            if(expand && !plays.containsKey(newState)) {
//                expand = false;
//                plays.put(newState, 0);
//                wins.put(newState, 0);
//            }
//            visitedStates.add(newState);
//
//            if(newState.checkForGameOver()) break;
//        }
//
//        for (BoardState visitedState: visitedStates) {
//            if (!plays.containsKey(visitedState)) continue;
//            plays.replace(visitedState, plays.get(visitedState) + 1);
//            if(visitedState.checkForGameOver() && currentPlayer == visitedState.turn)
//                wins.replace(visitedState, wins.get(visitedState) + 1);
//        }
//        return null;
//    }

    private double selectionFunction(BoardState state) {
        double simulationCount = plays.values().stream().mapToDouble(v -> (double) v).sum();
        double result;
            if (plays.containsKey(state) && wins.containsKey(state) && plays.get(state) != 0) {
                double winCount = wins.get(state), playCount = plays.get(state);
                double selectionValue = (winCount / playCount) + Math.sqrt(2) * Math.sqrt((Math.log(simulationCount)/playCount));
                result = selectionValue;
            } else {
                result = -1;
            }
        return result;
    }

    private BoardState selection(BoardState initialState) {
        Point[] allMoves = initialState.getAllMoves();

        double bestValue = -1;
        BoardState bestState = null;
        for (Point move : allMoves) {
            try {
                BoardState newState = new BoardState(initialState, initialState.getNextTurn());
                newState.set(move, initialState.turn);
                if (!plays.containsKey(newState))
                    return newState;

                double testValue = selectionFunction(newState);
                if (testValue > bestValue) {
                    bestValue = testValue;
                    bestState = newState;
                }
            } catch (InvalidBoardException e) {
                e.printStackTrace();
            }
        }
        return bestState;
    }

    private void ShuffleArray(Point[] array)
    {
        int index;
        Point temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
