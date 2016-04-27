package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.AI.AIType;
import sample.Connect4Board;

import java.util.HashMap;

/**
 * A state of the Main Menu for creating a new game.
 */
public class NewGameOption extends MainMenuOption {

    public NewGameOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/newgame/newgame_HEADER.png");

        String[] playerImageStrings = new String[]{"mainmenu/newgame/playerone.png", "mainmenu/newgame/playertwo.png"};
        PlayerTypePicker[] playerTypePickers = new PlayerTypePicker[playerImageStrings.length];
        VBox pickers = new VBox();
        for (int i = 0; i < playerImageStrings.length; i++) {
            String playerImageString = playerImageStrings[i];
            StackPane playerImage = new StackPane(new ImageView(playerImageString));
            playerImage.setAlignment(Pos.CENTER);

            playerTypePickers[i] = new PlayerTypePicker();
            VBox playerVBox = new VBox(8, playerImage, playerTypePickers[i]);
            playerVBox.setAlignment(Pos.CENTER);
            playerVBox.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
            pickers.getChildren().add(playerVBox);
        }

        pickers.spacingProperty().bind(content.spacingProperty());
        pickers.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(pickers, Priority.ALWAYS);
        content.getChildren().add(pickers);


        /**
         * Buttons
         */
        JFXButton confirm = new JFXButton("", new ImageView("mainmenu/newgame/confirm.png"));
        confirm.setOnMouseClicked((event) -> main.createNewGame(new Connect4Board(new AIType[]{playerTypePickers[0].getCurrentType(), playerTypePickers[1].getCurrentType()})));
        confirm.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        confirm.getStyleClass().add("main-menu-button");

        JFXButton cancel = new JFXButton("", new ImageView("mainmenu/newgame/cancel.png"));
        cancel.setOnMouseClicked((event) -> main.changeState(new MainOptions(main, header, contentStack), true));
        cancel.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        cancel.getStyleClass().add("main-menu-button");

        VBox buttons = new VBox(cancel, confirm);
        buttons.spacingProperty().bind(content.spacingProperty());
        VBox.setMargin(buttons, new Insets(0, 0, 24, 0));
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        content.getChildren().add(buttons);
    }

    private static HashMap<AIType, Image> images = new HashMap<>();
    static {
                images.put(AIType.HUMAN, new Image("mainmenu/newgame/human.png"));
                images.put(AIType.MINIMAX, new Image("mainmenu/newgame/minimax.png"));
                images.put(AIType.MONTECARLO, new Image("mainmenu/newgame/montecarlo.png"));
    }

    class PlayerTypePicker extends HBox {
        private final AIType[] AITypes = new AIType[] {
                AIType.HUMAN, AIType.MINIMAX, AIType.MONTECARLO
        };
        public IntegerProperty currentIndex;
        public PlayerTypePicker() {
            super(8);
            this.setAlignment(Pos.CENTER);
            this.getStyleClass().add("number");

            currentIndex = new SimpleIntegerProperty(-1);

            StackPane imagePane = new StackPane();
            imagePane.setPrefWidth(images.get(AIType.MONTECARLO).getWidth());
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
                currentImage.setImage(images.get(AITypes[newVal.intValue()]));
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
        public AIType getCurrentType() {
            return AITypes[currentIndex.get()];
        }
    }
}