package sample.test;

import sample.AI.MonteCarlo.MonteCarloNode;
import sample.Tile;
import sample.states.BoardState;

import java.util.Arrays;

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

        MonteCarloNode _node = new MonteCarloNode(null, boardState, Tile.PLAYER2);
        _node.deserialize(serializedNode);
        double[][] _serializedNode = _node.serialize();
        for (int i = 0; i < serializedNode.length; i++) {
            double[] playWinPair = serializedNode[i];
            System.out.print("Plays: " + playWinPair[0] + " Wins: " + playWinPair[1]);
            double[] _playWinPair = _serializedNode[i];
            System.out.print("\t" + "Plays: " + _playWinPair[0] + " Wins: " + _playWinPair[1]);
            System.out.println("\t" + (Arrays.equals(playWinPair, _playWinPair)));
        }
    }
}
