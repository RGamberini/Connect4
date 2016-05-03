package sample;

/**
 * Created by Nick on 5/2/2016.
 */

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import sample.AI.PlayerType;

import java.util.HashMap;

public class PlayerTypePicker extends HBox {
    private static final PlayerType[] PlayerTypes = new PlayerType[] {
            PlayerType.HUMAN, PlayerType.MINIMAX, PlayerType.MONTECARLO
    };
    public IntegerProperty currentIndex;

    private static HashMap<PlayerType, Image> images = new HashMap<>();
    static {
        images.put(PlayerType.HUMAN, new Image("mainmenu/newgame/human.png"));
        images.put(PlayerType.MINIMAX, new Image("mainmenu/newgame/minimax.png"));
        images.put(PlayerType.MONTECARLO, new Image("mainmenu/newgame/montecarlo.png"));
    }

    public PlayerTypePicker() {
        super(8);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("number");

        currentIndex = new SimpleIntegerProperty(-1);

        StackPane imagePane = new StackPane();
        imagePane.setPrefWidth(images.get(PlayerType.MONTECARLO).getWidth());
        ImageView currentImage = new ImageView();
        imagePane.getChildren().add(currentImage);

        ImageView carrotLeft = new ImageView("numbers/carrot_right.png");
        carrotLeft.setScaleX(-1);
        JFXButton leftArrow = new JFXButton("", carrotLeft);
        leftArrow.getStyleClass().add("carrot");

        ImageView carrotRight = new ImageView("numbers/carrot_right.png");
        JFXButton rightArrow = new JFXButton("", carrotRight);
        rightArrow.getStyleClass().add("carrot");

        this.getChildren().addAll(leftArrow, imagePane, rightArrow);

        /**
         * Event handlers
         */
        currentIndex.addListener((o, oldVal, newVal) -> {
            currentImage.setImage(images.get(PlayerTypes[newVal.intValue()]));
            if (newVal.intValue() == 2)
                rightArrow.setDisable(true);
            else
                rightArrow.setDisable(false);
            if (newVal.intValue() == 0)
                leftArrow.setDisable(true);
            else
                leftArrow.setDisable(false);
        });

        rightArrow.setOnMouseClicked((event) -> {
            if (currentIndex.get() < 2)
                currentIndex.setValue(currentIndex.get() + 1);
        });

        leftArrow.setOnMouseClicked((event) -> {
            if (currentIndex.get() > 0)
                currentIndex.setValue(currentIndex.get() - 1);
        });
        currentIndex.set(0);
    }
    public PlayerType getCurrentType() {
        return PlayerTypes[currentIndex.get()];
    }
}
