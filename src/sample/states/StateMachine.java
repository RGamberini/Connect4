package sample.states;

/**
 * A generic State Machine.
 */
public interface StateMachine {
    void changeState(State newState);
    void changeState(State newState, boolean backwards);
}
