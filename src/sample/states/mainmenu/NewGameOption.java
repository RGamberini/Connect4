package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.AI.PlayerType;
import sample.Connect4Board;
import sample.PlayerTypePicker;

import java.util.HashMap;

/**
 * A state of the Main Menu for creating a new game.
 */
public class NewGameOption extends MainMenuOption {

    public NewGameOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/newgame/newgame_HEADER.png");

        String[] playerImageStrings = new String[]{"mainmenu/newgame/playerone.png", "mainmenu/newgame/playertwo.png"};
        PlayerTypePicker[] playerTypePickers = new PlayerTypePicker[playerImageStrings.length];
        VBox pickers = new VBox();
        for (int i = 0; i < playerImageStrings.length; i++) {
            String playerImageString = playerImageStrings[i];
            StackPane playerImage = new StackPane(new ImageView(playerImageString));
            playerImage.setAlignment(Pos.CENTER);

            playerTypePickers[i] = new PlayerTypePicker();
            VBox playerVBox = new VBox(8, playerImage, playerTypePickers[i]);
            VBox.setVgrow(playerVBox, Priority.ALWAYS);
            playerVBox.setAlignment(Pos.CENTER);
            playerVBox.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
            pickers.getChildren().add(playerVBox);
        }

        pickers.spacingProperty().bind(content.spacingProperty());
        pickers.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(pickers, Priority.ALWAYS);
        content.getChildren().add(pickers);


        /**
         * Buttons
         */
        JFXButton confirm = new JFXButton("", new ImageView("mainmenu/newgame/confirm.png"));
        confirm.setOnMouseClicked((event) -> main.createNewGame(new Connect4Board(new PlayerType[]{playerTypePickers[0].getCurrentType(), playerTypePickers[1].getCurrentType()})));
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