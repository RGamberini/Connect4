package sample;

import sample.states.BoardState;

import java.awt.*;
import java.util.ArrayList;

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
            board.set(evaluateBestMove(newVal), tile);
    }


    public Point evaluateBestMove(BoardState state) {
        Point bestMove = state.getTopCell(3);
        int bestValue = 0;
        for (Point point: state.getAllMoves()) {
            BoardState newState = null;
            try {
                newState = new BoardState(state.getState());
            } catch (InvalidBoardException e) {
                e.printStackTrace();
            }
            assert newState != null;
            newState.set(point, tile);
            int newValue = evaluateBoardState(newState);
            if (newValue > bestValue) {
                bestMove = point;
                bestValue = newValue;
            }
        }
        System.out.println("(" + bestMove.x + ", " + bestMove.y + ") has a value of " + bestValue);
        return bestMove;
    }

    private int evaluateBoardState(BoardState state) {
        int myLongestRun = -1;
        int enemyLongestRun = -1;
        for (Tile turn: Connect4Board.turnOrder) {
            for (Point[] run: state.getRuns(turn)) {
                if(turn == tile) {
                    if (myLongestRun < run.length) {
                        myLongestRun = run.length;
                    }
                } else {
                    if(enemyLongestRun < run.length)
                        enemyLongestRun = run.length;
                }
            }
        }
        return myLongestRun;
    }
}
