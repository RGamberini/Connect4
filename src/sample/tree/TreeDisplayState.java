package sample.tree;

import com.sun.istack.internal.Nullable;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import sample.Connect4Display;
import sample.StateDisplay;
import sample.states.State;
import sample.states.StateMachine;

/**
 * Created by Rudy Gamberini on 3/15/2016.
 */
public class TreeDisplayState extends State {
    private TreeDisplay display;
    private StackPane mainStack;
    protected StateDisplay main;

    public TreeDisplayState(StateDisplay main, RecursiveTree model) {
        super(main);
        this.main = main;
        this.mainStack = new StackPane();
        this.display = new TreeDisplay(model);
        mainStack.getChildren().add(display);

        mainStack.prefHeightProperty().bind(main._height);
        mainStack.maxWidthProperty().bind(Bindings.add(
                Bindings.divide(main._width, 10),
                display.width
        ));
    }

    @Override
    public void enter(@Nullable Transition exitTransition) {
        //super.enter(exitTransition);
        main.getChildren().clear();
        main.getChildren().add(display);
    }

    @Override
    public void enter(@Nullable Transition exitTransition, boolean backwards) {
        //super.enter(exitTransition, backwards);
    }

    @Override
    public Transition exit() {
        return null;
    }

    @Override
    public Transition exit(boolean backwards) {
        return exit();
    }
}
