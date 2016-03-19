package sample;

import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 3/5/2016.
 */
public class WonDialog extends Pane {
    public final static Map<Tile, String> tileImageName = new HashMap<>(3);
    static {
        tileImageName.put(Tile.EMPTY, "error.png");
        tileImageName.put(Tile.PLAYER1, "player1won.png");
        tileImageName.put(Tile.PLAYER2, "player2won.png");
    }
    public WonDialog() {
        //this.getStyleClass().add("card");
        this.getStyleClass().add("won-menu");
        this.setMinSize(400, 300);
        this.parentProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null)
                newVal.getStyleClass().add("won-menu");
        });
    }
}
