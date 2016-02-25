package sample.states;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToolbar;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import sample.StateDisplay;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class StartMenuState extends StackPane implements State {

    public StartMenuState() {
        Pane mainCard = new Pane();
        mainCard.getStyleClass().add("card");
        mainCard.getStyleClass().add("main-menu");
        this.getChildren().add(mainCard);

        VBox mainVBox = new VBox();
        mainCard.getChildren().add(mainVBox);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.spacingProperty().bind(Bindings.multiply(.05, this.maxHeightProperty()));

        JFXToolbar headerBG = new JFXToolbar();
        headerBG.prefWidthProperty().bind(this.maxWidthProperty());
        headerBG.prefHeightProperty().bind(Bindings.multiply(.25, this.maxHeightProperty()));
        ImageView logo = new ImageView("logo.png");
        StackPane image = new StackPane(logo);
        image.setAlignment(Pos.CENTER);
        mainVBox.getChildren().add(new StackPane(headerBG, image));

        JFXButton button = new JFXButton("", new ImageView("newgame.png"));
        button.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        button.getStyleClass().add("main-menu-button");
        button.setRipplerFill(Paint.valueOf("FFFFFF"));
        mainVBox.getChildren().add(button);

        JFXButton loadgame = new JFXButton("", new ImageView("loadgame.png"));
        loadgame.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        loadgame.getStyleClass().add("main-menu-button");
        loadgame.setRipplerFill(Paint.valueOf("FFFFFF"));
        mainVBox.getChildren().add(loadgame);

        JFXButton options = new JFXButton("", new ImageView("options.png"));
        options.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        options.getStyleClass().add("main-menu-button");
        options.setRipplerFill(Paint.valueOf("FFFFFF"));
        mainVBox.getChildren().add(options);

        JFXButton quit = new JFXButton("", new ImageView("quitgame.png"));
        quit.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        quit.getStyleClass().add("main-menu-button");
        quit.setRipplerFill(Paint.valueOf("FFFFFF"));
        mainVBox.getChildren().add(quit);
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
