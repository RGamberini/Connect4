package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import sample.AI.AI;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nick on 2/25/2016.
 */
public class DisplayControls extends StackPane {
    private HashMap<Tile, JFXButton> currentTurn;
    private final String[] playerImages = {"playerone.png", "playertwo.png"};
    private final String[] computerImages = {"computerone.png", "computertwo.png"};
    private JFXButton[] turnImageButtons;
    public DisplayControls(Connect4Display display, SettingsDialog settingsMenu) {
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
        JFXButton settingsButton = new JFXButton("", settings);
        settingsButton.getStyleClass().add("icon-button");
        settingsButton.setMinSize(settings.getImage().getWidth(), settings.getImage().getHeight());
        settingsButton.setPrefSize(settings.getImage().getWidth(), settings.getImage().getHeight());
        settingsButton.setMaxSize(settings.getImage().getWidth(), settings.getImage().getHeight());

        JFXToolbar bottomToolbar = new JFXToolbar();
        bottomToolbar.maxHeightProperty().bind(settings.getImage().heightProperty());
        bottomToolbar.setCenter(settingsButton);
        bottomToolbar.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0,0,0,0.26), 10, 0.12, -1, -2));
        VBox bottomVBox = new VBox(bottomToolbar);
        borderPane.setBottom(bottomVBox);

        StackPane displayStack = new StackPane(display);
        borderPane.setCenter(displayStack);

        JFXDialog settingsDialog = new JFXDialog(displayStack, settingsMenu, JFXDialog.DialogTransition.BOTTOM);
        settingsButton.setOnMouseClicked((event) -> settingsDialog.show());
        settingsMenu.resume.setOnMouseClicked((event) -> settingsDialog.close());

        // Weird bug where the settings dialog wouldn't show the first time
        settingsDialog.show();
        settingsDialog.close();
        //displayStack.setAlignment(Pos.TOP_LEFT);

//        display.model.won.addListener((o, oldVal, newVal) -> {
//            if (newVal && display.model.getCurrentTurn() != Tile.EMPTY) {
//                ArrayList<Point[]> runs = display.model.currentState.get().getRuns(display.model.getCurrentTurn());
//                Point[] winningRun = null;
//                for (Point[] run: runs)
//                    if (run.length == 4) winningRun = run;
//                assert winningRun != null;
//
//                double startX = display.get(winningRun[0]).getParent().getBoundsInParent().getMinX(), startY = display.get(winningRun[0]).getParent().getBoundsInParent().getMinY();
//                double endX = display.get(winningRun[3]).getParent().getBoundsInParent().getMinX(), endY = display.get(winningRun[3]).getParent().getBoundsInParent().getMinY();
//
//                //startY += display.get(winningRun[0]).getParent().getLayoutBounds().getHeight();
//
//                Line wonLine = new Line(startX, startY, endX, endY);
//                wonLine.setFill(Paint.valueOf("FFFFFF"));
//                wonLine.setStrokeWidth(16);
//                displayStack.getChildren().add(wonLine);
//            }
//        });

        this.getChildren().add(borderPane);
    }
}
