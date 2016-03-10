package sample.states;

/**
 * Created by Nick on 2/26/2016.
 */
public interface StateMachine {
    void changeState(State newState);
    void changeState(State newState, boolean backwards);
}
