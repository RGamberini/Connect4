package sample.AI;

import java.awt.*;

/**
 * For use by the Minimax algorithm bundles together a move and that moves value.
 */
class Move {
    public double value;
    public final Point space;

    public Move(Point space, double value) {
        this.space = space;
        this.value = value;
    }
}
