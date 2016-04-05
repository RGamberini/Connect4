package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

/**
 * Created by Nick on 4/4/2016.
 */
public class ColorSetPicker extends Pane {
    private ObjectProperty<ColorSwatch> selectedButton;

    private static HashMap<Tile, Color[]> colorSets = new HashMap<>();
    static {
        colorSets.put(Tile.PLAYER1, new Color[]{
                Color.valueOf("#D32F2F"), Color.valueOf("F44336"), Color.valueOf("E91E63"), Color.valueOf("F8BBD0")});

        colorSets.put(Tile.PLAYER2, new Color[]{
                Color.valueOf("FFEB3B"), Color.valueOf("FBC02D"), Color.valueOf("FFC107"), Color.valueOf("FFF9C4")});
    }
    private class ColorSwatch extends JFXButton {
        Color color;

        public ColorSwatch(Color color) {
            super();
            this.color = color;

            this.setStyle("-fx-background-color: #" + color.toString().substring(2, color.toString().length() - 2));
            this.getStyleClass().add("color-swatch");
        }
    }

    public ColorSetPicker(Region parent, Tile tile) {
        double swatchSize = 4.5;
        double growSize = 1.2;
        selectedButton = new SimpleObjectProperty<>();
        selectedButton.addListener((o, oldVal, newVal) -> {
            if (oldVal != null) oldVal.getStyleClass().remove("selected");
            newVal.getStyleClass().add("selected");
            Configuration.playerColor.get(tile).setValue(newVal.color);
        });

        Color[] colors = colorSets.get(tile);
        for (int i = 0; i < colors.length; i++) {
            Color color = colors[i];

            ColorSwatch colorSwatch = new ColorSwatch(color);
            if (color.toString().equalsIgnoreCase(Configuration.playerColor.get(tile).get().toString())) selectedButton.set(colorSwatch);
            colorSwatch.setButtonType(JFXButton.ButtonType.RAISED);
            colorSwatch.setRipplerFill(Paint.valueOf("#FFFFFF"));
            DoubleProperty size = new SimpleDoubleProperty();
            size.bind(Bindings.divide(parent.maxWidthProperty(), swatchSize));

            colorSwatch.minWidthProperty().bind(size);
            colorSwatch.minHeightProperty().bind(size);

            colorSwatch.layoutXProperty().bind(
                    Bindings.add(
                            Bindings.add(
                                    Bindings.multiply(colorSwatch.widthProperty(), i),
                                    Bindings.multiply(
                                            Bindings.subtract(
                                                    Bindings.multiply(colorSwatch.widthProperty(), growSize - .15),
                                                    colorSwatch.widthProperty()),
                                            i)
                            ), Bindings.subtract(
                                    Bindings.multiply(colorSwatch.widthProperty(), growSize),
                                    colorSwatch.widthProperty()))
            );
            colorSwatch.layoutYProperty().bind(Bindings.subtract(Bindings.multiply(size, growSize), size));

            colorSwatch.setOnMouseEntered((event) -> {
                colorSwatch.toFront();
                Animations.enlarge(colorSwatch).play();
            });

            colorSwatch.setOnMouseExited((event) -> Animations.shrinkToNormal(colorSwatch).play());
            colorSwatch.setOnMouseClicked((event) -> selectedButton.set(colorSwatch));

            this.getChildren().add(colorSwatch);
        }
    }
}
