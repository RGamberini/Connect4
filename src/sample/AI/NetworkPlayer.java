package sample.AI;

import com.sun.istack.internal.Nullable;
import javafx.concurrent.Task;
import sample.*;
import sample.AI.PlayerType;
import sample.states.BoardState;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Nick on 5/3/2016.
 */
public class NetworkPlayer extends AI {
    Connect4Socket socket;
    public NetworkPlayer(NetworkConnect4Board board, Tile tile, Connect4Socket socket) throws IOException {
        super(board, tile);
        this.socket = socket;
        this.AITurn(board.currentState.get(), board.currentState.get());
    }

    @Override
    protected void AITurn(BoardState oldVal, BoardState newVal) {
        if (oldVal != null && board.getPlayer(oldVal.turn) != null && board.getPlayer(oldVal.turn).getPlayerType() != PlayerType.NETWORK) {
            if (this.socket != null) {
                Point nextMove = getMove(oldVal, newVal);
                if (nextMove != null) this.socket.sendMove(nextMove);
            }
        }
        if (newVal.turn == tile && newVal.winner == Tile.EMPTY && newVal.getAllMoves().length > 0) {
            // RETRIEVE A MOVE FROM THE SOCKET
            //System.out.println("Should listen to a move here");
            if (this.socket != null) {
                Task<Point> listen = socket.getMove();
                listen.setOnSucceeded((event) -> board.set(listen.getValue()));
                new Thread(listen).start();
            }
        }
    }

    @Override
    public PlayerType getPlayerType() {
        return PlayerType.NETWORK;
    }

    private @Nullable Point getMove(BoardState oldVal, BoardState newVal) {
        if (oldVal != null)
            for (Point move: oldVal.getAllMoves())
                if (oldVal.get(move) != newVal.get(move))
                    return move;
        return null;
    }
}
