package sample.AI;

import java.awt.*;

/**
 * Created by Nick on 3/27/2016.
 */
class Move {
    public int value;
    public Point space;

    public Move(Point space, int value) {
        this.space = space;
        this.value = value;
    }
}
