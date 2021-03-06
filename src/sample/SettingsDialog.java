package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import sample.states.State;
import sample.states.mainmenu.MainMenuState;

import javax.swing.plaf.FileChooserUI;
import java.io.File;

/**
 * In-Game settings menu.
 */
public class SettingsDialog extends Menu {
    private final static String pathPrefix = "gamedisplay/settings/";
    // Resume's action is set in Display Controls
    public final JFXButton resume;

    public SettingsDialog(StateDisplay main, Connect4Display display) {
        super();
        // I had a weird bug where the parent container still had hard corners, this fixed it.
        this.parentProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null)
                newVal.getStyleClass().add("main-menu");
        });

        headerBG.prefWidthProperty().bind(display.width);
        headerBG.minHeightProperty().bind(Bindings.multiply(.18, display.height));

        this.maxWidthProperty().bind(Bindings.divide(display.width, 1.75));
        this.maxHeightProperty().bind(Bindings.divide(display.height, 1.3));

        this.headerStack.getChildren().add(new ImageView(pathPrefix + "settings_HEADER.png"));

        VBox content = new VBox();
        content.setPadding(new Insets(0, 0, (.05 * this.getMaxHeight()), 0));
        content.setAlignment(Pos.CENTER);
        content.spacingProperty().bind(Bindings.multiply(.05, this.maxHeightProperty()));

        resume = new JFXButton("", new ImageView(pathPrefix + "resume.png"));
        resume.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        resume.getStyleClass().add("main-menu-button");
        content.getChildren().add(resume);

        JFXButton saveGame = new JFXButton("", new ImageView(pathPrefix + "savegame.png"));
        saveGame.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        saveGame.getStyleClass().add("main-menu-button");
        content.getChildren().add(saveGame);

        FileChooser saveFileChooser = new FileChooser();
        File saveFolder = new File("saves");
        saveFolder.mkdir();
        saveFileChooser.setInitialDirectory(saveFolder);
        saveFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

        JFXButton mainMenu = new JFXButton("", new ImageView("gamedisplay/wonmenu/" + "mainmenu.png"));
        mainMenu.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        mainMenu.getStyleClass().add("main-menu-button");
        content.getChildren().add(mainMenu);

        contentStack.getChildren().add(content);

        mainMenu.setOnMouseClicked((event) -> main.changeState(new MainMenuState(main)));
        saveGame.setOnMouseClicked((event) -> {
            File saveFile = saveFileChooser.showSaveDialog(this.getScene().getWindow());
            if (saveFile != null) GameSaver.saveGame(display.model, saveFile);
        });
    }
}
