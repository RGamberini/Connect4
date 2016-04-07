package sample;

import com.jfoenix.controls.JFXToolbar;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Generic Menu that contains a well styled header and body.
 */
public class Menu extends StackPane {
    protected final JFXToolbar headerBG;
    protected final VBox mainVBox;
    protected final StackPane headerStack;
    protected final StackPane contentStack;

    public Menu() {
        Pane mainCard = new Pane();
        mainCard.getStyleClass().add("card");
        mainCard.getStyleClass().add("main-menu");
        this.getChildren().add(mainCard);

        mainVBox = new VBox();
        Rectangle clipRectangle = new Rectangle();
        mainVBox.setClip(clipRectangle);
        mainVBox.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            clipRectangle.setWidth(newValue.getWidth());
            clipRectangle.setHeight(newValue.getHeight());
        });
        mainVBox.setAlignment(Pos.TOP_CENTER);
        mainVBox.spacingProperty().bind(Bindings.multiply(.05, this.maxHeightProperty()));

        headerBG = new JFXToolbar();

        headerStack = new StackPane();
        contentStack = new StackPane();

        mainVBox.getChildren().addAll(new StackPane(headerBG, headerStack), contentStack);
        this.getChildren().add(mainVBox);
    }

}
