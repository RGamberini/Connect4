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
import sample.Main;
import sample.states.State;

/**
 * My naming skills could use some work but this is the abstract class
 * that any "option" of the main menu (New Game, Load Game, and somewhat confusingly Options) extends from.
 */
public abstract class MainMenuOption extends State {
//    protected VBox content;
    protected ImageView headerImage;
    protected final VBox content;
    private final StackPane contentStack;
    protected final StackPane header;
    protected Transition entrance;
    protected final MainMenu main;

    public MainMenuOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main);
        this.main = main;

        this.header = header;
        //header.getChildren().add(logo);
        header.setAlignment(Pos.CENTER);

        this.contentStack = contentStack;
        content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);

        content.spacingProperty().bind(Bindings.multiply(.05, this.main.maxHeightProperty()));
        content.prefHeightProperty().bind(this.main.maxHeightProperty());
    }

    @Override
    public void enter(@Nullable Transition exitTransition) {
        this.enter(exitTransition, false);
        //this.main._getChildren().add(content);
    }

    @Override
    public void enter(@Nullable Transition exitTransition, boolean backwards) {
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
