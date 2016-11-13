package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static main.Main.*;

public class Dijkstra {
    private static double meanRad = 1079616754. / 1295.7005768508893;
    public int[] array;
    private double[] radiation;
    private int start, goal;
    private Set<Integer> closedSet;
    private PriorityQueue<Integer> openSet;
    private double[] gScore;
    private int[] cameFrom;
    private int size;


    public Dijkstra(int[] array, double[] radiation, int size, int start, int goal) {
        this.array = array;
        this.radiation = radiation;
        this.start = start;
        this.goal = goal;
        this.size = size;
        closedSet = new HashSet<>();
        openSet = new PriorityQueue<>((o1, o2) -> {
            if (gScore[o1] - gScore[o2] > 0)
                return 1;
            else if (gScore[o1] - gScore[o2] < 0)
                return -1;
            else return 0;
        });
        openSet.add(start);
        cameFrom = new int[size * size];
        gScore = new double[size * size];
        for (int i = 0; i < size * size; i++) {
            gScore[i] = Double.MAX_VALUE;
        }
        gScore[this.start] = 0;
    }

    public void clear() {
        openSet.clear();
        closedSet.clear();
        openSet.add(start);
        cameFrom = new int[size * size];
        gScore = new double[size * size];
        for (int i = 0; i < size * size; i++) {
            gScore[i] = Double.MAX_VALUE;
        }
        gScore[this.start] = 0;
    }

    public ArrayList<Integer> findRoute(double[] statistics, double alpha) {
        int i = 0;
        while (!openSet.isEmpty()) {
            int current = openSet.poll();
            if (current == goal)
                return reconstructPath(statistics);
            closedSet.add(current);
            for (int x : offsets) {
                int neighbor = x + current;
//                if (closedSet.contains(neighbor)  || array[current] == -1)
                if (array[neighbor] != WHITECELL || closedSet.contains(neighbor))
                    continue;
                double tentative_gScore = gScore[current] + radiation[neighbor] * alpha +
                        (1 - alpha) * meanRad * chebyshevDistance(current, neighbor); //radiation[neighbor];
                if (tentative_gScore >= gScore[neighbor])
                    continue;
                gScore[neighbor] = tentative_gScore;
                cameFrom[neighbor] = current;
                openSet.add(neighbor);
            }
        }
        return null;
    }


    private ArrayList<Integer> reconstructPath(double[] statistics) {
        ArrayList<Integer> totalPath = new ArrayList<>();
        double length = 0;
        int rad = 0;
        totalPath.add(goal);
        int current = goal;
        while (current != start) {
            length += distance(current, cameFrom[current]);
            rad += radiation[current];
            current = cameFrom[current];
            totalPath.add(current);
        }
        rad += radiation[start];
        statistics[0] = length;
        statistics[1] = rad;
        return totalPath;
    }
}


