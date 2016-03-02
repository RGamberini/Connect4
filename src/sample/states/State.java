package sample.states;

import com.sun.istack.internal.Nullable;
import javafx.animation.Transition;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public abstract class State {
    protected StateMachine main;
    public State (StateMachine main) {
        this.main = main;
    }


    public void enter(@Nullable Transition exitTransition) {

    }

    public void enter(@Nullable Transition exitTransition, boolean backwards) {

    }

    public Transition exit() {
        return null;
    }

    public Transition exit(boolean backwards) {
        return null;
    }
}
