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
public class Minimax extends Task<Point> {
    private final Tile currentPlayer;
    private final BoardState board;

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
        if (depth == 0 || state.checkForGameOver()) {
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> c1b3f335b680e1a141ea9bea4867f4667128e58a

    public Move alphaBeta(BoardState state, Point changed, int depth, int alpha, int beta) throws InvalidBoardException {
        if (depth == 0 || state.checkForGameOver()) {
            return new Move(changed, state.getValue());
        } else {
            if (state.turn == currentPlayer) {
                // Maximizing
                Move bestMove = new Move(null, Integer.MIN_VALUE);
                for (Point move : state.getAllMoves()) {
                    BoardState newState = new BoardState(state, state.getNextTurn());
                    newState.set(move, state.turn);
                    Move toTest = alphaBeta(newState, move, depth - 1, alpha, beta);
                    bestMove = max(toTest, bestMove);
                    alpha = Integer.max(alpha, bestMove.value);
                    bestMove.value = alpha;
                    if (alpha >= beta) break;
                }
                if (changed != null) return new Move(changed, alpha);
                else return bestMove;
            } else {
                // Minimizing
                Move bestMove = new Move(null, Integer.MAX_VALUE);
                for (Point move : state.getAllMoves()) {
                    BoardState newState = new BoardState(state, state.getNextTurn());
                    newState.set(move, state.turn);
                    Move toTest = alphaBeta(newState, move, depth - 1, alpha, beta);
                    bestMove = min(toTest, bestMove);
                    beta = Integer.min(beta, bestMove.value);
                    bestMove.value = beta;
                    if (alpha >= beta) break;
                }
                if (changed != null) return new Move(changed, beta);
                else return bestMove;
            }
        }
    }

    public Move negamax(BoardState state, Point changed, int depth) throws InvalidBoardException {
        if (depth == 0 || state.checkForGameOver()) {
            return new Move(changed, state.getValue() * (state.turn == currentPlayer? 1 : -1));
        } else {
            Move bestMove = new Move(null, Integer.MIN_VALUE);
            for (Point move: state.getAllMoves()) {
                BoardState newState = new BoardState(state, state.getNextTurn());
                newState.set(move, state.turn);
                Move toTest = negamax(newState, move, depth - 1);
                toTest.value *= -1;

                bestMove = max(bestMove, toTest);
            }
            if (changed != null) return new Move(changed, bestMove.value);
            else return bestMove;
        }
    }
<<<<<<< HEAD
=======
>>>>>>> 08b236bd971be659aa40f931c62c5bccd2adc9c9
=======
>>>>>>> d1bfb625731bb50ad5d52f8c83b8232fc54875ac
>>>>>>> c1b3f335b680e1a141ea9bea4867f4667128e58a
}
