package sample.states;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * Created by Nick on 2/26/2016.
 */
public interface StateMachine {
    void changeState(State newState);
    void changeState(State newState, boolean backwards);
    DoubleProperty _getWidth();
    DoubleProperty _getHeight();
    ObservableList<Node> _getChildren();
}
