package sample.test;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import sample.AI.MonteCarloAI;
import sample.AI.MonteCarloNode;
import sample.Connect4Board;
import sample.Tile;
import sample.states.BoardState;

import java.text.DecimalFormat;
import java.util.Iterator;

/**
 * Created by Nick on 4/19/2016.
 */
public class MonteCarloTest {
    public static void main(String[] args) throws InterruptedException {
        BoardState boardState = new BoardState();
        DecimalFormat df = new DecimalFormat("##.##");
        MonteCarloNode node = new MonteCarloNode(null, boardState, Tile.PLAYER2);
        node.expand();
        Thread[] threadArray = new Thread[node.children.size()];
        int c = 0;
        for (MonteCarloNode child : node.children.values()) {
            Thread thread = new Thread(() -> {
                for (int i = 0; i < 80000; i++) {
                    child.plays++;
                    child.wins += child.simulate();
                }
            });
            thread.start();
            threadArray[c] = thread;
            c++;
        }
        for (Thread thread : threadArray) thread.join();
        System.out.print("{(");
        for (MonteCarloNode child: node.children.values()) {
            System.out.print(df.format(child.selectionFunction()) + " ");
            System.out.print(child.wins + ":" + child.plays + "), (");
        }
        System.out.println("}");
    }
}
