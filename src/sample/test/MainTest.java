package sample.test;

import sample.ImageParser;
import sample.InvalidBoardException;
import sample.Tile;
import sample.states.BoardState;

/**
 * Original test class for making sure all of my win conditions work, the reason this uses ImageParser is because I didn't
 * yet have the Save / Load for BoardStates working yet.
 */
public class MainTest {
    public static void main(String[] args) throws InvalidBoardException {
        Tile[][] horizontalwin = ImageParser.parseImage("tests/horizontalwin");
        Tile[][] verticalwin = ImageParser.parseImage("tests/verticalwin");
        Tile[][] diagonalwin1 = ImageParser.parseImage("tests/diagonalwin1");
        Tile[][] diagonalwin2 = ImageParser.parseImage("tests/diagonalwin2");
        Tile[][] floating = ImageParser.parseImage("tests/floating");

        System.out.println("Should be 7 " + horizontalwin.length + " Should be 6 " + horizontalwin[0].length);

        System.out.print("Testing for horizontal win: ");
        if (new BoardState(horizontalwin, Tile.PLAYER1).winner != Tile.EMPTY)
            System.out.println("Passed!");
        else
            System.out.println("Failed!");

        System.out.print("Testing for vertical win: ");
        if (new BoardState(verticalwin, Tile.PLAYER1).winner != Tile.EMPTY)
            System.out.println("Passed!");
        else
            System.out.println("Failed!");

        System.out.print("Testing for diagonal win: ");
        if (new BoardState(diagonalwin1, Tile.PLAYER1).winner != Tile.EMPTY && new BoardState(diagonalwin2, Tile.PLAYER1).winner != Tile.EMPTY)
            System.out.println("Passed!");
        else
            System.out.println("Failed!");

        System.out.print("Testing for floating tile error: ");
        try {
            new BoardState(floating, Tile.PLAYER1);
            System.out.println("Failed!");
        } catch (InvalidBoardException e) {
            System.out.println("Passed!");
        }
    }
}
