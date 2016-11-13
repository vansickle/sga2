package main;

public class GraphBuilder {
    public static double[] build(int[] array, int size) {
        double[] radiation = new double[size * size];
        for (int i = 0; i < size * size; i++)
            if (array[i] == 1)
                for (int j = 0; j < size * size; j++)
                    radiation[j] += 1e5 / Main.buildDistance(i, j);
        return radiation;
    }
}
