package sample.tree;

import sample.Player;
import sample.states.BoardState;

import java.util.ArrayList;

/**
 * Created by Nick on 3/15/2016.
 */
public class TreeNode {
    private Player.Move move;
    private BoardState state;
    public ArrayList<TreeNode> children;
    public int depth;
    public TreeNode parent;

    public TreeNode(Player.Move move, BoardState state, int depth) {
        this.move = move;
        this.state = state;
        this.depth = depth;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public Player.Move getMove() {
        return move;
    }

    public BoardState getState() {
        return state;
    }
}
