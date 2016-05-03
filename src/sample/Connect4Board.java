package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import sample.AI.PlayerType;
import sample.AI.MinimaxAI;
import sample.AI.MonteCarlo.MonteCarloAI;
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
    protected final Map<Tile, Player> players;
    public final BooleanProperty won;

    static {
        turnOrder.add(Tile.PLAYER1);
        turnOrder.add(Tile.PLAYER2);
    }
    public final ObjectProperty<BoardState> currentState;


    protected Connect4Board() {
        currentState = new SimpleObjectProperty<>(new BoardState());
        won = new SimpleBooleanProperty(false);
        this.players = new HashMap<>(turnOrder.size());

    }
    protected void initializePlayers(Player[] players) {
        for (Player player: players) this.players.put(player.tile, player);
    }

    public Connect4Board(PlayerType[] playerTypes) {
        this();
        Player[] players = new Player[turnOrder.size()];
        for (int i = 0; i < players.length; i++) {
            Tile tile = turnOrder.get(i);
            switch (playerTypes[i]) {
                case HUMAN:
                    players[i] = new Player(this, tile);
                    break;
                case MINIMAX:
                    players[i] = new MinimaxAI(this, tile);
                    break;
                case MONTECARLO:
                    players[i] = new MonteCarloAI(this, tile);
                    break;
                case NETWORK:
                    System.err.println("ERROR: Should be using a NetworkConnect4Board!");
                    break;
            }
        }
        initializePlayers(players);
    }
    public Point getTopCell(int x) { return currentState.get().getTopCell(x);}
    public Tile getCurrentTurn() { return currentState.get().turn;}
    public Tile getNextTurn() { return this.currentState.get().getNextTurn();}
    public Point[] getAllMoves() { return this.currentState.get().getAllMoves();}
    public Player getPlayer(Tile tile) { return this.players.get(tile);}
    public Map<Tile, Player> getPlayers() { return players;}
    public PlayerType[] getAITypes() {
        PlayerType[] result = new PlayerType[players.size()];
        for (int i = 0; i < Connect4Board.turnOrder.size(); i++) {
            Tile turn = Connect4Board.turnOrder.get(i);
            result[i] = this.players.get(turn).getPlayerType();
        }
        return result;
    }

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
