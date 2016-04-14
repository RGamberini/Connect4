package sample.states;

import com.sun.istack.internal.Nullable;
import javafx.animation.Transition;

/**
 * A state for use in a State Machine, I really went back and forth and whether to use state's and state machines especially
 * because I've never used them before. However I think it was worth it and made certain parts of the project simpler, even
 * though I may have misused them in cases.
 */
public abstract class State {
    protected final StateMachine main;
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
