package sample;

import com.sun.istack.internal.Nullable;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.Connect4Board;
import sample.StateDisplay;
import sample.states.BoardState;

import java.awt.Point;
import java.util.Map;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class Connect4Display extends GridPane {
    private StackPane[][] cellArray;
    private Connect4Board model;
    public int width, height;
    @Nullable private ObjectProperty<Point> hoveredCell;

    public Connect4Display(Connect4Board model) {
        super();
        this.model = model;
        this.cellArray = new StackPane[Connect4Board.COLUMNS][Connect4Board.ROWS];
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("connect4-display");
        StackPane.setAlignment(this, Pos.CENTER);
        Image tile = new Image("board.png");
        this.width = (int) tile.getWidth() * Connect4Board.COLUMNS;
        this.height = (int) tile.getHeight() * Connect4Board.ROWS;
        this.setMaxSize(width, height);
        this.hoveredCell = new SimpleObjectProperty<>();

        for (int x = 0; x < Connect4Board.COLUMNS; x++) {
            this.addColumn(0);
            for (int y = 0; y < Connect4Board.ROWS; y++) {
                this.addRow(0);
                StackPane innerCell = new StackPane();
                StackPane cell = new StackPane(innerCell, new ImageView("board.png"));
                this.add(cell, x, y);
                cellArray[x][y] = innerCell;

                innerCell.setCursor(Cursor.HAND);
                final int _x = x;
                innerCell.setOnMouseEntered((event) -> {
                    Point topCell = model.getTopCell(_x);
                    if (topCell.y != -1)
                        hoveredCell.setValue(topCell);
                    else hoveredCell.setValue(null);
                });
                innerCell.setOnMouseExited((event) -> {
                    Point topCell = model.getTopCell(_x);
                    if (topCell.y != -1 && get(hoveredCell.get()) == cellArray[topCell.x][topCell.y]) {
                        hoveredCell.setValue(null);
                    }
                });
                innerCell.setOnMouseClicked((event) -> {
                    model.set(model.getTopCell(_x), model.getCurrentTurn());
                    hoveredCell.setValue(null);
                });
            }
        }

        model.currentState.addListener(this::updateState);
        hoveredCell.addListener(this::showHoveredCell);
    }

    public void updateState(Observable o, BoardState oldVal, BoardState newVal) {
        Tile[][] state = newVal.getState();
        for (int x = 0; x < state.length; x++) {
            for (int y = 0; y < state[x].length; y++) {
                if(state[x][y] != Tile.EMPTY) {
                    cellArray[x][y].getChildren().clear();
                    cellArray[x][y].getChildren().add(getChip(state[x][y]));
                }
            }
        }
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
        ImageView chip = new ImageView("chip.png");

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
