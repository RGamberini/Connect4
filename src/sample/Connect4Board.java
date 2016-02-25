package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import sample.states.BoardState;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class Connect4Board {
    public ObjectProperty<BoardState> currentState;
    public Connect4Board() {
        currentState = new SimpleObjectProperty<>(new BoardState());
    }
}
