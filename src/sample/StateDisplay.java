package sample;

import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;
import sample.states.mainmenu.MainMenuState;
import sample.states.State;
import sample.states.StateMachine;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class StateDisplay extends StackPane implements StateMachine {
    private State currentState;
    private int size = 250;
    public DoubleProperty _height = new SimpleDoubleProperty(size * 3);
    public DoubleProperty _width = new SimpleDoubleProperty(size * 4);
    public StateDisplay() {
        this.getStyleClass().add("state-display");

        //State state = new GameDisplayState(this, new Connect4Board());
        State state = new MainMenuState(this);
        changeState(state);
    }

    @Override
    public void changeState(State newState) {
        Transition exitTransition = null;
        if (this.currentState != null)
             exitTransition = this.currentState.exit();
        this.currentState = newState;
        this.currentState.enter(exitTransition);
    }

    @Override
    public void changeState(State newState, boolean backwards) {
        Transition exitTransition = null;
        if (this.currentState != null)
            exitTransition = this.currentState.exit();
        this.currentState = newState;
        this.currentState.enter(exitTransition, backwards);
    }
}
