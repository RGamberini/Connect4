package sample;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Nick on 3/1/2016.
 */
public class NumberImage extends ImageView {
    private static final String[] imageURLS = {"numbers/zero.png", "numbers/one.png", "numbers/two.png"};
    public IntegerProperty number;

    /**
     * @param start An int between 0-2
     */
    public NumberImage(int start) {
        super(imageURLS[start]);
        number = new SimpleIntegerProperty(start);

        number.addListener(this::numberChange);
    }

    private void numberChange(Observable o, Number oldVal, Number newVal) {
        this.setImage(new Image(imageURLS[newVal.intValue()]));
    }
}
