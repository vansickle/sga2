package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static main.Main.*;

public class GraphReader {
    public static int ORANGE = -32985;
    public static int RED = -1237980;

    public static int[] seeBMPImage(String BMPFileName, int[] source_target) throws IOException {
        BufferedImage image = ImageIO.read(new File(BMPFileName));
        int xx, yy;
        int size = image.getHeight() + 2;
        int[] array2D = new int[size * size];

        for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
            for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
                int color = image.getRGB(xPixel, yPixel);
                xx = xPixel + 1;
                yy = yPixel + 1;
                if (color == Color.white.getRGB())
                    array2D[xx * size + yy] = WHITECELL;
                else if (color == ORANGE)
                    array2D[xx * size + yy] = ORANGECELL;
                else if (color == Color.black.getRGB())
                    array2D[xx * size + yy] = BLACKCELL;
                else if (color == RED) {
                    source_target[0] = xx * size + yy;
                    array2D[xx * size + yy] = WHITECELL;
                } else {
                    source_target[1] = xx * size + yy;
                    array2D[xx * size + yy] = WHITECELL;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            array2D[i] = -1;
            array2D[size * i] = -1;
            array2D[size * (i + 1) - 1] = -1;
            array2D[size * (size - 1) + i] = -1;
        }
        return array2D;
    }
}
