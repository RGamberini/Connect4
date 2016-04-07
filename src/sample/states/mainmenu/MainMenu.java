package sample.states.mainmenu;

import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import sample.Connect4Board;
import sample.Menu;
import sample.StateDisplay;
import sample.states.GameDisplayState;
import sample.states.State;
import sample.states.StateMachine;

/**
 * Kind of a funky class this represents the MainMenu with nothing in it, it's also a StateMachine to make each
 * option more encapsulated.
 */
public class MainMenu extends Menu implements StateMachine {
    State currentState;
    final StateDisplay main;
    public MainMenu(StateDisplay main) {
        super();
        this.main = main;

        headerBG.prefWidthProperty().bind(main._width);
        headerBG.minHeightProperty().bind(Bindings.multiply(.20, main._height));

        this.maxWidthProperty().bind(Bindings.divide(main._width, 1.8));
        this.maxHeightProperty().bind(Bindings.divide(main._height, 1.3));
        this.changeState(new MainOptions(this, headerStack, contentStack));
    }

    @Override
    public void changeState(State newState) {
        Transition exitTransition = null;
        if (currentState != null)
            exitTransition = currentState.exit();
        currentState = newState;
        currentState.enter(exitTransition);
    }

    @Override
    public void changeState(State newState, boolean backwards) {
        Transition exitTransition = null;
        if (currentState != null)
            exitTransition = currentState.exit(backwards);
        currentState = newState;
        currentState.enter(exitTransition, backwards);
    }

    public void createNewGame(Connect4Board board) {
        main.changeState(new GameDisplayState(main, board));
    }
}
