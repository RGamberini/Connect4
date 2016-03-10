package sample.states;

import sample.Connect4Board;
import sample.InvalidBoardException;
import sample.Tile;

import java.awt.*;
import java.util.*;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class BoardState {
    private Tile[][] state;
    public Tile turn;
    private final Map<Tile, ArrayList<Point[]>> runs = new HashMap<>();
    public BoardState() {
        turn = Tile.PLAYER1;
        state = new Tile[Connect4Board.COLUMNS][Connect4Board.ROWS];
        for (int x = 0; x < state.length; x++)
            for (int y = 0; y < state[x].length; y++)
                state[x][y] = Tile.EMPTY;
        //Runs are empty because empty board
    }

    public BoardState(BoardState oldState, Tile turn) throws InvalidBoardException {
        if (isValidBoard(oldState.getState()))
            this.state = oldState.getState();
        this.runs.putAll(oldState.runs);
        this.turn = turn;
    }

    public BoardState(Tile[][] initialState) throws InvalidBoardException {
        if (isValidBoard(initialState))
            this.state = initialState;
        updateRuns();
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
        updateRuns();
    }

    public Tile get(Point point) {
        return state[point.x][point.y];
    }

    public Tile getNextTurn() { return Connect4Board.turnOrder.get((Connect4Board.turnOrder.indexOf(this.turn) + 1) % Connect4Board.turnOrder.size());}

    private void updateRuns() {
        for(Tile key: Connect4Board.turnOrder) {
            if (runs.get(key) != null)
                runs.get(key).clear();
            else
                runs.put(key, new ArrayList<>());
        }
        /**
         * First check for vertical runs
         */
        ArrayList<Point> inARun = new ArrayList<>();
        for (int x = 0; x < state.length; x++) {
            for (int y = state[x].length - 1; y > 2; y--) {
                if (state[x][y] != Tile.EMPTY) {
                    ArrayList<Point> run = new ArrayList<>();
                    Point origin = new Point(x, y);
                    run.add(origin);
                    for (int r = 1; r < 4; r++)
                        if (state[x][y - r] == state[x][y]) {
                            Point point = new Point(x, y + r);
                            run.add(point);
                            inARun.add(point);
                        }
                    if (run.size() > 1) {
                        runs.get(state[x][y]).add(run.toArray(new Point[run.size()]));
                        inARun.add(origin);
                    }
                }
            }
        }
        /**
         * Horizontal runs
         */
        inARun.clear();
        for (int x = 0; x < state.length - 3; x++) {
            for (int y = 0; y < state[x].length; y++) {
                if (state[x][y] != Tile.EMPTY) {
                    ArrayList<Point> run = new ArrayList<>();
                    Point origin = new Point(x, y);
                    run.add(origin);
                    for (int r = 1; r <= 3; r++) {
                        if (state[x + r][y] == state[x][y]) {
                            Point point = new Point(x + r, y);
                            run.add(point);
                            inARun.add(point);
                        }
                    }
                    if (run.size() > 1)
                        runs.get(state[x][y]).add(run.toArray(new Point[run.size()]));
                }
            }
        }
        /**
         * And finally the diagonals
         * Right and Down
         */
        inARun.clear();
        for (int x = 0; x < state.length - 3; x++) {
            for (int y = 0; y < state[x].length - 3; y++) {
                if (state[x][y] != Tile.EMPTY) {
                    ArrayList<Point> run = new ArrayList<>();
                    Point origin = new Point(x, y);
                    run.add(origin);
                    for (int r = 1; r <= 3; r++)
                        if (state[x+r][y+r] == state[x][y]) {
                            Point point = new Point(x + r, y + r);
                            run.add(point);
                            inARun.add(point);
                        }
                    if (run.size() > 1)
                        runs.get(state[x][y]).add(run.toArray(new Point[run.size()]));
                }
            }
        }
        /**
         * Right and Up
         */
        inARun.clear();
        for (int x = 0; x < state.length - 3; x++) {
            for (int y = 3; y < state[x].length; y++) {
                if (state[x][y] != Tile.EMPTY) {
                    ArrayList<Point> run = new ArrayList<>();
                    Point origin = new Point(x, y);
                    run.add(origin);
                    for (int r = 1; r <= 3; r++)
                        if (state[x+r][y-r] == state[x][y]) {
                            Point point = new Point(x + r, y + r);
                            run.add(point);
                            inARun.add(point);
                        }
                    if (run.size() > 1)
                        runs.get(state[x][y]).add(run.toArray(new Point[run.size()]));
                }
            }
        }
        //System.out.println(runs.size());
    }

    public ArrayList<Point[]> getRuns(Tile tile) {
        return this.runs.get(tile);
    }

    public boolean checkForGameOver() {
        for (Tile key: Connect4Board.turnOrder)
            for (Point[] run : this.runs.get(key))
                if (run.length > 3)
                    return true;
        return false;
    }

    public Point getTopCell(int x) {
        for (int y = state[x].length - 1; y >= 0; y--) {
            if (state[x][y] == Tile.EMPTY)
                return new Point(x, y);
        }
        return new Point(x, -1);
    }

    public Point[] getAllMoves() {
        ArrayList<Point> moves = new ArrayList<>();
        for (int x = 0; x < Connect4Board.COLUMNS; x++) {
            Point openTopCell = getTopCell(x);
            if (openTopCell.y != -1)
                moves.add(openTopCell);
        }
        return moves.toArray(new Point[moves.size()]);
    }

    public Tile[][] getState() {
        Tile[][] deepCopy = new Tile[state.length][state[0].length];
        for (int x = 0; x < deepCopy.length; x++) {
            for (int y = 0; y < deepCopy[x].length; y++) {
                deepCopy[x][y] = state[x][y];
            }
        }
        return deepCopy;
    }
}
