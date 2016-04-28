package sample.test;

import sample.AI.MonteCarlo.MonteCarloNode;
import sample.AI.MonteCarlo.MonteCarloWorker;
import sample.Connect4Board;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Created by Rudy Gamberini on 4/25/2016.
 */
public class MonteCarloSerializeTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        BoardState boardState = new BoardState();
        Point[] allMoves = boardState.getAllMoves();
        MonteCarloNode node = new MonteCarloNode(null, boardState, Tile.PLAYER1);
        try {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream("MonteCarlo.data"));
            double[][] serializedNode = (double[][])oos.readObject();
            node.deserialize(serializedNode);
            oos.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ignored) {
            node.expand();
        }
        Thread[] threads = new Thread[allMoves.length];
        for (int i = 0; i < allMoves.length; i++) {
            Point move = allMoves[i];
            MonteCarloWorker worker = new MonteCarloWorker(node.children.get(move).initialState, Tile.PLAYER1);
            worker.initialNode = node.children.get(move);
            threads[i] = new Thread(worker);
            threads[i].setDaemon(true);
            threads[i].start();
        }
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        for (Thread workerThread: threads) {
            workerThread.interrupt();
        }
        double[][] serializedNode = node.serialize();

//        MonteCarloNode _node = new MonteCarloNode(null, boardState, Tile.PLAYER1);
//        _node.deserialize(serializedNode);
//        double[][] _serializedNode = _node.serialize();
        for (int i = 0; i < Connect4Board.COLUMNS * Connect4Board.COLUMNS; i++) {
            double[] playWinPair = serializedNode[i];
//            if (playWinPair[0] > 20000) {
                System.out.println("Plays: " + playWinPair[0] + " Wins: " + playWinPair[1]);
//                double[] _playWinPair = _serializedNode[i];
//                System.out.print("\t" + "Plays: " + _playWinPair[0] + " Wins: " + _playWinPair[1]);
//                System.out.println("\t" + (Arrays.equals(playWinPair, _playWinPair)));
//            }
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("MonteCarlo.data"));
        oos.writeObject(serializedNode);
        oos.close();
    }
}
