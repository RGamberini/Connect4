package sample;

import sample.AI.PlayerType;
import sample.states.BoardState;

import java.io.*;

/**
 * Static class that takes files in and out and returns Connect4Boards.
 */
public class GameSaver {
    public static boolean saveGame(Connect4Board board, File output) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(output));
            StringBuilder sb = new StringBuilder();
            BoardState state = board.currentState.get();
            Tile[][] stateCopy = state.getState();
            boolean firstIteration = true;
            for (PlayerType playerType : board.getAITypes()) {
                if (!firstIteration) sb.append(" ");
                sb.append(playerType);
                firstIteration = false;
            }
            sb.append("\n");
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

    public static Connect4Board loadGame(File input) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(input));

            String[] split = reader.readLine().split(" ");
            PlayerType[] playerTypes = new PlayerType[split.length];
            for (int i = 0; i < split.length; i++) {
                playerTypes[i] = PlayerType.valueOf(split[i]);
            }

            Tile currentTurn = Tile.valueOf(reader.readLine());
            Tile[][] state = new Tile[Connect4Board.COLUMNS][Connect4Board.ROWS];
            for (int y = 0; y < state[0].length; y++) {
                String[] line = reader.readLine().split(" ");
                for (int x = 0; x < state.length; x++) {
                    state[x][y] = Tile.valueOf(line[x]);
                }
            }
            BoardState newState = new BoardState(state, currentTurn);
            Connect4Board newBoard = new Connect4Board(playerTypes);
            newBoard.currentState.set(newState);
            return newBoard;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidBoardException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.err.println("Improperly Formatted Board");
        }
        return null;
    }
}
