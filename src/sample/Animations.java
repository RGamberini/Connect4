package sample;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Static sample.Animations class I pulled from my last project.
 */
public class Animations {
    private static final Interpolator materialInterp = Interpolator.SPLINE(.62,.28,.23,.99);
    public static TranslateTransition sweepOutDown(Node node) {
        TranslateTransition sweepOut = new TranslateTransition(Duration.millis(700), node);

        double toY = node.getLayoutY() + node.getLayoutBounds().getHeight() + 16;
        sweepOut.setToY(toY);
        sweepOut.setInterpolator(materialInterp);
        sweepOut.setCycleCount(1);
        return sweepOut;
    }

    public static TranslateTransition sweepInDown(Node node, double height) {
        TranslateTransition sweepOut = new TranslateTransition(Duration.millis(700), node);

        node.setTranslateY(height * -1);
        sweepOut.setToY(0);
        sweepOut.setInterpolator(materialInterp);
        sweepOut.setCycleCount(1);
        return sweepOut;
    }

    public static TranslateTransition sweepOut(Node node, boolean toTheRight) {
        TranslateTransition sweepOut = new TranslateTransition(Duration.millis(700), node);

        double toX = node.getLayoutX() + node.getLayoutBounds().getWidth() + 16;
        toX *= (toTheRight) ? 1 : -1;
        sweepOut.setToX(toX);
        sweepOut.setInterpolator(materialInterp);
        sweepOut.setCycleCount(1);
        return sweepOut;
    }

    public static TranslateTransition sweepIn(Node node, boolean toTheRight) {
        TranslateTransition sweepIn = new TranslateTransition(Duration.millis(700), node);
        //System.out.println(node.getLayoutBounds());

        double startX = 556;
        startX *= (toTheRight) ? 1 : -1;
        node.setTranslateX(startX);
        sweepIn.setToX(0);
        sweepIn.setInterpolator(materialInterp);
        sweepIn.setCycleCount(1);
        return sweepIn;
    }
}
