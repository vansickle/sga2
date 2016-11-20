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
    private PriorityQueue<Integer> pq;
    private double[] gScore;
    private int[] prev;
    private int size;


    public Dijkstra(int[] array, double[] radiation, int size, int start, int goal) {
        this.array = array;
        this.radiation = radiation;
        this.start = start;
        this.goal = goal;
        this.size = size;
        closedSet = new HashSet<>();
        pq = new PriorityQueue<>((o1, o2) -> {
            if (gScore[o1] - gScore[o2] > 0)
                return 1;
            else if (gScore[o1] - gScore[o2] < 0)
                return -1;
            else return 0;
        });
        pq.add(start);
        prev = new int[size * size];
        gScore = new double[size * size];
        for (int i = 0; i < size * size; i++) {
            gScore[i] = Double.MAX_VALUE;
        }
        gScore[this.start] = 0;
    }

    
    public void clear() {
        pq.clear();
        closedSet.clear();
        pq.add(start);
        prev = new int[size * size];
        gScore = new double[size * size];
        for (int i = 0; i < size * size; i++) {
            gScore[i] = Double.MAX_VALUE;
        }
        gScore[this.start] = 0;
    }
    
    
    /**
     * Founds the shortest path
     * @param statistics
     * @param alpha
     * @param excluded
     * @return
     */
    public ArrayList<Integer> findRoute(
            double[] statistics, double alpha,
            Set<Edge> excluded) {
        int i = 0;
        while (!pq.isEmpty()) {
            int current = pq.poll();
            if (current == goal)
                return reconstructPath(statistics);
            closedSet.add(current);
            for (int x : offsets) {
                int neighbor = x + current;
                if (excluded.contains(new Edge(current, neighbor))) {
                    continue;
                }
                if (array[neighbor] != WHITECELL || closedSet.contains(neighbor)) {
                    continue;
                }

                double m = 1;
                if (x > 3) {
                    m = Math.sqrt(2);
                }
                double tentative_gScore = gScore[current] + radiation[neighbor] * m;
                
                if (tentative_gScore >= gScore[neighbor]) {
                    continue;
                }
                gScore[neighbor] = tentative_gScore;
                prev[neighbor] = current;
                pq.add(neighbor);
            }
        }
        return null;
    }

    
    /**
     * Returns the path (from the end to the begining)
     * @param statistics
     * @return
     */
    private ArrayList<Integer> reconstructPath(double[] statistics) {
        ArrayList<Integer> totalPath = new ArrayList<>();
        double length = 0;
        int rad = 0;
        totalPath.add(goal);
        int current = goal;
        while (current != start) {
            length += distance(current, prev[current]);
            rad += radiation[current];
            current = prev[current];
            totalPath.add(current);
        }
        rad += radiation[start];
        statistics[0] = length;
        statistics[1] = rad;
        return totalPath;
    }
    
    
    /**
     * Edge representation
     */
    public static class Edge {
        public int from;
        public int to;
        
        public Edge(int f, int t) {
            from = f;
            to = t;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + from;
            result = prime * result + to;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Edge other = (Edge) obj;
            if (from != other.from)
                return false;
            if (to != other.to)
                return false;
            return true;
        }
        
        @Override
        public String toString() {
            return from + " -> " + to;
        }
    }
}


