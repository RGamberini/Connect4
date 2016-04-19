package sample.AI;

import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Framework from https://web.archive.org/web/20160305232331/http://mcts.ai/code/java.html
 */
class TreeNode {
    static Random r = new Random();
    static double epsilon = 1e-6;

    TreeNode[] children;
    double nVisits, totValue;
    public Move move;
    public BoardState initialState;
    private Tile player;

    public TreeNode(BoardState initialState, Tile player) {
        this.initialState = initialState;
        this.player = player;
    }

    public void selectAction() {
        List<TreeNode> visited = new LinkedList<>();
        TreeNode cur = this;
        visited.add(this);
        while (!cur.isLeaf()) {
            cur = cur.select();
            visited.add(cur);
        }
        cur.expand();
        TreeNode newNode = cur.select();
        visited.add(newNode);
        double value = rollOut(newNode);
        for (TreeNode node : visited) {
            // would need extra logic for n-player game
            node.updateStats(value);
        }
    }

    public void expand() {
        Point[] allMoves = initialState.getAllMoves();
        children = new TreeNode[allMoves.length];
        for (int i=0; i < allMoves.length; i++) {
            children[i] = new TreeNode(initialState.set(allMoves[i]), player);
        }
    }

    private TreeNode select() {
        TreeNode selected = null;
        double bestValue = Double.MIN_VALUE;
        for (TreeNode c : children) {
            double uctValue = c.totValue / (c.nVisits + epsilon) +
                    Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
                    r.nextDouble() * epsilon;
            // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public double rollOut(TreeNode tn) {
        BoardState rollOutState = tn.initialState;
        while (rollOutState.winner != Tile.EMPTY) {
            Point[] allMoves = rollOutState.getAllMoves();
            rollOutState.set(allMoves[r.nextInt(allMoves.length)]);
        }
        return rollOutState.winner == player ? 1:0;
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }
}