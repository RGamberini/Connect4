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
    public SimpleObjectProperty<Player> currentPlayer;
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
        currentPlayer = new SimpleObjectProperty<>(this.players.get(Tile.PLAYER1));
        this.currentState.addListener(this::updatePlayer);
    }
    public Point getTopCell(int x) { return currentState.get().getTopCell(x);}
    public Tile getCurrentTurn() { return currentState.get().turn;}
    public Tile getNextTurn() { return this.currentState.get().getNextTurn();}
    public Player getPlayer(Tile tile) { return  this.players.get(tile);}

    public void set(Point p, Tile owner) {
        BoardState nextState;
        try {
            nextState = new BoardState(currentState.get(), getNextTurn());
            nextState.set(p, owner);
            if (nextState.checkForGameOver()) {
                nextState = new BoardState(nextState, this.currentState.get().turn);
                won.set(true);
            }
        } catch (InvalidBoardException e) {
            e.printStackTrace();
            System.err.println("Something went wrong!");
            return;
        }
        currentState.setValue(nextState);
    }

    public void updatePlayer(Observable o, BoardState oldVal, BoardState newVal) {
        this.currentPlayer.set(players.get(newVal.turn));
    }

    public Tile get(Point p) {
        return currentState.get().get(p);
    }
}
