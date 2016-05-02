package sample.test;

import sample.AI.MonteCarlo.MonteCarloWorker;
import sample.Tile;
import sample.states.BoardState;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nick on 4/30/2016.
 */
public class MonteCarloTest {
    public static void main(String[] args) throws InterruptedException {
        BoardState initialState = new BoardState();
        MonteCarloWorker worker = new MonteCarloWorker(initialState, Tile.PLAYER1);
        HashMap<BoardState, AtomicInteger> plays = new HashMap<>();
        HashMap<BoardState, AtomicInteger> wins = new HashMap<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("montecarlo.data"));
            plays = (HashMap<BoardState, AtomicInteger>) ois.readObject();
            wins = (HashMap<BoardState, AtomicInteger>) ois.readObject();
            worker.plays = plays;
            worker.wins = wins;
            ois.close();
        } catch (IOException | ClassNotFoundException ignored) {}
        Thread thread = new Thread(worker);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        thread.interrupt();

        for (Point move: initialState.getAllMoves()) {
            BoardState nextState = initialState.set(move);
            System.out.println("Plays: " + worker.plays.get(nextState) + " Wins: " + worker.wins.get(nextState));
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("montecarlo.data"));
            oos.writeObject(worker.plays);
            oos.writeObject(worker.wins);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
