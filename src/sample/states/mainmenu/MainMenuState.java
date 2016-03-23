package sample.states.mainmenu;

import com.jfoenix.controls.JFXToolbar;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import sample.Animations;
import sample.Connect4Board;
import sample.Main;
import sample.StateDisplay;
import sample.states.GameDisplayState;
import sample.states.State;
import sample.states.StateMachine;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class MainMenuState extends State {
    protected StateDisplay main;
    private MainMenu mainMenu;

    public MainMenuState(StateDisplay main) {
        super(main);
        this.main = main;
        this.mainMenu = new MainMenu(main);
    }

    @Override
    public void enter(Transition exitTransition) {
        Node[] toDelete = main.getChildren().toArray(new Node[main.getChildren().size()]);
        main.getChildren().add(mainMenu);
        if (exitTransition != null) {
            Transition entrance = Animations.sweepInDown(mainMenu, main._height.get());
            entrance = new ParallelTransition(entrance, exitTransition);

            entrance.setOnFinished((value) -> main.getChildren().removeAll(toDelete));
            entrance.play();
        } else main.getChildren().removeAll(toDelete);
    }

    @Override
    public Transition exit() {
        return Animations.sweepOutDown(mainMenu);
    }
}
