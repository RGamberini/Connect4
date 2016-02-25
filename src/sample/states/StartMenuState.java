package sample.states;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import sample.StateDisplay;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class StartMenuState extends StackPane implements State {

    public StartMenuState() {
        Pane mainCard = new Pane();
        mainCard.getStyleClass().add("card");
        this.getChildren().add(mainCard);
    }

    @Override
    public void enter(StateDisplay main) {
        this.setMaxSize(main.width / 1.8, main.height / 1.3);
        main.getChildren().clear();
        main.getChildren().add(this);
    }

    @Override
    public void exit(StateDisplay main) {

    }
}
