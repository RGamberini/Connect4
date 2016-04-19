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
 * AI Class uses minimax with a depth of 6 by default.
 */
public abstract class AI extends Player {
//    ObjectProperty<Point> nextMove;
    public AI(Connect4Board board, Tile tile) {
        super(board, tile);
        board.currentState.addListener((o, oldVal, newVal) -> {
            this.AITurn(newVal);
        });
        this.AITurn(board.currentState.get());
    }

    protected abstract void AITurn(BoardState newVal);
}
