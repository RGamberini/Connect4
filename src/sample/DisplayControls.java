package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToolbar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Nick on 2/25/2016.
 */
public class DisplayControls extends StackPane {
    private HashMap<Tile, JFXButton> currentTurn;
    private final String[] playerImages = {"playerone.png", "playertwo.png"};
    private final String[] computerImages = {"computerone.png", "computertwo.png"};
    private JFXButton[] turnImageButtons;
    public DisplayControls(Connect4Display display) {
        BorderPane borderPane = new BorderPane();

        JFXToolbar toolbar = new JFXToolbar();
        VBox tempVBox = new VBox(toolbar);
        borderPane.setTop(tempVBox);
        turnImageButtons = new JFXButton[Connect4Board.turnOrder.size()];

        for (int i = 0; i < Connect4Board.turnOrder.size(); i++) {
            Player player = display.model.getPlayer(Connect4Board.turnOrder.get(i));
            ImageView turnImage;
            if (!(player instanceof AI)) turnImage = new ImageView("gamedisplay/" + playerImages[i]);
            else turnImage = new ImageView("gamedisplay/" + computerImages[i]);

            JFXButton turnImageButton = new JFXButton("", turnImage);
            BorderPane.setAlignment(turnImageButton, Pos.CENTER);
            turnImageButton.setMouseTransparent(true);
            turnImageButtons[i] = turnImageButton;
        }

        JFXButton playerOne = turnImageButtons[0], playerTwo = turnImageButtons[1];
        BorderPane.setMargin(playerOne, new Insets(0, 0, 0, 16));
        toolbar.setLeft(playerOne);

        BorderPane.setMargin(playerTwo, new Insets(0, 16, 0, 0));
        toolbar.setRight(playerTwo);

        currentTurn = new HashMap<>(2);
        currentTurn.put(Tile.PLAYER1, playerOne);
        currentTurn.put(Tile.PLAYER2, playerTwo);
        playerTwo.setDisable(true);
        display.model.currentState.addListener((o, oldVal, newVal) -> {
            currentTurn.forEach(((tile, imageView) -> {
                if (tile == newVal.turn)
                    imageView.setDisable(false);
                else
                    imageView.setDisable(true);
            }));
        });

        ImageView logo = new ImageView("gamedisplay/logo_small.png");
        BorderPane.setMargin(logo, new Insets(0, 16, 0, 0));
        BorderPane.setAlignment(logo, Pos.CENTER);
        toolbar.setCenter(logo);

        ImageView settings = new ImageView("gamedisplay/settings.png");
        JFXToolbar bottomToolbar = new JFXToolbar();
        bottomToolbar.maxHeightProperty().bind(settings.getImage().heightProperty());
        bottomToolbar.setCenter(settings);
        bottomToolbar.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.26), 10, 0.12, -1, -2));
        VBox bottomVBox = new VBox(bottomToolbar);
        borderPane.setBottom(bottomVBox);

        borderPane.setCenter(display);
        this.getChildren().add(borderPane);
    }
}
