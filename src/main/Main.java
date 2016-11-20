package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import main.Dijkstra.Edge;

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

    
/**
 * Find the best path
 * @param args
 * @throws IOException
 */
    public static void main(String[] args) throws IOException {
        int[] source_target = new int[2];
        int[] graph = GraphReader.seeBMPImage("map.png", source_target);
        double[] radiation;
        // Read the file of radiation if exists
        File f = new File("radiation.txt");
        if (f.exists() && !f.isDirectory()) {
            radiation = new double[size * size];
            Scanner scanner = new Scanner(f);
            scanner.useLocale(Locale.ENGLISH);
            int i = 0;
            while (scanner.hasNextDouble()) {
                radiation[i++] = scanner.nextDouble();
            }
        } else {
            radiation = calculateRadiation(graph, "radiation.txt");
        }

        // Init
        Dijkstra dijkstra = new Dijkstra(
                graph, radiation, size, source_target[0], source_target[1]);
        double[] stats = new double[2];
        
        final int MAX_PATH_LENGTH = 1290;

        Set<Edge> excluded = new HashSet<>();
        ArrayList<Integer> p = new ArrayList<>();
        ArrayList<Integer> bestPath = null;
        int minRad = Integer.MAX_VALUE;
        int minPath = Integer.MAX_VALUE;
        int stopRange = 400;
        double prPath = Double.POSITIVE_INFINITY;
        double prRad = Double.POSITIVE_INFINITY;
        int counter = 0;
        
        // Search
        for (int i = 0; p != null; i++) {
            System.out.println(i);
            dijkstra.clear();
            p = dijkstra.findRoute(
                    stats, 1, excluded);
            if (p == null) {
                System.out.println("path is not found");
            } else {
                
                for (int j = p.size() - stopRange; j > stopRange; j--) {
                    Edge e = new Edge(p.get(j), p.get(j - 1));
                    /*System.out.println(e);*/
                    excluded.add(e);
                }
            }
            
            // Not allow infinit searching, expanding restricted paths
            if (prPath == stats[0] && prRad == stats[1]) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter >= 10) {
                stopRange -= 100;
            }
            prPath = stats[0];
            prRad = stats[1];
            
            // Remember the best path
            if (stats[0] <= MAX_PATH_LENGTH) {
                if (minRad > stats[1]) {
                    minPath = (int) stats[0];
                    minRad = (int) stats[1];
                    bestPath = p;
                }
            }
            System.out.println("Length : " + stats[0] + " Radiation : " + (int) stats[1]);
            if (p != null) {
                GraphWriter.writeBMPImage("visited_paths.bmp", p, "visited_paths.bmp");
            }
        }
        
        System.out.println("BEST PATH: ");
        System.out.println("Length : " + minPath + " Radiation : " + minRad);
        // The best -> Length : 1084 Radiation : 87890104
        
        int estimate = 1;
        
        // Draw path
        GraphWriter.writeBMPImage("map.png", bestPath, "path.bmp");
        
        // Draw radiation
        GraphWriter.writeRadiation("map.png", radiation, "radiation.bmp", graph);
    }

    
    
    public static double[] calculateRadiation(int[] graph, String filename) throws IOException {
        double[] radiation = buildRadTable(graph, size);
        BufferedWriter outputWriter;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < radiation.length; i++) {
            outputWriter.write(radiation[i] + " ");
        }
        outputWriter.flush();
        outputWriter.close();
        return radiation;
    }
    
    
    /**
     * Computes the radiation in each point (pixel)
     * @param array
     * @param size
     * @return
     */
    public static double[] buildRadTable(int[] array, int size) {
        double[] radiation = new double[size * size];
        for (int i = 0; i < size * size; i++) {
            if (array[i] == ORANGECELL) {
                for (int j = 0; j < size * size; j++) {
                    /*radiation[j] += Math.floor(Math.pow(10, 6) 
                            * Math.pow(Math.E, - Main.distance(i, j) / 2));*/
                    radiation[j] += Math.floor(Math.pow(10, 5) 
                            / Math.pow(Main.distance(i, j), 2));
                }
            }
        }
        return radiation;
    }

}
