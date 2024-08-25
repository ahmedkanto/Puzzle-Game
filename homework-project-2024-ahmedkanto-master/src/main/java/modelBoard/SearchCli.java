package modelBoard;

import puzzle.solver.BreadthFirstSearch;
import puzzle.solver.Node;

import java.util.Optional;

public class SearchCli {
    /**
     * The main method of the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        LabyrinthModel model = new LabyrinthModel();
        BreadthFirstSearch<String> solver = new BreadthFirstSearch<>();

        Optional<Node<String>> solution = solver.solve(model);

        if (solution.isPresent()) {
            System.out.println("Solution found:");
            printSolution(solution.get());
        } else {
            System.out.println("No solution found.");
        }

    }

    private static void printSolution(Node<String> solution) {
        if (solution.getParent().isPresent()) {
            printSolution(solution.getParent().get());
        }
        solution.getMove().ifPresent(move -> System.out.println("Move: " + move));
    }
}
