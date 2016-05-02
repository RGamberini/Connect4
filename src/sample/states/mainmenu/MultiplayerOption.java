package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Created by Nick on 5/1/2016.
 */
public class MultiplayerOption extends MainMenuOption {
    public MultiplayerOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/multiplayer/multiplayer_HEADER.png");

        StackPane connectHeader = new StackPane(new ImageView("mainmenu/multiplayer/connect.png"));
        connectHeader.setAlignment(Pos.CENTER);
        content.getChildren().add(connectHeader);

        JFXTextField jfxTextField = new JFXTextField();
        jfxTextField.setPromptText("Enter IP");
        jfxTextField.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        content.getChildren().add(jfxTextField);


        StackPane hostHeader = new StackPane(new ImageView("mainmenu/multiplayer/host.png"));
        hostHeader.setAlignment(Pos.CENTER);
        content.getChildren().add(hostHeader);


        JFXButton mainmenu = new JFXButton("", new ImageView("gamedisplay/wonmenu/mainmenu.png"));
        mainmenu.setOnMouseClicked((event) -> main.changeState(new MainOptions(main, header, contentStack), true));
        mainmenu.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        mainmenu.getStyleClass().add("main-menu-button");

        VBox buttons = new VBox(mainmenu);
        buttons.spacingProperty().bind(content.spacingProperty());
        VBox.setMargin(buttons, new Insets(0, 0, 24, 0));
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        content.getChildren().add(buttons);
    }
}
