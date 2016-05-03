package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import sample.PlayerTypePicker;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Nick on 5/1/2016.
 */
public class MultiplayerOption extends MainMenuOption {
    public MultiplayerOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/multiplayer/multiplayer_HEADER.png");

        PlayerTypePicker playerTypePicker = new PlayerTypePicker();
        HBox playAs = new HBox(8, new ImageView("mainmenu/multiplayer/playas.png"), playerTypePicker);
        playAs.setAlignment(Pos.CENTER);
        content.getChildren().add(playAs);

        StackPane connectHeader = new StackPane(new ImageView("mainmenu/multiplayer/connect.png"));
        connectHeader.setAlignment(Pos.CENTER);

        TextField remoteIP = new TextField();
        remoteIP.setText("localhost");
        remoteIP.setPadding(new Insets(0, 0, 1, 0));
        remoteIP.setAlignment(Pos.CENTER);
        remoteIP.setPromptText("IP");
        remoteIP.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));

        JFXButton connectPlayButton = new JFXButton("", new ImageView("mainmenu/multiplayer/connect.png"));
        connectPlayButton.setOnMouseClicked((event) ->
                main.changeState(
                        new EstablishConnection(main, header, contentStack, playerTypePicker.getCurrentType(), remoteIP.getText())));
        connectPlayButton.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        connectPlayButton.getStyleClass().add("main-menu-button");

        VBox connectVBox = new VBox(remoteIP, connectPlayButton);
        connectVBox.setAlignment(Pos.CENTER);
        connectVBox.spacingProperty().bind(Bindings.divide(content.spacingProperty(), 1.2));
        content.getChildren().add(connectVBox);

        TextField localIP = new TextField();
        localIP.setPadding(new Insets(0, 0, 1, 0));
        localIP.setAlignment(Pos.CENTER);
        localIP.setPromptText("IP");
        localIP.setStyle("-fx-opacity: 1.0;");
        localIP.setDisable(true);
        localIP.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        try {
            localIP.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        JFXButton host = new JFXButton("", new ImageView("mainmenu/multiplayer/host.png"));
        host.setOnMouseClicked((event) ->
                main.changeState(
                        new EstablishConnection(main, header, contentStack, playerTypePicker.getCurrentType(), null)));
        host.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        host.getStyleClass().add("main-menu-button");

        VBox hostVBox = new VBox(localIP, host);
        hostVBox.setAlignment(Pos.CENTER);
        hostVBox.spacingProperty().bind(Bindings.divide(content.spacingProperty(), 1.2));
        content.getChildren().add(hostVBox);

        JFXButton mainmenu = new JFXButton("", new ImageView("gamedisplay/wonmenu/mainmenu.png"));
        mainmenu.setOnMouseClicked((event) -> main.changeState(new MainOptions(main, header, contentStack), true));
        mainmenu.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        mainmenu.getStyleClass().add("main-menu-button");

        VBox buttons = new VBox(mainmenu);
        buttons.spacingProperty().bind(content.spacingProperty());
        VBox.setMargin(buttons, new Insets(0, 0, 24, 0));
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(buttons, Priority.ALWAYS);
        content.getChildren().add(buttons);
    }
}
