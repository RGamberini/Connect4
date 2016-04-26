package sample.test;

import sample.AI.MonteCarlo.MonteCarloNode;
import sample.AI.MonteCarlo.MonteCarloWorker;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Rudy Gamberini on 4/25/2016.
 */
public class MonteCarloSerializeTest {
    public static void main(String[] args) throws InterruptedException {
        BoardState boardState = new BoardState();
        Point[] allMoves = boardState.getAllMoves();
        MonteCarloWorker[] workers = new MonteCarloWorker[allMoves.length];
        Thread[] threads = new Thread[allMoves.length];
        for (int i = 0; i < allMoves.length; i++) {
            Point move = allMoves[i];
            MonteCarloWorker worker = new MonteCarloWorker(boardState.set(move), Tile.PLAYER2);
            threads[i] = new Thread(worker);
            threads[i].setDaemon(true);
            threads[i].start();
        }
        Thread.sleep(5000);
        for (Thread workerThread: threads) {
            workerThread.interrupt();
        }
        MonteCarloNode node = new MonteCarloNode(null, boardState, Tile.PLAYER2);
        double[][] serializedNode = node.ch;

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
