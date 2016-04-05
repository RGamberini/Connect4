package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import sample.ColorSetPicker;
import sample.Tile;

import java.util.HashMap;

/**
 * Created by Nick on 4/2/2016.
 */
public class OptionsOption extends MainMenuOption {
    private ScrollPane scrollPane;
    private VBox scrollContent;

    public OptionsOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/options/options_HEADER.png");

        scrollPane = new ScrollPane();
        scrollPane.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.15));
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        this.content.getChildren().add(scrollPane);

        scrollContent = new VBox();
        debug();

        StackPane tileOne = new StackPane(new ImageView("mainmenu/options/tileone.png"));
        tileOne.setAlignment(Pos.CENTER);
        scrollContent.getChildren().add(tileOne);
        scrollContent.getChildren().add(new ColorSetPicker(scrollPane, Tile.PLAYER1));

        StackPane tileTwo = new StackPane(new ImageView("mainmenu/options/tiletwo.png"));
        tileTwo.setAlignment(Pos.CENTER);
        tileTwo.setPadding(new Insets(24, 0, 0, 0));
        scrollContent.getChildren().add(tileTwo);
        scrollContent.getChildren().add(new ColorSetPicker(scrollPane, Tile.PLAYER2));

        scrollContent.setPadding(new Insets(0, 0, 24, 0));
        scrollPane.setContent(scrollContent);

        JFXButton confirm = new JFXButton("", new ImageView("mainmenu/newgame/confirm.png"));
        confirm.setOnMouseClicked((event) -> main.changeState(new MainOptions(main, header, contentStack), true));
        confirm.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        confirm.getStyleClass().add("main-menu-button");

        VBox buttons = new VBox(confirm);
        buttons.spacingProperty().bind(content.spacingProperty());
        VBox.setMargin(buttons, new Insets(0, 0, 24, 0));
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        content.getChildren().add(buttons);
    }

    @Override
    public void enter(@Nullable Transition exitTransition) {
        super.enter(exitTransition);
    }

    @Override
    public void enter(@Nullable Transition exitTransition, boolean backwards) {
        super.enter(exitTransition, backwards);
    }

    public void debug() {
        // Something's very broken with how pane's calculate size so this is necessary
        scrollContent.parentProperty().addListener((o, oldVal, newVal) -> {
            StackPane test = ((StackPane) newVal);
            test.layoutBoundsProperty().addListener(((observable, oldValue, newValue) -> {
                scrollContent.setMinWidth(newValue.getWidth());
            }));
        });
    }
}
