package sample.AI;

import javafx.concurrent.Task;
import sample.Connect4Board;
import sample.InvalidBoardException;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;

/**
 * Plain Ol' Minimax algorithm.
 */
class MinimaxAlgo extends Task<Point> {
    private final Tile currentPlayer;
    private final BoardState board;

    public MinimaxAlgo(Tile currentPlayer, BoardState board) {
        this.currentPlayer = currentPlayer;
        this.board = board;
    }

    @Override
    protected Point call() throws Exception {
        long start = System.currentTimeMillis();
        Point r =  evaluateBestMove(board, 6);
        long deltaTime = System.currentTimeMillis() - start;
        if (deltaTime < 500)
            Thread.sleep(500 - deltaTime);
        return r;
    }

    public Point evaluateBestMove(BoardState state, int depth) {
        try {
            return minimax(state, null, depth).space;
        } catch (InvalidBoardException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Move max(Move o1, Move o2) {
        if (o1.value >= o2.value) return o1;
        else return o2;
    }

    public static Move min(Move o1, Move o2) {
        if (o1.value <= o2.value) return o1;
        else return o2;
    }

    public Move minimax(BoardState state, Point changed, int depth) throws InvalidBoardException {
        if (depth == 0 || state.winner != Tile.EMPTY) {
            return new Move(changed, stateHeuristic(state));
        } else {
            if (state.turn == currentPlayer) {
                // Maximizing
                Move bestMove = new Move(null, Integer.MIN_VALUE);
                for (Point move : state.getAllMoves()) {
                    BoardState newState = state.set(move);
                    Move toTest = minimax(newState, move, depth - 1);
                    bestMove = max(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            } else {
                // Minimizing
                Move bestMove = new Move(null, Integer.MAX_VALUE);
                for (Point move : state.getAllMoves()) {
                    BoardState newState = state.set(move);
                    Move toTest = minimax(newState, move, depth - 1);
                    bestMove = min(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            }
        }
    }

    /**
     * The heuristic function for the state, returns 10^n where n is the length longest run of the current player
     * subtracted from the length of the longest run not owned by the current player.
     */
    public int stateHeuristic(BoardState state) {
        int currentTurnLongestRun = 0;
        int otherLongestRun = 0;
        for (Tile player : Connect4Board.turnOrder) {
            for (Point[] run : state.getRuns(player)) {
                if (player == currentPlayer) {
                    if (currentTurnLongestRun < run.length)
                        currentTurnLongestRun = (int) Math.pow(10, run.length);
                } else {
                    if (run.length > 3)
                        return Integer.MIN_VALUE;
                    if (otherLongestRun < run.length)
                        otherLongestRun = (int) Math.pow(10, run.length);
                }
            }
        }
        return currentTurnLongestRun + (otherLongestRun * -1);
    }
}
