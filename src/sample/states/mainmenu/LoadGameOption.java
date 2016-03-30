package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import sample.Connect4Board;
import sample.Connect4Display;
import sample.GameSaver;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nick on 3/28/2016.
 */
public class LoadGameOption extends MainMenuOption {
    ObjectProperty<Connect4Board> board;

    public LoadGameOption(MainMenu main, StackPane header, StackPane contentStack) {
        super(main, header, contentStack);
        headerImage = new ImageView("mainmenu/loadgame/loadgame_HEADER.png");

        VBox topVBox = new VBox();
        //topVBox.spacingProperty().bind(content.spacingProperty());
        topVBox.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(topVBox, Priority.ALWAYS);
        content.getChildren().add(topVBox);

        board = new SimpleObjectProperty<>(null);

        FileChooser loadGameChooser = new FileChooser();
        File saveFolder = new File("saves");
        saveFolder.mkdir();
        loadGameChooser.setInitialDirectory(saveFolder);
        loadGameChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));

        /**
         * Buttons
         */

        JFXButton chooseFile = new JFXButton("", new ImageView("mainmenu/loadgame/choosefile.png"));
        chooseFile.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        chooseFile.getStyleClass().add("main-menu-button");

        JFXButton confirm = new JFXButton("", new ImageView("mainmenu/newgame/confirm.png"));
        confirm.setDisable(true);
        confirm.setOnMouseClicked((event) -> main.createNewGame(board.get()));
        confirm.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        confirm.getStyleClass().add("main-menu-button");

        JFXButton cancel = new JFXButton("", new ImageView("mainmenu/newgame/cancel.png"));
        cancel.setOnMouseClicked((event) -> main.changeState(new MainOptions(main, header, contentStack), true));
        cancel.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        cancel.getStyleClass().add("main-menu-button");

        VBox buttons = new VBox(chooseFile, cancel, confirm);
        buttons.setSpacing(content.getSpacing());
        content.spacingProperty().unbind();
        content.setSpacing(0);
        VBox.setMargin(buttons, new Insets(0, 0, 24, 0));
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        content.getChildren().add(buttons);

        board.addListener((o, oldVal, newVal) -> {
            if (newVal != null) confirm.setDisable(false);
            else confirm.setDisable(true);
        });
        chooseFile.setOnMouseClicked((event) -> {
            File file = loadGameChooser.showOpenDialog(header.getScene().getWindow());
            if (file != null) {
                board.set(GameSaver.loadGame(file));

                Connect4Display display = new Connect4Display(board.get());
                main.main.getChildren().add(0, display);
                WritableImage preview = display.snapshot(null, null);
                main.main.getChildren().remove(display);
                ImageView previewImage = new ImageView(preview);
                previewImage.setSmooth(true);
                previewImage.setPreserveRatio(false);
                previewImage.setFitWidth(main.getWidth() * .3);
                previewImage.setFitHeight(main.getHeight() * .21);

                topVBox.getChildren().add(previewImage);
            }
        });
    }
}
