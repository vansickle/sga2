package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static final int WHITECELL = 0;
    public static final int BLACKCELL = -1;
    public static final int ORANGECELL = 1;
    public static int size = 702;
    public static int[] offsets = new int[]{-size, -1, 1, size, -size - 1, -size + 1, size - 1, size + 1};

    public static int linearize(int... p) {
        return p[0] * size + p[1];
    }

    public static int[] delinearize(int x) {
        return new int[]{x / size, x % size};
    }

    public static double distance(int a, int b) {
        int h = delinearize(a)[0] - delinearize(b)[0],
                w = delinearize(a)[1] - delinearize(b)[1];
        return Math.sqrt(h * h + w * w);
    }

    public static double buildDistance(int a, int b) {
        int h = delinearize(a)[0] - delinearize(b)[0],
                w = delinearize(a)[1] - delinearize(b)[1];
        return h * h + w * w;
    }

    public static double chebyshevDistance(int a, int b) {
        int h = Math.abs(delinearize(a)[0] - delinearize(b)[0]),
                w = Math.abs(delinearize(a)[1] - delinearize(b)[1]);
        return (h + w) + (Math.sqrt(2) - 2) * Math.min(h, w);
    }

    //1287.3229432149765 1056517886 min path
    public static void main(String[] args) throws IOException {
        int[] source_target = new int[2];
        int[] graph = GraphReader.seeBMPImage("map.png", source_target);
        double[] radiation;
        //read file if exists
        File f = new File("radiation.txt");
        if (f.exists() && !f.isDirectory()) {
            radiation = new double[size * size];
            Scanner scanner = new Scanner(f);
            scanner.useLocale(Locale.ENGLISH);
            int i = 0;
            while (scanner.hasNextDouble()) {
                radiation[i++] = scanner.nextDouble();
            }
        } else
            radiation = calculateRadiation(graph, "radiation.txt");

        Dijkstra dijkstra = new Dijkstra(graph, radiation, size, source_target[0], source_target[1]);
        double[] stats = new double[2];
        final int MAX_PATH_LENGTH = 1250;

//estimates
//        for (int i = 0; i < 100; i++) {
//            dijkstra.clear();
//            dijkstra.findRoute(stats, i*0.01);
//            System.out.println(stats[0] + " " + (int) stats[1]);
//        }

        //Bisection method
        double a = 0, b = 1, c = 0;
        double pathLengthA = 0, pathLengthB = 0, pathLengthC;
        double h = 1e-4;
        dijkstra.clear();
        dijkstra.findRoute(stats, a);
        pathLengthA = stats[0] - MAX_PATH_LENGTH;
        dijkstra.clear();
        dijkstra.findRoute(stats, b);
        pathLengthB = stats[0] - MAX_PATH_LENGTH;
        double best = Double.MAX_VALUE;
        double estimate = 0;
        while (Math.abs(a - b) > h) {
            c = (a + b) / 2;
            dijkstra.clear();
            dijkstra.findRoute(stats, c);
            pathLengthC = stats[0] - MAX_PATH_LENGTH;
            if (pathLengthA * pathLengthC < 0)
                b = c;
            else
                a = c;
            System.out.println(stats[0] + " " + stats[1] + " " + c);
            if (Math.abs(pathLengthC) < best && pathLengthC < 0) {
                estimate = c;
                best = Math.abs(pathLengthC);
            }
        }

        //print pass
        dijkstra.clear();
        ArrayList<Integer> path = dijkstra.findRoute(stats, estimate);
        System.out.println("Length : " + stats[0] + " Radiation : " + (int) stats[1]);
        GraphWriter.writeBMPImage("map.png", path, "path.bmp");
    }

    public static double[] calculateRadiation(int[] graph, String filename) throws IOException {
        double[] radiation = GraphBuilder.build(graph, size);
        BufferedWriter outputWriter;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < radiation.length; i++) {
            outputWriter.write(radiation[i] + " ");
        }
        outputWriter.flush();
        outputWriter.close();
        return radiation;
    }

}
