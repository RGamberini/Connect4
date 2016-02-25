package sample;

import sample.states.BoardState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Parses images into Connect4 boards and the reverse.
 */
public class ImageParser {
    public static Tile[][] parseImage(String imagename) {
        return parseImage(new File("resources\\" + imagename + ".bmp"));
    }

    public static Tile[][] parseImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERROR: INVALID IMAGENAME");
        }

        Tile[][] result = new Tile[image.getWidth()][image.getHeight()];

        for (int x = 0; x < result.length; x++) {
            for (int y = 0; y < result[x].length; y++) {
                int color = image.getRGB(x, y);
                if (color == Color.WHITE.getRGB())
                    result[x][y] = Tile.EMPTY;
                else if (color == Color.BLACK.getRGB())
                        result[x][y] = Tile.PLAYER1;
                else
                    result[x][y] = Tile.PLAYER2;
            }
        }
        return result;
    }

    public static void saveImage(File file, boolean[][] state) {
        BufferedImage image = new BufferedImage(state.length, state.length, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < state.length; x++) {
            for (int y = 0; y < state[x].length; y++) {
                if (state[x][y])
                    image.setRGB(x, y, Color.BLACK.getRGB());
                else
                    image.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        try {
            ImageIO.write(image, "BMP", file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: INVALID IMAGE FILE TO SAVE");
        }
    }
}
