package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import sample.AI.AIType;
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
    private final Map<Tile, Player> players;
    public final BooleanProperty won;

    static {
        turnOrder.add(Tile.PLAYER1);
        turnOrder.add(Tile.PLAYER2);
    }
    public final ObjectProperty<BoardState> currentState;
    public Connect4Board(AIType[] playerAITypes) {
        currentState = new SimpleObjectProperty<>(new BoardState());
        won = new SimpleBooleanProperty(false);
        this.players = new HashMap<>(turnOrder.size());
        for (int i = 0; i < turnOrder.size(); i++) {
            Tile tile = turnOrder.get(i);
            Player player = null;
            switch (playerAITypes[i]) {
                case HUMAN:
                    player = new Player(this, tile);
                    break;
                case MINIMAX:
                    player = new MinimaxAI(this, tile);
                    break;
                case MONTECARLO:
                    player = new MonteCarloAI(this, tile);
                    break;
            }
            this.players.put(tile, player);
        }
    }
    public Point getTopCell(int x) { return currentState.get().getTopCell(x);}
    public Tile getCurrentTurn() { return currentState.get().turn;}
    public Tile getNextTurn() { return this.currentState.get().getNextTurn();}
    public Point[] getAllMoves() { return this.currentState.get().getAllMoves();}
    public Player getPlayer(Tile tile) { return this.players.get(tile);}
    public Map<Tile, Player> getPlayers() { return players;}
    public AIType[] getAITypes() {
        AIType[] result = new AIType[players.size()];
        for (int i = 0; i < Connect4Board.turnOrder.size(); i++) {
            Tile turn = Connect4Board.turnOrder.get(i);
            result[i] = this.players.get(turn).getAIType();
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
