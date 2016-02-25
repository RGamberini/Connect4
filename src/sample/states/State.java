package sample.states;

import javafx.scene.Node;
import sample.StateDisplay;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public interface State {
    void enter(StateDisplay main);

    void exit(StateDisplay main);
}
