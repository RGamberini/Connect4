package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Created by Nick on 2/26/2016.
 */
public class MainMenuOptions extends StartMenuOption {
    private RudeButton[] buttons;
    class RudeButton extends JFXButton {
        public StartMenuOption nextState;
        public ImageView image;
        public RudeButton(String text, ImageView image, StartMenuOption nextState) {
            super(text, image);
            this.nextState = nextState;
            this.getStyleClass().add("main-menu-button");
        }
    }
    public MainMenuOptions(MainMenuHeaderAndMachine main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("logo.png");

        RudeButton newgame = new RudeButton("", new ImageView("newgame.png"), new NewGameOption(main, header, contentStack));

        RudeButton loadgame = new RudeButton("", new ImageView("loadgame.png"), new NewGameOption(main, header, contentStack));

        RudeButton options = new RudeButton("", new ImageView("options.png"), new NewGameOption(main, header, contentStack));

        RudeButton quit = new RudeButton("", new ImageView("quitgame.png"), null);
        buttons = new RudeButton[]{newgame, loadgame, options, quit};

        for (RudeButton button : buttons) {
            button.prefWidthProperty().bind(Bindings.multiply(.75, main.mainStack.maxWidthProperty()));
            button.setOnMouseClicked((event) -> {
                if (button.nextState != null)
                    main.changeState(button.nextState);
            });
        }
        quit.setOnMouseClicked((event) -> Platform.exit());

        content.getChildren().addAll(buttons);
    }
}
