package models;
/**
 * Created by Krystian on 2017-04-03.
 */
public class Graph {

    private int[][] graph;

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

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                builder.append(graph[i][j]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}