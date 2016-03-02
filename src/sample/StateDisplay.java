package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import sample.states.mainmenu.MainMenuHeaderAndMachine;
import sample.states.State;
import sample.states.StateMachine;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class StateDisplay extends StackPane implements StateMachine {
    private State currentState;
    private int size = 250;
    public DoubleProperty height = new SimpleDoubleProperty(size * 3);
    public DoubleProperty width = new SimpleDoubleProperty(size * 4);
    public StateDisplay() {
        this.getStyleClass().add("state-display");

        Connect4Board board = new Connect4Board();
        State state = new MainMenuHeaderAndMachine(this);
        changeState(state);
    }

    @Override
    public void changeState(State newState) {
        if (this.currentState != null)
            this.currentState.exit();
        this.currentState = newState;
        this.currentState.enter(null);
    }

    @Override
    public void changeState(State newState, boolean backwards) {
        if (this.currentState != null)
            this.currentState.exit(backwards);
        this.currentState = newState;
        this.currentState.enter(null, backwards);
    }

    @Override
    public DoubleProperty _getWidth() {
        return width;
    }

    @Override
    public DoubleProperty _getHeight() {
        return height;
    }

    @Override
    public ObservableList<Node> _getChildren() {
        return this.getChildren();
    }
}
