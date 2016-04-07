package sample.states;

import com.jfoenix.controls.JFXDialog;
import com.sun.istack.internal.Nullable;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import sample.*;

import java.awt.*;

/**
 * The state where the game is playing this state composites the display controls, gridpane and winning and settings dialogs.
 */
public class GameDisplayState extends State {
    private final Connect4Display display;
    private final StackPane mainStack;
    private final JFXDialog wonDialog;
    protected final StateDisplay main;

    public GameDisplayState(StateDisplay main, Connect4Board model) {
        super(main);
        this.main = main;
        mainStack = new StackPane();
        mainStack.getStyleClass().add("gamedisplay-vbox");
        display = new Connect4Display(model);
        mainStack.getChildren().add(new DisplayControls(display, new SettingsDialog(main, display)));

        wonDialog = new JFXDialog(mainStack, new WonDialog(main, model), JFXDialog.DialogTransition.CENTER);
        model.won.addListener(this::gameWon);
    }

    @Override
    public void enter(Transition exitTransition) {
        Node[] toDelete = main.getChildren().toArray(new Node[main.getChildren().size()]);
        mainStack.prefHeightProperty().bind(main._height);
        mainStack.maxWidthProperty().bind(Bindings.add(
                Bindings.divide(main._width, 10),
                display.width
        ));
        main.getChildren().add(mainStack);
        Transition entrance = Animations.sweepInDown(mainStack, mainStack.getPrefHeight());
        if (exitTransition != null)
            entrance = new ParallelTransition(entrance, exitTransition);

        entrance.setOnFinished((value) -> main.getChildren().removeAll(toDelete));
        entrance.play();
    }

    @Override
    public void enter(@Nullable Transition exitTransition, boolean backwards) {
        enter(exitTransition);
    }

    @Override
    public Transition exit() {
        return Animations.sweepOutDown(this.mainStack);
    }

    @Override
    public Transition exit(boolean backwards) {
        return exit();
    }

    public void gameWon(Observable o, boolean oldVal, boolean newVal) {
        if(newVal) wonDialog.show();
    }
}
