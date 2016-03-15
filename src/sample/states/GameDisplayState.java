package sample.states;

import com.sun.istack.internal.Nullable;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import sample.*;
import sample.tree.TreeDisplayState;

/**
 * Created by Nick on 2/25/2016.
 */
public class GameDisplayState extends State {
    private Connect4Display display;
    private StackPane mainStack;
    protected StateDisplay main;

    public GameDisplayState(StateDisplay main, Connect4Board model) {
        super(main);
        this.main = main;
        mainStack = new StackPane();
        mainStack.getStyleClass().add("gamedisplay-vbox");
        display = new Connect4Display(model);
        mainStack.getChildren().add(new DisplayControls(display));
        model.currentState.addListener(this::updatePlayer);
    }

    @Override
    public void enter(Transition exitTransition) {
        mainStack.prefHeightProperty().bind(main._height);
        mainStack.maxWidthProperty().bind(Bindings.add(
                Bindings.divide(main._width, 10),
                display.width
        ));
        main.getChildren().add(mainStack);
        Transition entrance = Animations.sweepInDown(mainStack, mainStack.getPrefHeight());
        if (exitTransition != null)
            entrance = new ParallelTransition(entrance, exitTransition);
        entrance.play();
    }

    @Override
    public void enter(@Nullable Transition exitTransition, boolean backwards) {
        enter(exitTransition);
    }

    @Override
    public Transition exit() {
        return null;
    }

    @Override
    public Transition exit(boolean backwards) {
        return exit();
    }

    public void updatePlayer(Observable o, BoardState oldVal, BoardState newVal) {
        Player oldPlayer = display.model.getPlayer(oldVal.turn);
        if(oldPlayer instanceof AI) {
            main.changeState(new TreeDisplayState(main, ((AI) oldPlayer).deciscionTree));
        } else {
            System.out.println("That's no moon");
        }
    }

}
