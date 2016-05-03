package sample.states.mainmenu;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sample.AI.PlayerType;
import sample.Connect4Socket;
import sample.NetworkConnect4Board;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Nick on 5/2/2016.
 */
public class EstablishConnection extends MainMenuOption {
    private Thread thread;
    private PlayerType playerType;
    public EstablishConnection(MainMenu main, StackPane header, StackPane contentStack, PlayerType playerType, String ip) {
        super(main, header, contentStack);
        this.playerType = playerType;
        headerImage = new ImageView("mainmenu/multiplayer/pleasewait_HEADER.png");

        StackPane waitingForClient;
        boolean host = ip == null;

        if (host) waitingForClient = new StackPane(new ImageView("mainmenu/multiplayer/establishing_HOST.png"));
        else waitingForClient = new StackPane(new ImageView("mainmenu/multiplayer/establishing_CLIENT.png"));

        waitingForClient.setAlignment(Pos.TOP_CENTER);
        JFXSpinner spinner = new JFXSpinner();
        spinner.setRadius(98);
        VBox topVBox = new VBox(waitingForClient, spinner);
        topVBox.setAlignment(Pos.CENTER);
        topVBox.spacingProperty().bind(content.spacingProperty());
        VBox.setVgrow(topVBox, Priority.ALWAYS);
        content.getChildren().add(topVBox);

        JFXButton cancel = new JFXButton("", new ImageView("mainmenu/newgame/cancel.png"));
        cancel.setOnMouseClicked((event) -> main.changeState(new MultiplayerOption(main, header, contentStack), true));
        cancel.maxWidthProperty().bind(Bindings.divide(main.maxWidthProperty(), 1.4));
        cancel.getStyleClass().add("main-menu-button");

        VBox buttons = new VBox(cancel);
        buttons.spacingProperty().bind(content.spacingProperty());
        VBox.setMargin(buttons, new Insets(0, 0, 24, 0));
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(buttons, Priority.ALWAYS);
        content.getChildren().add(buttons);

        if (host) {
            try {
                ServerSocket serverSocket = new ServerSocket(44444);
                Task<Socket> establishConnection = new Task<Socket>() {
                    @Override
                    protected Socket call() throws Exception {
                        return serverSocket.accept();
                    }
                };
                establishConnection.setOnSucceeded((event) -> createConnect4Socket(establishConnection.getValue(), true));
                thread = new Thread(establishConnection);
                thread.setDaemon(true);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Socket socket = new Socket(ip, 44444);
                createConnect4Socket(socket, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void createConnect4Socket(Socket socket, boolean host) {
        try {
            Connect4Socket connect4Socket = new Connect4Socket(socket, host);
            Task<Void> startupTask = connect4Socket.startup();
            startupTask.setOnSucceeded((event) -> {
                NetworkConnect4Board connect4Board = new NetworkConnect4Board(playerType, connect4Socket, host);
                main.createNewGame(connect4Board);
            });
            thread = new Thread(startupTask);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transition exit() {
        thread.interrupt();
        return super.exit();
    }

    @Override
    public Transition exit(boolean backwards) {
        thread.interrupt();
        return super.exit(backwards);
    }
}
