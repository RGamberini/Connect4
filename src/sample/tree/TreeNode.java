package sample.tree;

import sample.AI;
import sample.Player;
import sample.states.BoardState;

import java.util.ArrayList;

/**
 * Created by Nick on 3/15/2016.
 */
public class TreeNode {
    private AI.Move move;
    private BoardState state;
    public ArrayList<TreeNode> children;
    public int depth;
    public TreeNode parent;

    public TreeNode(AI.Move move, BoardState state, int depth) {
        this.move = move;
        this.state = state;
        this.depth = depth;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public AI.Move getMove() {
        return move;
    }

    public BoardState getState() {
        return state;
    }
}
