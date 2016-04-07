package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.Connect4Board;
import sample.NumberImage;

/**
 * A state of the Main Menu for creating a new game.
 */
public class NewGameOption extends MainMenuOption {

    public NewGameOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/newgame/newgame_HEADER.png");

        /**
         * Player count picker
         */
        StackPane playersImage = new StackPane(new ImageView("mainmenu/newgame/players.png"));
        playersImage.setAlignment(Pos.CENTER_LEFT);

        NumberImage playersNumber = new NumberImage(0);
        playersNumber.getStyleClass().add("number");

        ImageView carrotUp = new ImageView("numbers/carrot_up.png");
        JFXButton upArrow = new JFXButton("", carrotUp);
        upArrow.getStyleClass().add("carrot");

        ImageView carrotDown = new ImageView("numbers/carrot_up.png");
        carrotDown.setScaleY(-1);
        JFXButton downArrow = new JFXButton("", carrotDown);
        downArrow.getStyleClass().add("carrot");

        VBox playersNumberVBox = new VBox(8, upArrow, playersNumber, downArrow);
        playersNumberVBox.setAlignment(Pos.CENTER);
        playersNumberVBox.getStyleClass().add("number");
        HBox playersHBox = new HBox(playersImage, playersNumberVBox);
        HBox.setHgrow(playersImage, Priority.ALWAYS);
        playersHBox.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        playersHBox.setAlignment(Pos.CENTER);

        /**
         * Computer count display
         */

        StackPane computersImage = new StackPane(new ImageView("mainmenu/newgame/computers.png"));
        computersImage.setAlignment(Pos.CENTER_LEFT);
        NumberImage computersNumber = new NumberImage(0);
        VBox computersNumberVBox = new VBox(computersNumber);
        HBox computerHBox = new HBox(computersImage, computersNumberVBox);
        HBox.setHgrow(computersImage, Priority.ALWAYS);
        computerHBox.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        computerHBox.setAlignment(Pos.CENTER);
        computersNumberVBox.prefWidthProperty().bind(playersNumberVBox.widthProperty());
        computersNumberVBox.setAlignment(Pos.CENTER);
        computersNumber.getStyleClass().add("number");

        VBox pickers = new VBox(playersHBox, computerHBox);
        pickers.spacingProperty().bind(content.spacingProperty());
        pickers.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(pickers, Priority.ALWAYS);
        content.getChildren().add(pickers);

        /**
         * Buttons
         */
        JFXButton confirm = new JFXButton("", new ImageView("mainmenu/newgame/confirm.png"));
        confirm.setOnMouseClicked((event) -> main.createNewGame(new Connect4Board(playersNumber.number.get())));
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


        /**
         * Event handlers
         */
        playersNumber.number.addListener((o, oldVal, newVal) -> {
           computersNumber.number.set(2 - newVal.intValue());
            if (newVal.intValue() == 2)
                upArrow.setDisable(true);
            else
                upArrow.setDisable(false);
            if (newVal.intValue() == 0)
                downArrow.setDisable(true);
            else
                downArrow.setDisable(false);
        });

        upArrow.setOnMouseClicked((event) -> {
            if (playersNumber.number.intValue() < 2)
                playersNumber.number.set(playersNumber.number.get() + 1);});

        downArrow.setOnMouseClicked((event) -> {
            if (playersNumber.number.intValue() > 0)
                playersNumber.number.set(playersNumber.number.get() - 1);});
        playersNumber.number.setValue(2);
    }
}
