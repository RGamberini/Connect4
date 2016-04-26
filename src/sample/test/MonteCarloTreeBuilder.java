package sample.test;

import sample.AI.MonteCarlo.MonteCarloNode;
import sample.AI.MonteCarlo.MonteCarloWorker;
import sample.Tile;
import sample.states.BoardState;

/**
 * Created by Nick on 4/26/2016.
 */
public class MonteCarloTreeBuilder {
    public static void main(String[] args) {
        BoardState boardState = new BoardState();
        MonteCarloWorker worker = new MonteCarloWorker(boardState, Tile.PLAYER2);
    }
}
