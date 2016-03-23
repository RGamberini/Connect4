package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.SimpleStyleableObjectProperty;
import sample.states.BoardState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rudy Gamberini on 2/25/2016.
 */
public class Connect4Board {
    public static final int COLUMNS = 7, ROWS = 6;
    public static final ArrayList<Tile> turnOrder = new ArrayList<>();
    private Map<Tile, Player> players;
    public BooleanProperty won;

    static {
        turnOrder.add(Tile.PLAYER1);
        turnOrder.add(Tile.PLAYER2);
    }
    public ObjectProperty<BoardState> currentState;
    public Connect4Board(int players) {
        currentState = new SimpleObjectProperty<>(new BoardState());
        won = new SimpleBooleanProperty(false);
        this.players = new HashMap<>(turnOrder.size());
        for (int i = 0; i < turnOrder.size(); i++) {
            Tile tile = turnOrder.get(i);
            Player player;
            if (i < players)
                player = new Player(this, tile);
            else
                player = new AI(this, tile);
            this.players.put(tile, player);
        }
    }
    public Point getTopCell(int x) { return currentState.get().getTopCell(x);}
    public Tile getCurrentTurn() { return currentState.get().turn;}
    public Tile getNextTurn() { return this.currentState.get().getNextTurn();}
    public Player getPlayer(Tile tile) { return this.players.get(tile);}
    public Map<Tile, Player> getPlayers() { return players;}

    public void set(Point p, Tile owner) {
        // Don't update won before changing the state to avoid breaking listeners on won
        boolean _won = false;
        BoardState nextState;
        try {
            nextState = new BoardState(currentState.get(), getNextTurn());
            nextState.set(p, owner);
            if (nextState.checkForGameOver()) {
                nextState = new BoardState(nextState, this.currentState.get().turn);
                _won = true;
                System.out.println("WON GAME");
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

    public Tile get(Point p) {
        return currentState.get().get(p);
    }
}
