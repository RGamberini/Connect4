package sample.states;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.Connect4Board;
import sample.Connect4Display;
import sample.DisplayControls;
import sample.StateDisplay;

/**
 * Created by Nick on 2/25/2016.
 */
public class GameDisplayState extends StackPane implements State {
    private Connect4Display display;

    public GameDisplayState(Connect4Board model) {
        StackPane.setAlignment(this, Pos.CENTER);
        this.getStyleClass().add("gamedisplay-vbox");
        display = new Connect4Display(model);
        this.getChildren().add(new DisplayControls(display));
    }

    @Override
    public void enter(StateDisplay main) {
        this.setMaxWidth(display.width + (main.width / 10));
        main.getChildren().clear();
        main.getChildren().add(this);
    }

    @Override
    public void exit(StateDisplay main) {

    }
}
