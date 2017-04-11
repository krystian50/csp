package validator;

import models.CSPVertexPair;
import models.Graph;
import models.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystian on 2017-04-05.
 */
public class GraphValidator {

    private static int[][] borders =
            {{-1, 0},
                    {0, -1},
                    {1, 0},
                    {0, 1}};

    public boolean isValid(Graph graph, int x, int y) {
        int size = graph.getSize();
        int[][] graphArray = graph.getGraph();

        boolean result = true;

        if (x != (size - 1)) {
            result = result && (graphArray[x][y] != graphArray[x+1][y]);
        }
        if (x != 1) {
            result = result && (graphArray[x-1][y] != graphArray[x][y]);
        }

        if (y != (size - 1)) {
            result = result && (graphArray[x][y] != graphArray[x][y+1]);
        }
        if (y != 1) {
            result = result && (graphArray[x][y-1] != graphArray[x][y]);
        }
        return result;
    }

    public boolean isBackwardsValid(Graph graph, int x, int y) {
        int size = graph.getSize();
        int[][] graphArray = graph.getGraph();

        boolean result = true;

        if (x != 0) {
            result = result && graphArray[x-1][y] != graphArray[x][y];
        }
        if (y != 0) {
            result = result && graphArray[x][y-1] != graphArray[x][y];
        }

        return result;
    }

    public boolean areNeighboursDifferent(Graph graph, int x, int y, int color) {
        int size = graph.getSize();
        int[][] graphArray = graph.getGraph();

        ArrayList<Tuple<Integer, Integer>> neighbors = new ArrayList<>();

        for (int i = 0; i < borders.length; i++) {
            int modX = x + borders[i][0];
            int modY = y + borders[i][1];
            if (hasCell(size, modX, modY)) {
                neighbors.add(new Tuple<Integer, Integer>(modX, modY));
            }
        }

        for (Tuple<Integer, Integer> neighbor : neighbors) {
            int nX = neighbor.first;
            int nY = neighbor.second;
            if (graphArray[nX][nY] == color) {
                return false;
            }
        }

        return true;
    }

    public boolean areColorPairsUnique(Graph graph, List<CSPVertexPair> colors, int x, int y, int color) {
        int[][] graphArray = graph.getGraph();

        CSPVertexPair firstPair = null;
        CSPVertexPair secondPair = null;

        boolean hasFirstPair = false;
        boolean hasSecondPair = false;

        if (x == 0 && y == 0) {
            return true;
        }

        if (x != 0) {
            firstPair = new CSPVertexPair(graphArray[x-1][y], color);
            hasFirstPair = colors.contains(firstPair);
        }
        if (y != 0) {
            secondPair = new CSPVertexPair(graphArray[x][y-1], color);
            hasSecondPair = colors.contains(secondPair);
        }

        if (hasFirstPair || hasSecondPair) {
            return false;
        }

        return true;
    }

    private boolean hasCell(int size, int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            return false;
        }
        return true;
    }
}