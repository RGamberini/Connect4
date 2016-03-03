package sample.states;

import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import sample.*;

/**
 * Created by Nick on 2/25/2016.
 */
public class GameDisplayState extends State {
    private Connect4Display display;
    private StackPane mainStack;

    public GameDisplayState(StateMachine main, Connect4Board model) {
        super(main);
        mainStack = new StackPane();
        mainStack.getStyleClass().add("gamedisplay-vbox");
        display = new Connect4Display(model);
        mainStack.getChildren().add(new DisplayControls(display));
    }

    @Override
    public void enter(Transition exitTransition) {
        mainStack.prefHeightProperty().bind(main._getHeight());
        mainStack.maxWidthProperty().bind(Bindings.add(
                Bindings.divide(main._getWidth(), 10),
                display.width
        ));
        main._getChildren().add(mainStack);
        Transition entrance = Animations.sweepInDown(mainStack, mainStack.getPrefHeight());
        if (exitTransition != null)
            entrance = new ParallelTransition(entrance, exitTransition);
        entrance.play();
    }

    @Override
    public Transition exit() {
        return null;
    }
}
