package sample.AI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import sample.Connect4Board;
import sample.InvalidBoardException;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nick on 4/14/2016.
 */
public class MonteCarloAI extends AI {
    public MonteCarloWorker workerAI;
    public ConcurrentHashMap<Tile[][], AtomicInteger>
            plays = new ConcurrentHashMap<>(),
            wins = new ConcurrentHashMap<>();

    public MonteCarloAI(Connect4Board board, Tile tile) {
        super(board, tile);
        workerAI = new MonteCarloWorker(board.currentState.get());
        Thread thread = new Thread(workerAI);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void AITurn(BoardState newVal) {
        if (newVal.turn == tile && newVal.winner != Tile.EMPTY) {
            Task<Point> grabTask = new MonteCarloAlgo(this, board.currentState.get());
            grabTask.setOnSucceeded((event) -> board.set(grabTask.getValue()));
            new Thread(grabTask).start();
        }
    }

//    class MonteCarloWorker implements Runnable {
//        @Override
//        public void run() {
//            int i = 0;
//            DecimalFormat df = new DecimalFormat("##.##");
//            System.out.println("running");
//            while (!board.won.get() && !Thread.interrupted()) {
//                montecarlo(board.currentState.get());
//                i++;
//                if (i > 2000) {
//                    try {
//                        for (Point move: board.getAllMoves()) {
//                            BoardState testState = new BoardState(board.currentState.get(), board.getNextTurn());
//                            testState.set(move, board.currentState.get().turn);
//                            double testValue = selectionFunction(testState);
//                            System.out.println("(" + move.x + ", " + move.y + ") value: " + df.format(testValue) + " Win/Play " +
//                            wins.get(testState.getState()) + ":" + plays.get(testState.getState()));
//                        }
//                    } catch (InvalidBoardException e) {
//                        e.printStackTrace();
//                    }
//                    i = 0;
//                }
//            }
//            System.out.println("All over!");
//        }
//
//        private void montecarlo(BoardState initialState) {
//            // Keep track of all the parents
//            HashSet<BoardState> parentStates = new HashSet<>();
//            parentStates.add(initialState);
//            /**
//             * First selection
//             */
//            BoardState selectedState = initialState;
//            boolean isLeaf = !plays.containsKey(selectedState.getState());
//            while (!isLeaf) {
//                plays.get(selectedState.getState()).incrementAndGet();
//                selectedState = selection(selectedState);
//                parentStates.add(selectedState);
//                isLeaf = !plays.containsKey(selectedState.getState());
//            }
//
//            /**
//             * Expansion
//             */
//            plays.put(selectedState.getState(), new AtomicInteger(1));
//            wins.put(selectedState.getState(), new AtomicInteger(0));
//
//            /**
//             * Simulation
//             */
//            if (simulation(selectedState)) {
//                // Back Propagation
//                for (BoardState parent : parentStates) {
//                    System.out.println(parent.getState() == selectedState.getState());
//                    System.out.print(wins.get(parent.getState()));
//                    wins.get(parent.getState()).incrementAndGet();
//                }
//            }
//        }
//
//        private boolean simulation(BoardState initialState) {
//            try {
//                Point[] allMoves = initialState.getAllMoves();
//                if (allMoves.length < 1) return false;
//                Random rng = new Random();
//                BoardState nextState = new BoardState(initialState, initialState.getNextTurn());
//                nextState.set(allMoves[rng.nextInt(allMoves.length)], initialState.turn);
//
//                if (nextState.checkForGameOver())
//                    return nextState.turn == tile;
//                else {
//                    return simulation(nextState);
//                }
//            } catch (InvalidBoardException e) {
//                e.printStackTrace();
//            }
//            System.out.println("reached default end");
//            return false;
//        }
//
////    private Point run_simulation(BoardState state) throws InvalidBoardException {
////        Point[] allMoves = state.getAllMoves();
////        ShuffleArray(allMoves);
////        Set<BoardState> visitedStates = new HashSet<>();
////
////        boolean expand = true;
////        for (Point play: allMoves) {
////            BoardState newState = new BoardState(state, state.getNextTurn());
////            newState.set(play, state.turn);
////
////            if(expand && !plays.containsKey(newState)) {
////                expand = false;
////                plays.put(newState, 0);
////                wins.put(newState, 0);
////            }
////            visitedStates.add(newState);
////
////            if(newState.checkForGameOver()) break;
////        }
////
////        for (BoardState visitedState: visitedStates) {
////            if (!plays.containsKey(visitedState)) continue;
////            plays.replace(visitedState, plays.get(visitedState) + 1);
////            if(visitedState.checkForGameOver() && currentPlayer == visitedState.turn)
////                wins.replace(visitedState, wins.get(visitedState) + 1);
////        }
////        return null;
////    }
//
//        public double selectionFunction(BoardState initialState) {
//            double simulationCount = plays.values().stream().mapToDouble(AtomicInteger::doubleValue).sum();
//            //if (simulationCount % 5000 == 0) System.out.println(simulationCount + " simulations");
////            System.out.println(plays.keySet().size() + " different keys");
//            double result;
//            if (plays.containsKey(initialState.getState()) && wins.containsKey(initialState.getState()) && plays.get(initialState.getState()).intValue() > 0) {
//                double winCount = wins.get(initialState.getState()).doubleValue(), playCount = plays.get(initialState.getState()).doubleValue();
//                //double selectionValue = (winCount / playCount) + Math.sqrt(2) * Math.sqrt((Math.log(simulationCount) / playCount));
//                double firstHalf = (winCount / playCount), secondHalf = Math.sqrt((2 * Math.log(playCount)) / playCount);
//                double selectionValue = Double.max(firstHalf + secondHalf, firstHalf - secondHalf);
////                System.out.println("Win/Play: " + winCount + ":" + playCount);
//                result = selectionValue;
//            } else {
//                System.out.println("Could not find");
//                result = 0;
//            }
//            return result;
//        }
//
//        private BoardState selection(BoardState initialState) {
//            Point[] allMoves = initialState.getAllMoves();
//            if (allMoves.length == 0) return initialState;
//
//            double bestValue = -1;
//            BoardState bestState = null;
//            for (Point move : allMoves) {
//                try {
//                    BoardState newState = new BoardState(initialState, initialState.getNextTurn());
//                    newState.set(move, initialState.turn);
//                    if (!plays.containsKey(newState.getState())) {
//                        System.out.println("Plays does not contain: " + "(" + move.x + ", " + move.y + ")");
//                        return newState;
//                    }
//
//                    double testValue = selectionFunction(newState);
//                    if (testValue > bestValue) {
//                        bestValue = testValue;
//                        bestState = newState;
//                    }
//                } catch (InvalidBoardException e) {
//                    e.printStackTrace();
//                    return initialState;
//                }
//            }
//            return bestState;
//        }
//    }
    class MonteCarloWorker implements Runnable {
        TreeNode treeHead;

        public MonteCarloWorker(BoardState initialState) {
            treeHead = new TreeNode(initialState, tile);
        }

        @Override
        public void run() {
            while (true) treeHead.selectAction();
        }
    }
}
