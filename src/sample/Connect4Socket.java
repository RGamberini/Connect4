package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Nick on 5/2/2016.
 */
public class Connect4Socket {
    private Connect4Board board;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean host;

    public Connect4Socket(Socket socket, boolean host) throws IOException {
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        this.host = host;
    }

    public void setBoard(Connect4Board board) {
        this.board = board;
    }

    public Task<Void> startup() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _startup();
                return null;
            }
        };
    }

    private void _startup() {
        if (host) {
            try {
                writeMessage('S');
                System.out.println("Waiting for confirmation from client");
                readMessage('A');
                System.out.println("Received confirmation from client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Waiting for confirmation from server");
            readMessage('S');
            System.out.println("Responding to server");
            try {
                writeMessage('A');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readMessage(String toRead) {
        toRead.chars().forEach((i) -> {
            char charToRead = (char) i;
            if (isValid(charToRead)) {
                try {
                    char c = inputStream.readChar();
                    if (c != charToRead) {
                        System.err.println("ERROR: UNEXPECTED MESSAGE GOT " + c + " EXPECTED " + charToRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("ERROR: ILLEGAL CHARACTER " + charToRead);
            }
        });
    }

    public void readMessage(char toRead) {
        readMessage(toRead + "T");
    }

    public char[] readMessage(int n) {
        char[] result = new char[n];
        for(int i = 0; i < n; i++) {
            try {
                result[i] = inputStream.readChar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean isValid(char c) {
        if (c == 'S' || c == 'A' || c == 'M' || c == 'T') return true;
        int intC = Character.getNumericValue(c);
        return intC > 0 && intC < Connect4Board.COLUMNS;
    }

    private void writeMessage(char c) throws IOException {
        if (isValid(c)) {
            outputStream.writeChar(c);
            outputStream.writeChar('T');
            outputStream.flush();
        } else {
            throw new IOException("ERROR: ILLEGAL CHARACTER " + c);
        }
    }

    private void writeMessage(String s) throws IOException {
        s.chars().forEach((i) -> {
            char c = (char) i;
            if (isValid(c)) {
                try {
                    outputStream.writeChar(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("ERROR: ILLEGAL CHARACTER " + c);
            }
        });
        outputStream.writeChar('T');
        System.out.println("Flushing outputstream");
        outputStream.flush();
    }

    public void sendMove(Point nextMove) {
        Thread thread = new Thread(() -> {
            try {
                writeMessage("M" + nextMove.x);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public Task<Point> getMove() {
        return new Task<Point>() {
            @Override
            protected Point call() throws Exception {
                char[] move = readMessage(3);
                int column = Character.getNumericValue(move[1]);
                return board.getTopCell(column);
            }
        };
    }
}
