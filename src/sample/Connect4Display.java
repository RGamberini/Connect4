package sample;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.Connect4Board;
import sample.StateDisplay;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class Connect4Display extends GridPane {
    private ImageView[][] cellArray;
    private Connect4Board model;
    public int width, height;

    public Connect4Display(Connect4Board model) {
        super();
        this.model = model;
        this.cellArray = new ImageView[Connect4Board.COLUMNS][Connect4Board.ROWS];
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("connect4-display");
        StackPane.setAlignment(this, Pos.CENTER);
        Image tile = new Image("board.png");
        this.width = (int) tile.getWidth() * Connect4Board.COLUMNS;
        this.height = (int) tile.getHeight() * Connect4Board.ROWS;
        this.setMaxSize(width, height);

        for (int x = 0; x < Connect4Board.COLUMNS; x++) {
            this.addColumn(0);
            for (int y = 0; y < Connect4Board.ROWS; y++) {
                this.addRow(0);
                ImageView cell = new ImageView("board.png");
                this.add(cell, x, y);
                cellArray[x][y] = cell;
            }
        }
    }

    public void updateState() {

    }
}
