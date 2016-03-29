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
        for(Tile key: Connect4Board.turnOrder) {
            if (runs.get(key) != null)
                runs.get(key).clear();
            else
                runs.put(key, new ArrayList<>());
        }

    }

    public BoardState(BoardState oldState, Tile turn) throws InvalidBoardException {
        if (isValidBoard(oldState.getState()))
            this.state = oldState.getState();
        this.runs.putAll(oldState.runs);
        this.turn = turn;
    }

    public BoardState(Tile[][] initialState, Tile turn) throws InvalidBoardException {
        if (isValidBoard(initialState))
            this.state = initialState;
        this.turn = turn;
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

    /**
     * Returns all winnable runs on the board
     */
    private void updateRuns() {
        for(Tile key: Connect4Board.turnOrder) {
            if (runs.get(key) != null)
                runs.get(key).clear();
            else
                runs.put(key, new ArrayList<>());
        }

        /**
         * Directions we need to check for runs in
         */
        Point[] directions = new Point[]{
                // Vertical
                new Point(0, 1),
                // Horizontal
                new Point(1, 0),
                // Right and Down
                new Point(1, 1),
                // Right and Up
                new Point(1, -1)

        };
        ArrayList<Point> inARun = new ArrayList<>();
        for (Point direction: directions) {
            inARun.clear();
            for (int x = 0; x < state.length; x++) {
                for (int y = 0; y < state[x].length; y++) {
                    if (state[x][y] != Tile.EMPTY) {
                        // Keeping track of the free space on both ends of a run allows me to check if a run is winnable
                        int freeSpace = 0;
                        ArrayList<Point> run = new ArrayList<>();
                        Point origin = new Point(x, y);
                        if (!inARun.contains(origin)) {
                            // This bool is necessary because I always check the whole 4 tiles for open spaces
                            boolean continuous = true;
                            for (int r = 1; r <= 3; r++) {
                                int nextX = x + (r * direction.x);
                                int nextY = y + (r * direction.y);
                                if (nextX < state.length && nextX >= 0 && nextY < state[x].length && nextY >= 0) {
                                    Tile nextTile = state[nextX][nextY];
                                    if (state[x][y] == nextTile && continuous) {
                                        Point point = new Point(nextX, nextY);
                                        run.add(point);
                                    } else if (nextTile == Tile.EMPTY) {
                                        freeSpace++;
                                        continuous = false;
                                    }
                                    else break;
                                } else break;
                            }
                        }
                        if (run.size() > 0) {
                            /**
                             * This loop checks for whether the run is winnable by checking for free space on the
                             * opposite (left) end of the run.
                             */
                            for (int r = 1; r <= 4 - (run.size() + freeSpace); r++) {
                                int nextX = x + (r * direction.x * -1);
                                int nextY = y + (r * direction.y * -1);
                                if (nextX < state.length && nextX >= 0 && nextY < state[x].length && nextY >= 0) {
                                    Tile nextTile = state[nextX][nextY];
                                    if (nextTile == Tile.EMPTY) {
                                        freeSpace++;
                                    } else break;
                                } else break;
                            }
                        }
                        // If and only if the run is winnable will we add it to the list of runs
                        // We add 1 because the origin is not yet part of the run
                        if (run.size() > 0 && (run.size() + 1 + freeSpace) >= 4) {
                            run.add(0, origin);
                            //System.out.println(state[x][y] + " has a winnable run of length: " + run.size());
                            runs.get(state[x][y]).add(run.toArray(new Point[run.size()]));
                            for (Point runSegment: run)
                                inARun.add(runSegment);
                        }
                    }
                }
            }
        }
        //System.out.println("--------------------------------------------");
    }

    public ArrayList<Point[]> getRuns(Tile tile) {
        return this.runs.get(tile);
    }

    /**
     * The heuristic function for the state, returns 10^n where n is the length longest run of the current player
     * subtracted from the length of the longest run not owned by the current player.
     */
    public int getValue() {
        int currentTurnLongestRun = 0;
        int otherLongestRun = 0;
        for (Tile player : Connect4Board.turnOrder) {
            for (Point[] run : getRuns(player)) {
                if (player == turn) {
                    if (currentTurnLongestRun < run.length)
                        currentTurnLongestRun = (int) Math.pow(10, run.length);
                } else {
                    if (run.length > 3)
                        return Integer.MIN_VALUE;
                    if (otherLongestRun < run.length)
                        otherLongestRun = (int) Math.pow(10, run.length);
                }
            }
        }
        return currentTurnLongestRun + (otherLongestRun * -1);
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

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }
}
