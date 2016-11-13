package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphWriter {
    public static void writeBMPImage(String BMPFileName, ArrayList<Integer> path, String s) throws IOException {
        BufferedImage bi = ImageIO.read(new File(BMPFileName));
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        BufferedImage output = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
        for (int x : path)
            output.setRGB(Main.delinearize(x)[0] - 1, Main.delinearize(x)[1] - 1, Color.BLUE.getRGB());
        ImageIO.write(output, "bmp", new File(s));

    }

}
