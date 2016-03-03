package sample.states;

import sample.Connect4Board;
import sample.InvalidBoardException;
import sample.Tile;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class BoardState {
    private Tile[][] state;
    public Tile turn;
    public BoardState() {
        turn = Tile.PLAYER1;
        state = new Tile[Connect4Board.COLUMNS][Connect4Board.ROWS];
        for (int x = 0; x < state.length; x++)
            for (int y = 0; y < state[x].length; y++)
                state[x][y] = Tile.EMPTY;
    }

    public BoardState(BoardState oldState, Tile turn) throws InvalidBoardException {
        this(oldState.getState());
        this.turn = turn;
    }

    public BoardState(Tile[][] initialState) throws InvalidBoardException {
        if (isValidBoard(initialState))
            this.state = initialState;
    }

    public static boolean isValidBoard(Tile[][] state) throws InvalidBoardException {
        if (state.length != 7 || state[0].length != 6) {
            throw new InvalidBoardException("Incorrect board size");
        } else {
            for (int x = 0; x < state.length; x++) {
                for (int y = 0; y < state[x].length; y++) {
                    if (state[x][y] != Tile.EMPTY) {
                        //System.out.println("Tile at (" + x + ", " + y + ") belongs to " + state[x][y] +"\nChecking cells below");
                        for (int y2 = y; y2 < state[x].length; y2++)
                            if (state[x][y2] == Tile.EMPTY)
                                throw new InvalidBoardException("Floating Pieces");
                    }
                }
            }
        }
        return true;
    }

    private boolean inBounds(Point point) {
        return point.x < state.length && point.y < state[point.x].length && point.x >= 0 && point.y >= 0;
    }

    public void set(Point point, Tile value) {
        if (inBounds(point)) {
            this.state[point.x][point.y] = value;
        }
    }

    public Tile get(Point point) {
        return state[point.x][point.y];
    }

    public boolean checkForGameOver() {
        /**
         * First check for horizontal wins
         */
        for (int x = 0; x < state.length; x++) {
            for (int y = 0; y < state[x].length - 3; y++) {
                if (state[x][y] != Tile.EMPTY &&
                        state[x][y] == state[x][y + 1] &&
                        state[x][y] == state[x][y + 2] &&
                        state[x][y] == state[x][y + 3])
                    return true;
            }
        }

        /**
         * Now for vertical
         */
        for (int x = 0; x < state.length - 3; x++) {
            for (int y = 0; y < state[x].length; y++) {
                if (state[x][y] != Tile.EMPTY &&
                        state[x][y] == state[x+1][y] &&
                        state[x][y] == state[x+2][y] &&
                        state[x][y] == state[x+3][y])
                    return true;
            }
        }

        /**
         * And finally the diagonals
         * Right and Down
         */

        for (int x = 0; x < state.length - 3; x++) {
            for (int y = 0; y < state[x].length - 3; y++) {
                if (state[x][y] != Tile.EMPTY &&
                        state[x][y] == state[x+1][y+1] &&
                        state[x][y] == state[x+2][y+2] &&
                        state[x][y] == state[x+3][y+3])
                    return true;
            }
        }

        /**
         * Right and Up
         */
        for (int x = 0; x < state.length - 3; x++) {
            for (int y = 3; y < state[x].length; y++) {
                if (state[x][y] != Tile.EMPTY &&
                        state[x][y] == state[x+1][y-1] &&
                        state[x][y] == state[x+2][y-2] &&
                        state[x][y] == state[x+3][y-3])
                    return true;
            }
        }

        return false;
    }

    public Point getTopCell(int x) {
        for (int y = state[x].length - 1; y >= 0; y--) {
            if (state[x][y] == Tile.EMPTY)
                return new Point(x, y);
        }
        return new Point(x, -1);
    }

    public Tile[][] getState() {
        //No exterior meddling of state!
        return state.clone();
    }
}
