package sample.tree;

import java.util.ArrayList;

/**
 * Created by Nick on 3/15/2016.
 */
public class RecursiveTree {
    private int maxDepth, currentDepth;
    private ArrayList<ArrayList<TreeNode>> futureChildren;
    public TreeNode rootNode;

    public RecursiveTree(int maxDepth) {
        this.maxDepth = maxDepth + 1;
        this.currentDepth = 0;
        this.futureChildren = new ArrayList<>(this.maxDepth);
        for (int i = 0; i < this.maxDepth; i++) {
            futureChildren.add(new ArrayList<>());
        }
        this.rootNode = null;
    }

    public void addNode(TreeNode node) {
        if (node.depth > currentDepth) {
            for (TreeNode child: futureChildren.get(currentDepth)) child.parent = node;
            node.children.addAll(futureChildren.get(currentDepth));
            this.futureChildren.get(currentDepth).clear();
        }
        this.currentDepth = node.depth;

        if (node.depth == maxDepth) {
            this.rootNode = node;
        } else {
            this.futureChildren.get(currentDepth).add(node);
        }
    }
}
