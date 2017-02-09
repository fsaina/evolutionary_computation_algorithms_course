package hr.fer.zemris.optjava.dz6.TSPSolver;

import javax.swing.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Traveling salesman problem solver with the Max-Min Ant System algorithm( MMAS ).
 * Implementation is tested on TSPLIB problem sets with some parameter tinkering.
 *
 * Run examples:
 * data/bays29.tsp 5 50 500
 * data/att48.tsp 8 50 800
 * data/ch150.tsp 8 50 800
 * data/pr2392.tsp 10 100 1000
 *
 * At the end of the run, the best found solution will be shown with the Global
 * optimum value gained from summing the OPTIMUM path provided from TSPLIB.
 *
 * Graphical representation is also provided without the last verticy connected
 * so the initial and last city are visible.
 *
 *
 * NOTE: For better performance, for every problem there is a slightly different
 * set of parameter values. General guidelines:
 *
 * RO - from 1% to 5%           - evaporation speed
 * BETA - from 2 to 6           - strength of heuristic guidelines (inverse of distance)
 * ALPHA - ~1                   - strength of feromone trail
 *
 * @author Filip Saina
 */
public class TSPSolver {

    public static final double RO = 0.03;
    public static final double BETA = 6;
    public static final double ALPHA = 1.05;

    private final int STAGNATION_TRESHOLD;
    private int maxIteration;
    private Graph graph;
    private List<Ant> ants;

    public TSPSolver(int maxIteration, Graph graph, int m) {
        this.maxIteration = maxIteration;
        STAGNATION_TRESHOLD = maxIteration / 3;
        this.graph = graph;

        createAnts(m);
    }

    private void createAnts(int m) {
        ants = new ArrayList<Ant>();
        for (int i = 0; i < m; i++) {
            ants.add(new Ant(graph));
        }
    }

    public void run(String fileLocation) {
        int iteration = 0;
        double globalBestDistance = Double.MAX_VALUE;
        Ant globalBestAnt = null;
        int lastBestIteration = 0;
        while (iteration < maxIteration) {

            double BEST_distance = Double.MAX_VALUE;
            Ant bestAnt = null;

            for (Ant ant : ants) {
                ant.runSolution();
                double distance = ant.evaluateDistanceTraveled();

                if (distance < BEST_distance) {
                    BEST_distance = distance;
                    bestAnt = ant;
                }
            }

            if (Math.abs(lastBestIteration - iteration) > STAGNATION_TRESHOLD) {
                graph.setEvaporationValuesToMaximum();
                System.out.println(iteration + "  Stagnation! ");
                lastBestIteration = iteration;
            }

            if (BEST_distance < globalBestDistance) {
                globalBestAnt = bestAnt.copy();
                globalBestDistance = BEST_distance;
                //update tmax tmin
                graph.updateFeromones(globalBestDistance);
                System.out.println(String.format("%4d  %f", iteration, globalBestDistance));
                lastBestIteration = iteration;
            }

            graph.evaporateFeromoneTrail();
            double E = (iteration) / (double) maxIteration;
            graph.evaporateAntTrail(bestAnt, 1 - E);
            graph.evaporateAntTrail(globalBestAnt, E);

            for (Ant ant : ants) {
                ant.resetAnt();
            }

            iteration++;
        }

        System.out.println("\nFinished!\n");
        System.out.println("Obtained distance: " + globalBestAnt.evaluateDistanceTraveled());
        System.out.println("Obtained solution: \n" + globalBestAnt.toString());
        drawSolution(globalBestAnt);

        //Load and evaluate the solution
        graph.loadSolutionFromFile(
                Paths.get(fileLocation.substring(0, fileLocation.indexOf(".tsp")) + ".opt.tour"));
    }

    private void drawSolution(final Ant bestAnt) {
        Runnable r = new Runnable() {
            public void run() {
                List<City> cities = bestAnt.getVisitedCities();
                LineComponent lineComponent = new LineComponent(500, 500, 10);

                City city = cities.get(0);
                for (int ii = 1; ii < graph.numberOfCities(); ii++) {
                    lineComponent.addLine(city.getX(),
                            city.getY(),
                            cities.get(ii).getX(),
                            cities.get(ii).getY());

                    city = cities.get(ii);
                }

                for (City city1 : cities) {
                    lineComponent.addPoint(city1.getX(), city1.getY());
                }
                JOptionPane.showMessageDialog(null, lineComponent);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Wrong number of arguments provided");
            System.exit(-1);
        }

        String fileLocation = args[0];          // TSP problem description file
        Integer k = Integer.parseInt(args[1]);  // number of neighbouring candidates
        Integer l = Integer.parseInt(args[2]);  // number of ants in colony
        Integer maxIter = Integer.parseInt(args[3]); // maximum number of iterations

        Graph graph = new Graph(k, Paths.get(fileLocation), maxIter);
        TSPSolver tspSolver = new TSPSolver(maxIter, graph, l);
        tspSolver.run(fileLocation);
    }
}
