package sample;

import com.jfoenix.controls.JFXToolbar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.states.GameDisplayState;

/**
 * Created by Nick on 2/25/2016.
 */
public class DisplayControls extends StackPane {
    public DisplayControls(Connect4Display display) {
        BorderPane borderPane = new BorderPane();

        JFXToolbar toolbar = new JFXToolbar();
        VBox tempVBox = new VBox(toolbar);
        borderPane.setTop(tempVBox);

        ImageView playerOne = new ImageView("playerone.png");
        BorderPane.setMargin(playerOne, new Insets(0, 0, 0, 16));
        BorderPane.setAlignment(playerOne, Pos.CENTER);
        toolbar.setLeft(playerOne);

        ImageView playerTwo = new ImageView("playertwo.png");
        BorderPane.setMargin(playerTwo, new Insets(0, 16, 0, 0));
        BorderPane.setAlignment(playerTwo, Pos.CENTER);
        toolbar.setRight(playerTwo);

        ImageView logo = new ImageView("logo_small.png");
        BorderPane.setMargin(logo, new Insets(0, 16, 0, 0));
        BorderPane.setAlignment(logo, Pos.CENTER);
        toolbar.setCenter(logo);

        borderPane.setCenter(display);
        this.getChildren().add(borderPane);
    }
}
