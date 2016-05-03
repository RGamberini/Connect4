package sample;

import sample.AI.MinimaxAI;
import sample.AI.MonteCarlo.MonteCarloAI;
import sample.AI.NetworkPlayer;
import sample.AI.PlayerType;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Nick on 5/2/2016.
 */
public class NetworkConnect4Board extends Connect4Board {
    Connect4Socket onlinePlayer;
    public NetworkConnect4Board(PlayerType nonNetWorkPlayerType, Connect4Socket socket, boolean host) {
        super();
        socket.setBoard(this);
        Player[] players = new Player[turnOrder.size()];
        Tile tile = host ? Tile.PLAYER1:Tile.PLAYER2;
        switch (nonNetWorkPlayerType) {
            case HUMAN:
                players[0] = new Player(this, tile);
                break;
            case MINIMAX:
                players[0] = new MinimaxAI(this, tile);
                break;
            case MONTECARLO:
                players[0] = new MonteCarloAI(this, tile);
                break;
            case NETWORK:
                System.err.println("ERROR: Not how you do that");
                break;
        }
        try {
            players[1] = new NetworkPlayer(this, host ? Tile.PLAYER2:Tile.PLAYER1, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializePlayers(players);
    }
}
