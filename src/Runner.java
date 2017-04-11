import algorithm.Backtracking;
import algorithm.Fastforwarding;
import models.Graph;
import validator.GraphValidator;

/**
 * Created by Krystian on 2017-04-03.
 */
public class Runner {
    public static void forward(int n) {
        GraphValidator validator2 = new GraphValidator();
        Graph graph2 = new Graph(n, validator2);
        Fastforwarding solverFast = new Fastforwarding();
        long timeBefore2 = System.currentTimeMillis();
        solverFast.solveForwardChecking(graph2);
//        System.out.println(graph2);
        System.out.print((System.currentTimeMillis() - timeBefore2)+";");
    }

    public static void back(int n) {
        GraphValidator validator = new GraphValidator();
        Graph graph = new Graph(n, validator);
        Backtracking solverBacktracking = new Backtracking();
        long timeBefore = System.currentTimeMillis();
        solverBacktracking.solve(graph);
//        System.out.println(graph);
        System.out.print((System.currentTimeMillis() - timeBefore));
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i++) {
            forward(7);
            back(7);
            System.out.println();
        }
    }
}