package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.states.GameDisplayState;
import sample.states.mainmenu.MainMenuState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 3/5/2016.
 */
public class WonDialog extends Menu {
    private final static String pathPrefix = "gamedisplay/wonmenu/";
    private final static Map<Tile, String> playerWinImage = new HashMap<>(2);
    static {
        playerWinImage.put(Tile.PLAYER1, "playerone_large.png");
        playerWinImage.put(Tile.PLAYER2, "playertwo_large.png");
    }

    private final static Map<Tile, String> computerWinImage = new HashMap<>(2);
    static {
        computerWinImage.put(Tile.PLAYER1, "computerone_large.png");
        computerWinImage.put(Tile.PLAYER2, "computertwo_large.png");
    }

    private final static String TIE = "tie.png";
    private Connect4Board model;
    private VBox mainVBox;

    public WonDialog(StateDisplay main, Connect4Board model) {
        super();
        // I had a weird bug where the parent container still had hard corners, this fixed it.
        this.parentProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != null)
                newVal.getStyleClass().add("main-menu");
        });

        headerBG.prefWidthProperty().bind(main._width);
        headerBG.minHeightProperty().bind(Bindings.multiply(.20, main._height));

        this.maxWidthProperty().bind(Bindings.divide(main._width, 2.13));
        this.maxHeightProperty().bind(Bindings.divide(main._height, 1.3));

        this.model = model;
        this.model.won.addListener(this::gameWon);

        mainVBox = new VBox(16);
        mainVBox.setAlignment(Pos.CENTER);

        this.headerStack.getChildren().add(mainVBox);

        VBox content = new VBox();
        content.setPadding(new Insets(0, 0, (.05 * this.getMaxHeight()), 0));
        content.setAlignment(Pos.CENTER);
        content.spacingProperty().bind(Bindings.multiply(.05, this.maxHeightProperty()));

        JFXButton playAgain = new JFXButton("", new ImageView(pathPrefix + "playagain.png"));
        playAgain.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        playAgain.getStyleClass().add("main-menu-button");
        content.getChildren().add(playAgain);

        JFXButton mainMenu = new JFXButton("", new ImageView(pathPrefix + "mainmenu.png"));
        mainMenu.prefWidthProperty().bind(Bindings.multiply(.75, this.maxWidthProperty()));
        mainMenu.getStyleClass().add("main-menu-button");
        content.getChildren().add(mainMenu);

        contentStack.getChildren().add(content);

        playAgain.setOnMouseClicked((event) -> {
            int players = 0;
            for (Player player: model.getPlayers().values())
                if (!(player instanceof AI)) players++;
            main.changeState(new GameDisplayState(main, new Connect4Board(players)));
        });

        mainMenu.setOnMouseClicked((event) -> main.changeState(new MainMenuState(main)));
    }

    public void gameWon(Observable o, boolean oldVal, boolean newVal) {
        if (newVal) {
            Tile winningTile = model.currentState.get().turn;
            String winnerImagePath;
            System.out.println(model.currentState.get().getAllMoves().length);
            if (model.currentState.get().getAllMoves().length > 0) {
                if (model.getPlayer(winningTile) instanceof AI)
                    winnerImagePath = computerWinImage.get(winningTile);
                else
                    winnerImagePath = playerWinImage.get(winningTile);
                StackPane won = new StackPane(new ImageView(pathPrefix + "won.png"));
                won.setAlignment(Pos.CENTER);
                this.mainVBox.getChildren().add(won);
            } else winnerImagePath = TIE;
            Image winnerImageBase = new Image(pathPrefix + winnerImagePath);
            StackPane winnerImage = new StackPane(new ImageView(winnerImageBase));

            mainVBox.spacingProperty().bind(Bindings.divide(winnerImageBase.heightProperty(), 4));
            winnerImage.setAlignment(Pos.CENTER);
            this.mainVBox.getChildren().add(0, winnerImage);
        }
    }
}
