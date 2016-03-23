package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import sample.states.BoardState;
import sample.states.State;

import java.awt.*;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Rudy Gamberini on 3/15/2016.
 */
public class AI extends Player {
//    ObjectProperty<Point> nextMove;
    public AI(Connect4Board board, Tile tile) {
        super(board, tile);
        board.currentState.addListener((o, oldVal, newVal) -> {
            AITurn(newVal);
        });
        AITurn(board.currentState.get());
//        nextMove = new SimpleObjectProperty<>();
//        nextMove.addListener((o, oldVal, newVal) -> board.set(newVal, tile));
    }

    private void AITurn(BoardState newVal) {
        if (newVal.turn == tile && !newVal.checkForGameOver()) {
            Task<Point> heuristic = new Task<Point>() {
                @Override
                protected Point call() throws Exception {
                    long start = System.currentTimeMillis();
                    Point r =  evaluateBestMove(newVal, 6);
                    long deltaTime = System.currentTimeMillis() - start;
                    if (deltaTime < 500)
                        Thread.sleep(500 - deltaTime);
                    return r;
                }
            };
            heuristic.setOnSucceeded((event) -> board.set(heuristic.getValue(), tile));
            new Thread(heuristic).start();
        }
    }

    public class Move {
        public int value;
        public Point space;

        public Move(Point space, int value) {
            this.space = space;
            this.value = value;
        }
    }

    private Point evaluateBestMove(BoardState state, int depth) {
        try {
            return miniMax(state, null, depth).space;
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

    public Move miniMax(BoardState state, Point changed, int depth) throws InvalidBoardException {
        if (depth == 0) {
            return new Move(changed, state.getValue());
        } else {
            if (state.turn == tile) {
                // Maximizing
                Move bestMove = new Move(null, Integer.MIN_VALUE);
                for (Point move: state.getAllMoves()) {
                    BoardState newState = new BoardState(state, state.getNextTurn());
                    newState.set(move, state.turn);
                    Move toTest = miniMax(newState, move, depth - 1);
                    bestMove = max(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            }
            else {
                // Minimizing
                Move bestMove = new Move(null, Integer.MAX_VALUE);
                for (Point move: state.getAllMoves()) {
                    BoardState newState = new BoardState(state, state.getNextTurn());
                    newState.set(move, state.turn);
                    Move toTest = miniMax(newState, move, depth - 1);
                    bestMove = min(toTest, bestMove);
                }
                if (changed != null) return new Move(changed, bestMove.value);
                else return bestMove;
            }
        }
    }
}
