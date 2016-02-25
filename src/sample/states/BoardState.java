package sample.states;

import sample.InvalidBoardException;
import sample.Tile;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class BoardState {
    Tile[][] state;
    public BoardState() {
        state = new Tile[7][6];
        for (int x = 0; x < state.length; x++)
            for (int y = 0; y < state[x].length; y++)
                state[x][y] = Tile.EMPTY;
    }

    public BoardState(Tile[][] initialState) throws InvalidBoardException {
        if (isValidBoard(initialState))
            this.state = initialState;
    }

    public boolean isValidBoard(Tile[][] state) throws InvalidBoardException {
        if (state.length != 7 || state[0].length != 6) {
            throw new InvalidBoardException("Incorrect board size");
        } else {
            for (int x = 0; x < state.length; x++) {
                for (int y = 0; y < state[x].length; y++) {
                    if (state[x][y] != Tile.EMPTY) {
                        for (int x2 = x; x < state.length; x++)
                            if (state[x2][y] == Tile.EMPTY) {
                                throw new InvalidBoardException("Floating Pieces");
                            }
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
            for (int y = 0; y < state[x].length; y++) {
                if (state[x][y] != Tile.EMPTY &&
                        state[x][y] == state[x-1][y+1] &&
                        state[x][y] == state[x-2][y+2] &&
                        state[x][y] == state[x-3][y+3])
                    return true;
            }
        }

        return false;
    }
}
