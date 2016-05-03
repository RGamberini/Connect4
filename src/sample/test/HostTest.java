package sample.test;

import sample.AI.PlayerType;
import sample.Connect4Board;
import sample.Connect4Socket;
import sample.NetworkConnect4Board;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Nick on 5/3/2016.
 */
public class HostTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(44444);
        Socket socket = serverSocket.accept();
//        NetworkConnect4Board board = new NetworkConnect4Board(PlayerType.HUMAN, socket, true);
    }
}
