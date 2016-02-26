package sample;

import javafx.scene.layout.StackPane;
import sample.states.GameDisplayState;
import sample.states.StartMenuState;
import sample.states.State;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class StateDisplay extends StackPane {
    private State currentState;
    private int size = 250;
    public int height = size * 3;
    public int width = size * 4;
    public StateDisplay() {
        this.getStyleClass().add("state-display");

        Connect4Board board = new Connect4Board();
        State state = new StartMenuState();
        changeState(state);
    }

    public void changeState(State newState) {
        if (this.currentState != null)
            currentState.exit(this);
        this.currentState = newState;
        newState.enter(this);
    }
}
