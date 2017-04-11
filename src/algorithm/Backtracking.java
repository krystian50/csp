package algorithm;

import models.CSPVertexPair;
import models.Graph;
import models.Tuple;

import java.util.LinkedList;

/**
 * Created by Krystian on 2017-04-03.
 */
public class Backtracking {

    Graph graph;
    int[][] graphArray;
    private LinkedList<CSPVertexPair> colors;
    private static final int INVALID_STATE = -1;

    public Backtracking() {
        colors = new LinkedList<CSPVertexPair>();
    }

    public boolean solve(Graph graph) {
        this.colors.clear();
        this.graph = graph;
        this.graphArray = graph.getGraph();
        return nextState();
    }

    /**
     * 1. find next vertice to color
     * 2. for every color
     * 3. check if can be colored
     * 3a. color vertice
     * 4. if cannot be colored
     * 4a. remove color
     * @return
     */
    private boolean nextState() {
        Tuple<Integer, Integer> uncoloredVertex = getFirstUncolored();

        if (uncoloredVertex.first == -1
                && uncoloredVertex.second == -1) {
            return true;
        }
        int uncoloredX = uncoloredVertex.first;
        int uncoloredY = uncoloredVertex.second;
        for (int i = 0; i < graph.getAvailableColors(); i++) {
            if (canColorVertice(uncoloredX, uncoloredY, i)) {
                graphArray[uncoloredX][uncoloredY] = i;
//                System.out.println(graph);
                if (nextState()) {
                    return true;
                }else{
//                    System.out.println(uncoloredX + ", " + uncoloredY + " - Undoing, size before" + colors.size());
                    graphArray[uncoloredX][uncoloredY] = INVALID_STATE;
                    undoPairs(uncoloredX, uncoloredY);
//                    System.out.println("Undoing, size after" + colors.size());
                }
            }
        }
        return false;
    }

    private void undoPairs(int uncoloredX, int uncoloredY) {
        if ((uncoloredX == 0) != (uncoloredY == 0)) {
            colors.removeLast();
        }else if (uncoloredX != 0 && uncoloredY != 0){
            colors.removeLast();
            colors.removeLast();
        }
    }

    private Tuple<Integer, Integer> getFirstUncolored() {
        int[][] graphArray = graph.getGraph();
        for (int i = 0; i < graph.getSize(); i++){
            for (int j = 0; j < graph.getSize(); j++) {
                if (graphArray[i][j] == -1) {
                    return new Tuple<Integer, Integer>(i, j);
                }
            }
        }
        return new Tuple<Integer, Integer>(INVALID_STATE, INVALID_STATE);
    }

    private boolean canColorVertice(int x, int y, int color) {
        int previousColor = graphArray[x][y];

        graphArray[x][y] = color;
        boolean result = graph.validateBackwards(x, y);
        graphArray[x][y] = previousColor;

        if (result) {
            result = hasGraphOnlySingleColoredPair(x, y, color);
        }

        return result;
    }

    private boolean hasGraphOnlySingleColoredPair(int x, int y, int color) {
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

        if (firstPair != null && !hasFirstPair) {
            colors.add(firstPair);
        }

        if (secondPair != null && !hasSecondPair) {
            colors.add(secondPair);
        }

        return true;
    }
}
