package sample;

import sample.states.BoardState;
import sample.tree.RecursiveTree;
import sample.tree.TreeNode;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Nick on 3/9/2016.
 */
public class Player {
    private boolean AI = false;
    private Connect4Board board;
    private Tile tile;
    public Player(Connect4Board board, Tile tile, boolean AI) {
        this.board = board;
        this.tile = tile;
        this.AI = AI;
        if (AI) {
            board.currentState.addListener((o, oldVal, newVal) -> {
                AITurn(newVal);
            });
            AITurn(board.currentState.get());
        }
    }

    public boolean isAI() {
        return AI;
    }

    public Tile getTile() {
        return tile;
    }

    public void AITurn(BoardState newVal) {
        if (newVal.turn == tile && !board.won.get())
            board.set(evaluateBestMove(newVal, 2), tile);
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
            return recurse(state, depth, new RecursiveTree(depth)).space;
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

    //Either returns the best or worst move depending on what turn it is
    private Move recurse(BoardState state, int depth, RecursiveTree tree) throws InvalidBoardException {
        Move bestMove;
        if (state.turn == tile)
            bestMove = new Move(null, Integer.MIN_VALUE);
        else
            bestMove = new Move(null, Integer.MAX_VALUE);

        for (Point move: state.getAllMoves()) {
            BoardState stateToTest = new BoardState(state, state.turn);
            stateToTest.set(move, state.turn);
            //System.out.print("Possible Move: (" + move.x + ", " + move.y + ")\n" + "Depth: " + depth);
//            if (depth == 0) {
//                System.out.println(" Value: " + evaluateBoardState(state));
//            } else System.out.println();
            Move moveToTest;
            if (depth != 0) {
                moveToTest = recurse(new BoardState(stateToTest, stateToTest.getNextTurn()), depth - 1, tree);
            }
            else {
                moveToTest = new Move(move, evaluateBoardState(stateToTest));
            }
            // If it's our turn maximize
            if (tile == state.turn) {
                if (bestMove.value < moveToTest.value) {
                    bestMove = new Move(move, moveToTest.value);
                }
            }
            // Otherwise minimize
            else {
                if(bestMove.value > moveToTest.value) {
                    bestMove = new Move(move, moveToTest.value);
                }
            }
            tree.addNode(new TreeNode(moveToTest, stateToTest, depth));
        }
        System.out.print("At depth: " + depth);
        if (tile == state.turn)  {
            System.out.println(" maximizing");
        } else System.out.println(" minimizing");
        System.out.println("BestMove: (" + bestMove.space.x + ", " + bestMove.space.y + ") has a value " + bestMove.value);
        if (depth == 2) {
            BoardState bestState = new BoardState(state, state.getNextTurn());
            bestState.set(bestMove.space, tile);
            tree.addNode(new TreeNode(bestMove, bestState, depth + 1));
            System.out.println(tree);
        }
        return bestMove;
    }

    private int evaluateBoardState(BoardState state) {
        int currentTurnLongestRun = 0;
        int otherLongestRun = 0;
        for (Tile player : Connect4Board.turnOrder) {
            for (Point[] run : state.getRuns(player)) {
                if (player == tile) {
                    if (currentTurnLongestRun < run.length) {
                        //System.out.println("Found a run for ME: " + run.length);
                        currentTurnLongestRun = (int) Math.pow(10, run.length);
                        //System.out.println("See I set it: " + currentTurnLongestRun);
                    }
                } else {
                    if (otherLongestRun < run.length)
                        otherLongestRun = (int) Math.pow(10, run.length);
                }
            }
        }
//        if (state.turn == tile) {
//            //System.out.println("Post Loop: " + currentTurnLongestRun);
//            return currentTurnLongestRun;
//        }
        return currentTurnLongestRun + (otherLongestRun * -1);
    }
}
