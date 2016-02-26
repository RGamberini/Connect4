package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import sample.states.BoardState;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class Connect4Board {
    public static final int COLUMNS = 7, ROWS = 6;
    public ObjectProperty<BoardState> currentState;
    public Connect4Board() {
        currentState = new SimpleObjectProperty<>(new BoardState());
    }
}
