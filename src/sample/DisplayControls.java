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
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * Created by Nick on 2/25/2016.
 */
public class DisplayControls extends StackPane {
    private HashMap<Tile, JFXButton> currentTurn;
    public DisplayControls(Connect4Display display) {
        BorderPane borderPane = new BorderPane();

        JFXToolbar toolbar = new JFXToolbar();
        VBox tempVBox = new VBox(toolbar);
        borderPane.setTop(tempVBox);

        JFXButton playerOne = new JFXButton("", new ImageView("playerone.png"));
        BorderPane.setMargin(playerOne, new Insets(0, 0, 0, 16));
        BorderPane.setAlignment(playerOne, Pos.CENTER);
        playerOne.setMouseTransparent(true);
        toolbar.setLeft(playerOne);

        JFXButton playerTwo = new JFXButton("", new ImageView("playertwo.png"));
        BorderPane.setMargin(playerTwo, new Insets(0, 16, 0, 0));
        BorderPane.setAlignment(playerTwo, Pos.CENTER);
        playerTwo.setMouseTransparent(true);
        toolbar.setRight(playerTwo);

        currentTurn = new HashMap<>(2);
        currentTurn.put(Tile.PLAYER1, playerOne);
        currentTurn.put(Tile.PLAYER2, playerTwo);
        playerTwo.setDisable(true);
        display.model.currentState.addListener((o, oldVal, newVal) -> {
            currentTurn.forEach(((tile, imageView) -> {
                if (tile == newVal.turn)
                    currentTurn.get(tile).setDisable(false);
                else
                    currentTurn.get(tile).setDisable(true);
            }));
        });

        ImageView logo = new ImageView("logo_small.png");
        BorderPane.setMargin(logo, new Insets(0, 16, 0, 0));
        BorderPane.setAlignment(logo, Pos.CENTER);
        toolbar.setCenter(logo);

        ImageView settings = new ImageView("settings.png");
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
