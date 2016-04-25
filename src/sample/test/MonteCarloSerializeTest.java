package sample.test;

import sample.AI.MonteCarloNode;
import sample.Tile;
import sample.states.BoardState;

/**
 * Created by Rudy Gamberini on 4/25/2016.
 */
public class MonteCarloSerializeTest {
    public static void main(String[] args) {
        BoardState boardState = new BoardState();
        MonteCarloNode node = new MonteCarloNode(null, boardState, Tile.PLAYER2);
        node.expand();
        for (int i = 0; i < 100; i++) {
            node.plays++;
            node.wins += node.simulate();
            for (MonteCarloNode child: node.children.values()) {
                child.plays++;
                child.wins += child.simulate();
            }
        }
        double[][] serializedNode = node.serialize();
        for (double[] playWinPair: serializedNode) {
            System.out.println("Plays: " + playWinPair[0] + " Wins: " + playWinPair[1]);
        }
    }
}
