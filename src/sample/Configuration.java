package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.Map;

/**
 * Static Configuration class.
 */
public class Configuration {
    public static final Map<Tile, ObjectProperty<Color>> playerColor = new HashMap<>(2);
    static {
        playerColor.put(Tile.PLAYER1, new SimpleObjectProperty<>(Color.valueOf("D32F2F")));
        playerColor.put(Tile.PLAYER2, new SimpleObjectProperty<>(Color.valueOf("FFEB3B")));
    }
}
