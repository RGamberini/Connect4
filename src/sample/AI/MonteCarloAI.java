package sample.AI;

import com.sun.istack.internal.Nullable;
import sample.Connect4Board;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashSet;

/**
 * Created by Nick on 4/14/2016.
 */
public class MonteCarloAI extends AI {
    public MonteCarloWorker workerAI;
    private Thread monteCarloThread;

    public MonteCarloAI(Connect4Board board, Tile tile) {
        super(board, tile);
        workerAI = new MonteCarloWorker(board.currentState.get());
        monteCarloThread = new Thread(workerAI);
        monteCarloThread.setDaemon(true);
        monteCarloThread.start();
    }

    @Override
    protected void AITurn(BoardState oldVal, BoardState newVal) {
        if (workerAI != null) workerAI.setNextMove(getMove(oldVal, newVal));
        if (newVal.turn == tile && newVal.winner == Tile.EMPTY && newVal.getAllMoves().length > 0) {
            MonteCarloAlgo grabTask = new MonteCarloAlgo(workerAI, tile);
            grabTask.setOnSucceeded((event) -> board.set(grabTask.getValue()));
            new Thread(grabTask).start();
        }
    }

    private @Nullable Point getMove(BoardState oldVal, BoardState newVal) {
        if (oldVal != null)
            for (Point move: oldVal.getAllMoves())
                if (oldVal.get(move) != newVal.get(move))
                    return move;
        return null;
    }
    class MonteCarloWorker implements Runnable {
        MonteCarloNode initialNode;
        private @Nullable Point nextMove;

        public synchronized void setNextMove(Point nextMove) {
            this.nextMove = nextMove;
        }

        public MonteCarloWorker(BoardState initialState) {
            this.initialNode = new MonteCarloNode(null, initialState, tile);
        }

        @Override
        public void run() {
            int i = 0;
            DecimalFormat df = new DecimalFormat("##.##");
            System.out.println("running");
            while (!board.won.get() && !Thread.interrupted()) {
                if (nextMove != null && !initialNode.isLeaf()) {
                    initialNode = initialNode.get(nextMove);
                    nextMove = null;
                }
                montecarlo(initialNode);
                i++;
                if (i > 80000) {
                    for (Point move : board.getAllMoves()) {
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
            selectedNode.expand();

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
    }
}
