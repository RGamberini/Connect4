package sample;

import sample.states.BoardState;
import sample.states.State;

import java.awt.*;

/**
 * Created by Rudy Gamberini on 3/15/2016.
 */
public class AI extends Player {
    public AI(Connect4Board board, Tile tile) {
        super(board, tile);
        board.currentState.addListener((o, oldVal, newVal) -> {
            AITurn(newVal);
        });
        AITurn(board.currentState.get());
    }

    public void AITurn(BoardState newVal) {
        if (newVal.turn == tile && !board.won.get())
            board.set(board.getTopCell(evaluateBestMove(newVal, 4).x), tile);
    }

    public class Move {
        public int value;
        public Point space;

        public Move(Point space, int value) {
            this.space = space;
            this.value = value;
        }
    }

    public Point evaluateBestMove(BoardState state, int depth) {
        try {
            return miniMax(state, null, depth).space;
        } catch (InvalidBoardException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public Point evaluateBestMove(BoardState state, int depth) {
//        Point bestMove = state.getTopCell(3);
//        int maxMin = 0;
//        for (Point point: state.getAllMoves()) {
//            BoardState newState = null;
//            try {
//                newState = new BoardState(state, state.getNextTurn());
//            } catch (InvalidBoardException e) {
//                e.printStackTrace();
//            }
//            assert newState != null;
//            newState.set(point, tile);
//                int newValue = evaluateBestBoardState(newState);
//                if (newValue > maxMin) {
//                    bestMove = point;
//                    maxMin = newValue;
//                }
//        }
//        //System.out.println("(" + bestMove.x + ", " + bestMove.y + ") has a value of " + bestValue);
//    }

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
