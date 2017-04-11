package models;

import validator.GraphValidator;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Krystian on 2017-04-03.
 */

public class Graph {

    private int[][] graph;
    private GraphValidator validator;
    private static final int INVALID_STATE = -1;
    private LinkedList<CSPVertexPair> colors;

    public int[][] getGraph() {
        return graph;
    }

    public void setGraph(int[][] graph) {
        this.graph = graph;
    }

    public int getSize() {
        return graph.length;
    }

    public int getAvailableColors() {
        return (getSize() % 2 == 0) ? 2*getSize() : 2*getSize() + 1;
    }

    public Graph(int n, GraphValidator validator) {
        graph = new int[n][n];
        for(int[] dimension : graph) {
            java.util.Arrays.fill(dimension, INVALID_STATE);
        }
        this.validator = validator;
        colors = new LinkedList<>();
    }

    public Graph(Graph another) throws IllegalAccessException, InstantiationException {
        int[][] anotherGraph = another.getGraph();
        int size = another.getSize();
        graph = new int[size][size];
        validator = another.validator.getClass().newInstance();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                graph[i][j] = anotherGraph[i][j];
            }
        }
    }

    public boolean validateGraph() {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (!validator.isValid(this, i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateVertice(int x, int y) {
        return validator.isValid(this, x, y);
    }

    public boolean validateBackwards(int x, int y) {
        return validator.isBackwardsValid(this, x, y);
    }

    public boolean canColor(int x, int y, int color) {
        return validator.areNeighboursDifferent(this, x, y, color)
                && validator.areColorPairsUnique(this, colors, x, y, color);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                builder.append(graph[i][j]);
                if (j < getSize() - 1) {
                    builder.append(",");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public boolean setColor(int x, int y, int color) {

        return true;
    }

    public boolean removeColor(int x, int y) {
        if (graph[x][y] != INVALID_STATE) {
            graph[x][y] = INVALID_STATE;
            if ((x == 0) != (y == 0)) {
                colors.removeLast();
            }else if (x != 0 && y != 0){
                colors.removeLast();
                colors.removeLast();
            }
        }else{
            return false;
        }
        return true;
    }
}