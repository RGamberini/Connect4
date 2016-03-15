package sample.states;

import com.sun.istack.internal.Nullable;
import javafx.animation.Transition;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public abstract class State {
    protected StateMachine main;
    public State(StateMachine main) {
        this.main = main;
    }


    public abstract void enter(@Nullable Transition exitTransition);

    public void enter(@Nullable Transition exitTransition, boolean backwards) {
        enter(exitTransition);
    }

    public abstract Transition exit();

    public Transition exit(boolean backwards) {
        return exit();
    }
}
