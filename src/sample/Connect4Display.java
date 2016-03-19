package sample;

import com.sun.istack.internal.Nullable;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.states.BoardState;

import java.awt.Point;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class Connect4Display extends GridPane {
    public StackPane[][] cellArray;
    public Connect4Board model;
    public int width, height;
    @Nullable private ObjectProperty<Point> hoveredCell;
    public BooleanProperty interactive;

    public Connect4Display(Connect4Board model) {
        super();
        this.model = model;
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("connect4-display");
        StackPane.setAlignment(this, Pos.CENTER);

        this.cellArray = new StackPane[Connect4Board.COLUMNS][Connect4Board.ROWS];
        Image tile = new Image("gamedisplay/board.png");

        this.width = (int) tile.getWidth() * Connect4Board.COLUMNS;
        this.height = (int) tile.getHeight() * Connect4Board.ROWS;
        this.setMaxSize(width, height);

        this.hoveredCell = new SimpleObjectProperty<>();
        this.interactive = new SimpleBooleanProperty(true);


        /**
         * Clip rectangle so you don't see the game pieces before you should
         */
        Rectangle clipRectangle = new Rectangle();
        this.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            clipRectangle.setWidth(newValue.getWidth());
            clipRectangle.setHeight(newValue.getHeight());
        });
        this.setClip(clipRectangle);

        for (int x = 0; x < Connect4Board.COLUMNS; x++) {
            this.addColumn(x);
            for (int y = 0; y < Connect4Board.ROWS; y++) {
                this.addRow(y);
                StackPane innerCell = new StackPane();
                StackPane cell = new StackPane(new ImageView("gamedisplay/board.png"), innerCell);
                this.add(cell, x, y);
                cellArray[x][y] = innerCell;

                innerCell.setCursor(Cursor.HAND);
                final int _x = x;
                innerCell.setOnMouseEntered((event) -> {
                    Point topCell = model.getTopCell(_x);
                    if (topCell.y != -1 && interactive.get())
                        hoveredCell.setValue(topCell);
                    else hoveredCell.setValue(null);
                });
                innerCell.setOnMouseExited((event) -> {
                    Point topCell = model.getTopCell(_x);
                    if (topCell.y != -1 &&
                            get(hoveredCell.get()) == cellArray[topCell.x][topCell.y] &&
                            interactive.get()) {
                        hoveredCell.setValue(null);
                    }
                });
                innerCell.setOnMouseClicked((event) -> {
                    if (interactive.get())
                        model.set(model.getTopCell(_x), model.getCurrentTurn());
                    hoveredCell.setValue(null);
                });
                interactive.addListener((o, oldVal, newVal) -> {
                    if (!interactive.get()) innerCell.setCursor(Cursor.DEFAULT);
                    else  innerCell.setCursor(Cursor.HAND);
                });
            }
        }

        model.currentState.addListener(this::updateState);
        model.currentState.addListener(this::updatePlayer);
        hoveredCell.addListener(this::showHoveredCell);
    }

    public void updateState(Observable o, BoardState oldVal, BoardState newVal) {
        Tile[][] oldState = oldVal.getState();
        Tile[][] newState = newVal.getState();
        for (int x = 0; x < newState.length; x++) {
            for (int y = 0; y < newState[x].length; y++) {
                if (newState[x][y] != Tile.EMPTY) {
                    cellArray[x][y].getChildren().clear();
                    ImageView chip = getChip(newState[x][y]);
                    cellArray[x][y].getChildren().add(chip);
                    if (oldState[x][y] == Tile.EMPTY)
                        Animations.sweepInDown(chip, height).play();
                }
            }
        }
        if(newVal.checkForGameOver())
            interactive.set(false);
    }

    public void updatePlayer(Observable o, BoardState oldVal, BoardState newVal) {
        if (model.getPlayer(newVal.turn) instanceof AI)
            interactive.set(false);
    }

    private StackPane get(Point p) {
        if (p != null)
            return cellArray[p.x][p.y];
        else
            return null;
    }

    /**
     * Whatever this magic is I pulled it from stackoverflow
     * http://stackoverflow.com/questions/31587092/how-to-use-coloradjust-to-set-a-target-color
     */
    public static double map(double value, double start, double stop, double targetStart, double targetStop) {
        return targetStart + (targetStop - targetStart) * ((value - start) / (stop - start));
    }

    public static ImageView getChip(Tile player) {
        ImageView chip = new ImageView("gamedisplay/chip.png");

        /**
         * Same deal
         * http://stackoverflow.com/questions/31587092/how-to-use-coloradjust-to-set-a-target-color
         */
        ColorAdjust colorAdjust = new ColorAdjust();
        Color targetColor = Configuration.playerColor.get(player).get();

        double hue = map((targetColor.getHue() + 180) % 360, 0, 360, -1, 1);
        colorAdjust.setHue(hue);

        double saturation = targetColor.getSaturation();
        colorAdjust.setSaturation(saturation);

        double brightness = map(targetColor.getBrightness(), 0, 1, -1, 0);
        colorAdjust.setBrightness(brightness);
        chip.setEffect(colorAdjust);

        return chip;
    }


    private void showHoveredCell(Observable o, Point oldVal, Point newVal) {
        if (oldVal != newVal) {
            if (oldVal != null && model.get(oldVal) == Tile.EMPTY)
                get(oldVal).getChildren().clear();
            if (newVal != null) {
                ImageView chip = getChip(model.getCurrentTurn());
                chip.setOpacity(.2);
                get(newVal).getChildren().add(chip);
            }
        }
    }
}
