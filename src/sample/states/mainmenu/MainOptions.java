package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * The first state of the Main Menu that shows all the other options.
 */
public class MainOptions extends MainMenuOption {
    private final RudeButton[] buttons;
    class RudeButton extends JFXButton {
        public final MainMenuOption nextState;
        public ImageView image;
        public RudeButton(String text, ImageView image, MainMenuOption nextState) {
            super(text, image);
            this.nextState = nextState;
            this.getStyleClass().add("main-menu-button");
        }
    }
    public MainOptions(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/logo.png");

        RudeButton newgame = new RudeButton("", new ImageView("mainmenu/newgame.png"), new NewGameOption(main, header, contentStack));

        RudeButton loadgame = new RudeButton("", new ImageView("mainmenu/loadgame.png"), new LoadGameOption(main, header, contentStack));

        RudeButton multiplayer = new RudeButton("", new ImageView("mainmenu/multiplayer.png"), new OptionsOption(main, header, contentStack));

        RudeButton options = new RudeButton("", new ImageView("mainmenu/options.png"), new OptionsOption(main, header, contentStack));

        RudeButton quit = new RudeButton("", new ImageView("mainmenu/quitgame.png"), null);
        buttons = new RudeButton[]{newgame, loadgame, multiplayer, options, quit};

        for (RudeButton button : buttons) {
            button.prefWidthProperty().bind(Bindings.multiply(.75, main.maxWidthProperty()));
            button.setOnMouseClicked((event) -> {
                if (button.nextState != null)
                    main.changeState(button.nextState);
            });
        }
        quit.setOnMouseClicked((event) -> Platform.exit());

        content.getChildren().addAll(buttons);
    }
}
