package sample.tree;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Created by Rudy Gamberini on 3/15/2016.
 */
public class TreeDisplayNode extends StackPane {
    public double x, y;
    public TreeDisplayNode(TreeNode node, double x, double y) {
        this.setAlignment(Pos.CENTER);
        Circle mainCircle = new Circle(50, Paint.valueOf("FFFFFF"));
        this.x = x;
        this.y = y;
        this.setMaxSize(mainCircle.getRadius() * 2, mainCircle.getRadius() * 2);
//        mainCircle.setCenterX(x);
//        mainCircle.setCenterY(y);
        this.getChildren().add(mainCircle);
        this.getChildren().add(new Label("(" + x + ", " + y + ")\n" + node.getMove().value));
    }
}
