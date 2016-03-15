package sample.tree;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Created by Rudy Gamberini on 3/15/2016.
 */
public class TreeDisplay extends StackPane {
    private RecursiveTree model;
    public double width = 600;
    public double height = 300;
    public int maxChildren, maxDepth;

    public TreeDisplay(RecursiveTree model) {
        this.model = model;
        this.setAlignment(Pos.TOP_LEFT);
        int[] heightWidth = recursiveFindHeightWidth(model.rootNode, 0, 0);
        maxChildren = heightWidth[1];
        maxDepth = heightWidth[0];
        recursiveBuildDisplay(model.rootNode, 1);
    }

    public void recursiveBuildDisplay(TreeNode root, double index) {
        double x = (width / (double) maxChildren) * index;
        double y = (height / (double) maxDepth) * root.depth;
        TreeDisplayNode node = new TreeDisplayNode(root, index, root.depth);
        this.getChildren().add(node);
        node.setTranslateX(x);
        node.setTranslateY(y);

        for (int i = 0; i < root.children.size(); i++) {
            recursiveBuildDisplay(root.children.get(i), i);
        }
    }

    public int[] recursiveFindHeightWidth(TreeNode root, int height, int width) {
        int[] heightWidth = {height, width};
        if (root.depth > height) height = root.depth;
        for (int i = 0; i < root.children.size(); i++) {
            heightWidth = recursiveFindHeightWidth(root.children.get(i), height, i);
            if (heightWidth[0] > height) height = heightWidth[0];
            if (heightWidth[1] > width) width = heightWidth[1];
        }
        return heightWidth;
    }
}
