package sample;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        int size = 250;
        StateDisplay stateDisplay = new StateDisplay();
        JFXDecorator prettyWindow = new JFXDecorator(primaryStage, stateDisplay);
        HBox buttonContainer = (HBox) prettyWindow.getChildren().get(0);
        //Remove extra buttons on the window
        buttonContainer.getChildren().remove(0);
        buttonContainer.getChildren().remove(1);

        Scene scene = new Scene(prettyWindow);
        primaryStage.setWidth(stateDisplay._width.get());
        primaryStage.setHeight(stateDisplay._height.get());
        primaryStage.setTitle("Connect 4");

        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
