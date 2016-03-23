package sample;

import com.jfoenix.controls.JFXToolbar;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Created by Nick on 3/20/2016.
 */
public class Menu extends StackPane {
    protected JFXToolbar headerBG;
    protected VBox mainVBox;
    protected StackPane headerStack;
    protected StackPane contentStack;

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
