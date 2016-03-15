package sample;

import sample.states.BoardState;
import sample.tree.RecursiveTree;
import sample.tree.TreeNode;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Nick on 3/9/2016.
 */
public class Player {
    protected Connect4Board board;
    protected Tile tile;

    public Player(Connect4Board board, Tile tile) {
        this.board = board;
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }
}