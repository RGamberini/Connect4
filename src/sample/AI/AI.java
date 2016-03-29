package sample.AI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import sample.Connect4Board;
import sample.InvalidBoardException;
import sample.Player;
import sample.Tile;
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
    }

    private void AITurn(BoardState newVal) {
        if (newVal.turn == tile && !newVal.checkForGameOver()) {
            Task<Point> heuristic = new Minimax(tile, newVal);
            heuristic.setOnSucceeded((event) -> board.set(heuristic.getValue(), tile));
            new Thread(heuristic).start();
        }
    }
}
