package algorithm;

import models.CSPVertexPair;
import models.Graph;
import models.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krystian on 2017-04-10.
 */
public class Fastforwarding {

    Graph graph;
    int[][] graphArray;
    private LinkedList<CSPVertexPair> colors;
    private static final int INVALID_STATE = -1;
    private HashMap<int[], List<Integer>> variableDomains;

    public Fastforwarding() {
        colors = new LinkedList<CSPVertexPair>();
        variableDomains = new HashMap<>();
    }

    private void prepareSolver(Graph graph) {
        this.variableDomains.clear();
        this.colors.clear();
        this.graph = graph;
        this.graphArray = graph.getGraph();
        variableDomains = prepareDomainsForVariables();
    }

    public boolean solveForwardChecking(Graph graph) {
        prepareSolver(graph);
        return nextStateForwardChecking();
    }

    private boolean nextStateForwardChecking() {
        Tuple<Integer, Integer> uncoloredVertex = getFirstUncolored();
        int x = uncoloredVertex.first;
        int y = uncoloredVertex.second;
        int[] first = {x, y};

        if (x == -1) {
            return true;
        }

        List<Integer> colors = getDomain(first);
        for (Integer color: colors) {
            if (canColorVertice(x, y, color)) {
                graphArray[x][y] = color;
                HashMap<int[], List<Integer>> copy = copyVariableDomains(variableDomains);
                changeVariableDomains(first, color);
                if (nextStateForwardChecking()) {
                    return true;
                }else{
                    graphArray[x][y] = INVALID_STATE;
                    undoPairs(x, y);
                    variableDomains = copy;
                }
            }
        }

        return false;
    }

    private HashMap<int[], List<Integer>> copyVariableDomains(HashMap<int[], List<Integer>> variableDomains) {
        HashMap<int[], List<Integer>> copy = new HashMap<>();
        for (int[] key : variableDomains.keySet()) {
            int[] k = {key[0], key[1]};
            copy.put(k, new ArrayList<Integer>(variableDomains.get(key)));
        }

        return copy;
    }

    private HashMap<int[], List<Integer>> prepareDomainsForVariables() {
        HashMap<int[], List<Integer>> variableDomains = new HashMap<>();
        List<Integer> domain = new ArrayList<Integer>();

        for (int c = 0; c < graph.getAvailableColors(); c++) {
            domain.add(c);
        }

        for (int i = 0; i < graph.getSize(); i++) {
            for (int j = 0; j < graph.getSize(); j++) {
                int[] var = {i, j};
                variableDomains.put(var, new ArrayList<Integer>(domain));
            }
        }

        return variableDomains;
    }

    private List<Integer> getDomain(int[] variable) {
        for (int[] key : variableDomains.keySet()){
            if (java.util.Arrays.equals(key, variable)){
                return variableDomains.get(key);
            }
        }
        return null;
    }

    private  void changeVariableDomains(int[] var, int color) {
        int[] firstNeighbor = {var[0], var[1] + 1};
        int[] secondNeighbor = {var[0] + 1, var[1]};
        int[] domainToDelete = null;

        for (int[] key:variableDomains.keySet()) {
            if (java.util.Arrays.equals(key, var)) {
                domainToDelete = key;
            }else{
                if (java.util.Arrays.equals(key, firstNeighbor)) {
                    List<Integer> colors = variableDomains.get(key);
                    List<Integer> colorsInPair = getColorsInPair(color);
                    if (colors.contains(color)) {
                        colors.remove(colors.indexOf(color));
                    }
                    colors.removeAll(colorsInPair);
                    variableDomains.put(key, colors);
                }
                if (java.util.Arrays.equals(key, secondNeighbor)) {
                    List<Integer> colors = variableDomains.get(key);
                    List<Integer> colorsInPair = getColorsInPair(color);
                    if (colors.contains(color)) {
                        colors.remove(colors.indexOf(color));
                    }
                    colors.removeAll(colorsInPair);
                    variableDomains.put(key, colors);
                }
            }
        }
        variableDomains.remove(domainToDelete);
    }

    private List<Integer> getColorsInPair(int color) {
        List<Integer> colorsInPair = new ArrayList<>();
        for (CSPVertexPair t: colors) {
            if (t.second == color) {
                colorsInPair.add(t.first);
            }
        }
        return colorsInPair;
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
        boolean result = graph.canColor(x, y, color);

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