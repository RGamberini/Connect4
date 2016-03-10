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


    public Point evaluateBestMove(BoardState newVal) {
        ArrayList<Point[]> runs = newVal.getRuns(tile);
        if (runs.size() == 0)
            return newVal.getTopCell(5);
        runs.sort((p, c) -> p.length - c.length);
        Point[] bestRun = runs.get(0);
        return newVal.getTopCell(bestRun[0].x);
    }
}
