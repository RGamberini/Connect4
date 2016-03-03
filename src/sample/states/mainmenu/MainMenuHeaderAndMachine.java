package sample.states.mainmenu;

import com.jfoenix.controls.JFXToolbar;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import sample.Animations;
import sample.Connect4Board;
import sample.StateDisplay;
import sample.states.GameDisplayState;
import sample.states.State;
import sample.states.StateMachine;

/**
 * Created by Rudy Gamberini on 2/24/2016.
 */
public class MainMenuHeaderAndMachine extends State implements StateMachine{
    private State currentState;
    private StackPane mainStack;
    private VBox mainVBox;

    public MainMenuHeaderAndMachine(StateMachine main) {
        super(main);
        mainStack = new StackPane();
        Pane mainCard = new Pane();
        mainCard.getStyleClass().add("card");
        mainCard.getStyleClass().add("main-menu");
        mainStack.getChildren().add(mainCard);

        mainVBox = new VBox();
        Rectangle clipRectangle = new Rectangle();
        mainVBox.setClip(clipRectangle);
        mainVBox.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            clipRectangle.setWidth(newValue.getWidth());
            clipRectangle.setHeight(newValue.getHeight());
        });
        mainVBox.setAlignment(Pos.TOP_CENTER);
        mainVBox.spacingProperty().bind(Bindings.multiply(.05, mainStack.maxHeightProperty()));

        //mainCard.getChildren().add(mainVBox);

        JFXToolbar headerBG = new JFXToolbar();
        headerBG.prefWidthProperty().bind(main._getWidth());
        headerBG.minHeightProperty().bind(Bindings.multiply(.20, main._getHeight()));
        StackPane headerStack = new StackPane();
        StackPane contentStack = new StackPane();

        mainVBox.getChildren().addAll(new StackPane(headerBG, headerStack), contentStack);
        mainStack.getChildren().add(mainVBox);

        this.changeState(new MainMenuOptions(this, headerStack, contentStack));
    }

    @Override
    public void enter(Transition exitTransition) {
        mainStack.maxWidthProperty().bind(Bindings.divide(main._getWidth(), 1.8));
        mainStack.maxHeightProperty().bind(Bindings.divide(main._getHeight(), 1.3));
        main._getChildren().clear();
        main._getChildren().add(mainStack);
    }

    @Override
    public Transition exit() {
        return Animations.sweepOutDown(mainStack);
    }

    @Override
    public void changeState(State newState) {
        if (!(newState instanceof GameDisplayState)) {
            Transition exitTransition = null;
            if (currentState != null)
                exitTransition = currentState.exit();
            currentState = newState;
            currentState.enter(exitTransition);
        } else {
            System.out.println("Should NOT see this");
            System.out.println("Should be true: " + (main instanceof StateDisplay));
            main.changeState(newState);
        }
    }

    @Override
    public void changeState(State newState, boolean backwards) {
        Transition exitTransition = null;
        if (currentState != null)
            exitTransition = currentState.exit(backwards);
        currentState = newState;
        currentState.enter(exitTransition, backwards);
    }

    @Override
    public DoubleProperty _getWidth() {
        return mainStack.maxWidthProperty();
    }

    @Override
    public DoubleProperty _getHeight() {
        return mainStack.maxHeightProperty();
    }

    @Override
    public ObservableList<Node> _getChildren() {
        return mainVBox.getChildren();
    }

    public void createNewGame(Connect4Board board) {
        main.changeState(new GameDisplayState(main, board));
    }
}