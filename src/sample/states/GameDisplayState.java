package sample.states;

import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import sample.Connect4Board;
import sample.Connect4Display;
import sample.DisplayControls;

/**
 * Created by Nick on 2/25/2016.
 */
public class GameDisplayState extends State {
    private Connect4Display display;
    private StackPane mainStack;

    public GameDisplayState(StateMachine main, Connect4Board model) {
        super(main);
        mainStack = new StackPane();
        StackPane.setAlignment(mainStack, Pos.CENTER);
        mainStack.getStyleClass().add("gamedisplay-vbox");
        display = new Connect4Display(model);
        mainStack.getChildren().add(new DisplayControls(display));
    }

    @Override
    public void enter(Transition exitTransition) {
        //mainStack.setMaxWidth(display.width + (main._getWidth() / 10));
        mainStack.maxWidthProperty().bind(Bindings.add(
                Bindings.divide(main._getWidth(), 10),
                display.width
        ));
        main._getChildren().clear();
        main._getChildren().add(mainStack);
    }

    @Override
    public Transition exit() {
        return null;
    }
}
