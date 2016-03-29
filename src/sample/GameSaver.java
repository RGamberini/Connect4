package sample;

import sample.states.BoardState;

import java.io.*;

/**
 * Created by Nick on 3/29/2016.
 */
public class GameSaver {
    public static boolean saveGame(BoardState state, File output) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            StringBuilder sb = new StringBuilder();
            Tile[][] stateCopy = state.getState();
            sb.append(state.turn).append("\n");
            for (int y = 0; y < stateCopy[0].length; y++) {
                for (int x = 0; x < stateCopy.length; x++) {
                    sb.append(stateCopy[x][y]).append(" ");
                }
                sb.append("\n");
            }
            writer.write(sb.toString());
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BoardState loadGame(File input) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(input));
            Tile currentTurn = Tile.valueOf(reader.readLine());
            Tile[][] state = new Tile[Connect4Board.COLUMNS][Connect4Board.ROWS];
            for (int y = 0; y < state[0].length; y++) {
                String[] line = reader.readLine().split(" ");
                for (int x = 0; x < state.length; x++) {
                    state[x][y] = Tile.valueOf(line[x]);
                }
            }
            return new BoardState(state, currentTurn);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidBoardException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.err.println("Improperly Formatted Board");
        }
        return null;
    }
}
