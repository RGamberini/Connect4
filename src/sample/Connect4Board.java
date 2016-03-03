package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import sample.states.BoardState;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class Connect4Board {
    public static final int COLUMNS = 7, ROWS = 6;
    public static final ArrayList<Tile> turnOrder = new ArrayList<>();
    static {
        turnOrder.add(Tile.PLAYER1);
        turnOrder.add(Tile.PLAYER2);
    }
    public ObjectProperty<BoardState> currentState;
    public Connect4Board() {
        currentState = new SimpleObjectProperty<>(new BoardState());
    }
    public Point getTopCell(int x) { return currentState.get().getTopCell(x);}
    public Tile getCurrentTurn() { return currentState.get().turn;}

    public void set(Point p, Tile owner) {
        BoardState nextState;
        try {
            nextState = new BoardState(currentState.get(), turnOrder.get(
                    (turnOrder.indexOf(owner) + 1) % turnOrder.size()
            ));
        } catch (InvalidBoardException e) {
            e.printStackTrace();
            System.err.println("Something went wrong!");
            return;
        }
        nextState.set(p, owner);
        currentState.set(nextState);
    }

    public Tile get(Point p) {
        return currentState.get().get(p);
    }
}
