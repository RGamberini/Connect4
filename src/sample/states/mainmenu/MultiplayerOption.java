package sample.states.mainmenu;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Created by Nick on 5/1/2016.
 */
public class MultiplayerOption extends MainMenuOption {
    public MultiplayerOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/multiplayer/multiplayer_HEADER.png");
    }
}
