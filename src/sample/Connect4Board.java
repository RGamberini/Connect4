package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import sample.AI.AI;
import sample.AI.MinimaxAI;
import sample.AI.MonteCarloAI;
import sample.states.BoardState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Mostly immutable Connect4 Board
 */
public class Connect4Board {
    public static final int COLUMNS = 7, ROWS = 6;
    public static final ArrayList<Tile> turnOrder = new ArrayList<>();
    private final Map<Tile, Player> players;
    public final BooleanProperty won;

    static {
        turnOrder.add(Tile.PLAYER1);
        turnOrder.add(Tile.PLAYER2);
    }
    public final ObjectProperty<BoardState> currentState;
    public Connect4Board(int players) {
        currentState = new SimpleObjectProperty<>(new BoardState());
        won = new SimpleBooleanProperty(false);
        this.players = new HashMap<>(turnOrder.size());
        for (int i = 0; i < turnOrder.size(); i++) {
            Tile tile = turnOrder.get(i);
            Player player;
            if (i < players)
                player = new Player(this, tile);
            else player = new MinimaxAI(this, tile);
            this.players.put(tile, player);
        }
    }
    public Point getTopCell(int x) { return currentState.get().getTopCell(x);}
    public Tile getCurrentTurn() { return currentState.get().turn;}
    public Tile getNextTurn() { return this.currentState.get().getNextTurn();}
    public Point[] getAllMoves() { return this.currentState.get().getAllMoves();}
    public Player getPlayer(Tile tile) { return this.players.get(tile);}
    public Map<Tile, Player> getPlayers() { return players;}

    public void set(Point p) {
        // Don't update won before changing the state to avoid breaking listeners on won
        boolean _won = false;
        BoardState nextState;
        try {
            nextState = currentState.get().set(p);
            if (nextState.winner != Tile.EMPTY) {
                nextState = new BoardState(nextState, nextState.winner);
                _won = true;
                System.out.println("WON GAME " + nextState.winner);
            } else if(nextState.getAllMoves().length < 1) {
                nextState = new BoardState(nextState, Tile.EMPTY);
                _won = true;
            }
        } catch (InvalidBoardException e) {
            e.printStackTrace();
            System.err.println("Something went wrong!");
            return;
        }
        currentState.setValue(nextState);
        if (_won) won.set(true);
    }

    public synchronized Tile get(Point p) {
        return currentState.get().get(p);
    }
}
