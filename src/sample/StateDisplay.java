package sample;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import sample.states.StartMenuState;
import sample.states.State;

import java.util.Collection;
import java.util.PriorityQueue;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class StateDisplay extends StackPane {
    private State currentState;
    private int size = 175;
    public int height = size * 3;
    public int width = size * 4;
    public StateDisplay() {
        this.getStyleClass().add("state-display");

        StartMenuState startMenu = new StartMenuState();
        changeState(startMenu);
    }

    public void changeState(State newState) {
        if (this.currentState != null)
            currentState.exit(this);
        this.currentState = newState;
        newState.enter(this);
    }
}
