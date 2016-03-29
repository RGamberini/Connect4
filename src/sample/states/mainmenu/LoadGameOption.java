package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.Connect4Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nick on 3/28/2016.
 */
public class LoadGameOption extends MainMenuOption {

    public LoadGameOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/loadgame/loadgame_HEADER.png");

        StackPane textStack = new StackPane();



        /**
         * Buttons
         */
        JFXButton confirm = new JFXButton("", new ImageView("mainmenu/newgame/confirm.png"));
        //confirm.setOnMouseClicked((event) -> main.createNewGame(new Connect4Board(playersNumber.number.get())));
        confirm.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        confirm.getStyleClass().add("main-menu-button");

        JFXButton cancel = new JFXButton("", new ImageView("mainmenu/newgame/cancel.png"));
        cancel.setOnMouseClicked((event) -> main.changeState(new MainOptions(main, header, contentStack), true));
        cancel.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        cancel.getStyleClass().add("main-menu-button");

        VBox buttons = new VBox(cancel, confirm);
        buttons.spacingProperty().bind(content.spacingProperty());
        VBox.setMargin(buttons, new Insets(0, 0, 24, 0));
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        content.getChildren().add(buttons);
    }
}
