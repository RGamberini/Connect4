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
 * Created by Nick on 2/25/2016.
 */
public class GameDisplayState extends State {
    private Connect4Display display;
    private StackPane mainStack;
    private JFXDialog wonDialog;
    protected StateDisplay main;

    public GameDisplayState(StateDisplay main, Connect4Board model) {
        super(main);
        this.main = main;
        mainStack = new StackPane();
        mainStack.getStyleClass().add("gamedisplay-vbox");
        display = new Connect4Display(model);
        mainStack.getChildren().add(new DisplayControls(display));

        wonDialog = new JFXDialog(mainStack, new WonDialog(), JFXDialog.DialogTransition.CENTER);
        model.won.addListener(this::gameWon);
        main.getScene().setOnKeyPressed((event) -> {
            if(event.getCode() == KeyCode.A) {
                model.won.set(true);
            }
        });
    }

    @Override
    public void enter(Transition exitTransition) {
        Node[] toDelete = main.getChildren().toArray(new Node[0]);
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
        wonDialog.close();
        return null;
    }

    @Override
    public Transition exit(boolean backwards) {
        return exit();
    }

    public void gameWon(Observable o, boolean oldVal, boolean newVal) {
        if(newVal) {
            System.out.println("TRIGGERED");
            wonDialog.show();
        }
    }
}
