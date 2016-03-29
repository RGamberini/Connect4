package sample.AI;

import javafx.concurrent.Task;
import sample.Connect4Board;
import sample.InvalidBoardException;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;

/**
 * Created by Nick on 3/27/2016.
 */
public class Minimax extends Task<Point> {
    private Tile currentPlayer;
    private BoardState board;

    public Minimax(Tile currentPlayer, BoardState board) {
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
        if (depth == 0) {
            return new Move(changed, state.getValue());
        } else {
            if (state.turn == currentPlayer) {
                // Maximizing
                Move bestMove = new Move(null, Integer.MIN_VALUE);
                for (Point move : state.getAllMoves()) {
                    BoardState newState = new BoardState(state, state.getNextTurn());
                    newState.set(move, state.turn);
                    Move toTest = minimax(newState, move, depth - 1);
                    bestMove = max(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            } else {
                // Minimizing
                Move bestMove = new Move(null, Integer.MAX_VALUE);
                for (Point move : state.getAllMoves()) {
                    BoardState newState = new BoardState(state, state.getNextTurn());
                    newState.set(move, state.turn);
                    Move toTest = minimax(newState, move, depth - 1);
                    bestMove = min(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            }
        }
    }
}
