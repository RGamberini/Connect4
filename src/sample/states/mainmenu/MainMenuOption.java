package sample.states.mainmenu;

import com.sun.istack.internal.Nullable;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.Animations;
import sample.states.State;

/**
 * Created by Nick on 2/26/2016.
 */
public abstract class MainMenuOption extends State {
//    protected VBox content;
    protected ImageView headerImage;
    protected VBox content;
    private StackPane contentStack;
    protected StackPane header;
    protected Transition entrance;
    protected MainMenuHeaderAndMachine main;

    public MainMenuOption(MainMenuHeaderAndMachine main, StackPane header, StackPane contentStack) {
        super(main);
        this.main = main;

        this.header = header;
        //header.getChildren().add(logo);
        header.setAlignment(Pos.CENTER);

        this.contentStack = contentStack;
        content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
    }

    @Override
    public void enter(@Nullable Transition exitTransition) {
        this.enter(exitTransition, false);
        //this.main._getChildren().add(content);
    }

    @Override
    public void enter(@Nullable Transition exitTransition, boolean backwards) {
        content.spacingProperty().bind(Bindings.multiply(.05, this.main.mainStack.maxHeightProperty()));
        content.prefHeightProperty().bind(this.main.mainStack.maxHeightProperty());
        Node[] contentToDelete = contentStack.getChildren().toArray(new Node[0]);
        Node[] headerImageToDelete = header.getChildren().toArray(new Node[0]);

        entrance = new ParallelTransition(Animations.sweepIn(headerImage, !backwards), Animations.sweepIn(content, !backwards));
        if (exitTransition != null)
            entrance = new ParallelTransition(entrance, exitTransition);
        contentStack.getChildren().add(content);
        header.getChildren().add(headerImage);
        entrance.play();
        entrance.setOnFinished((event) -> {
            contentStack.getChildren().removeAll((contentToDelete));
            header.getChildren().removeAll(headerImageToDelete);
        });
    }

    @Override
    public Transition exit() {
        return exit(false);
    }

    @Override
    public Transition exit(boolean backwards) {
        return new ParallelTransition(Animations.sweepOut(headerImage, backwards), Animations.sweepOut(content, backwards));
    }
}
