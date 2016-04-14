package sample;

/**
 * Obvious exception for if the BoardState is illegal.
 */
public class InvalidBoardException extends Exception {
    public InvalidBoardException(String s) {
        super(s);
    }
}
