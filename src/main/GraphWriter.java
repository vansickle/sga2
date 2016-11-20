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
    /**
     * Draw the route on the image
     * @param BMPFileName
     * @param path
     * @param outFileName
     * @throws IOException
     */
    public static void writeBMPImage(
            String BMPFileName, ArrayList<Integer> path,
            String outFileName) throws IOException {
        BufferedImage bi = ImageIO.read(new File(BMPFileName));
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        BufferedImage output = new BufferedImage(
                cm, raster, isAlphaPremultiplied, null);
        for (int x : path)
            output.setRGB(Main.delinearize(x)[0] - 1,
                    Main.delinearize(x)[1] - 1, Color.BLUE.getRGB());
        ImageIO.write(output, "bmp", new File(outFileName));
    }
    
    /**
     * Draw the image of radiation spread.
     * @param BMPFileName
     * @param radiation
     * @param outFileName
     * @param graph
     * @throws IOException
     */
    public static void writeRadiation(
            String BMPFileName, double[] radiation,
            String outFileName, int[] graph) throws IOException {
        BufferedImage bi = ImageIO.read(new File(BMPFileName));
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        BufferedImage output = new BufferedImage(
                cm, raster, isAlphaPremultiplied, null);
        for (int i = 0; i < graph.length; i++) {
            double rad = radiation[i];
            int lvl = (int) rad / 50000;
            int color = Color.WHITE.getRGB();
            switch (lvl) {
                case 0: color = Color.MAGENTA.getRGB();
                break;
                case 1: color = Color.BLUE.getRGB();
                break;
                case 2: color = Color.CYAN.getRGB();
                break;
                case 3: color = Color.GREEN.getRGB();
                break;
                case 4: color = Color.YELLOW.getRGB();
                break;
                case 5: color = Color.PINK.getRGB();
                break;
                case 6: color = Color.RED.getRGB();
                break;
                default: color = Color.RED.getRGB();;
            }
            
            try {
                if (graph[i] == Main.WHITECELL) {
                    output.setRGB(Main.delinearize(i)[0] - 1,
                            Main.delinearize(i)[1] - 1, color);
                }
            } catch(Exception e) {
                // do nothing
            }
        }   
        ImageIO.write(output, "bmp", new File(outFileName));
    }
}
