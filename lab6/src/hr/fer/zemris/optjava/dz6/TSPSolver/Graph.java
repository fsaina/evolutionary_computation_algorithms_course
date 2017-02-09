package hr.fer.zemris.optjava.dz6.TSPSolver;


import javax.swing.table.TableCellEditor;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 12/2/16.
 */
public class Graph {
    private List<City> nodes = new ArrayList<City>();
    private List<List<City>> candidateList;
    private double[][] verticyMatrix;
    private double[][] verticyFeromoneMatrix;
    private double[][] verticyHeuristicMatrix;
    private int kNearest;
    private double feromoneValueMax;
    private double feromoneValueMin;

    public static double FEROMONE_A;

    public Graph(int kNearest, Path file, int maxiter) {
        this.kNearest = kNearest;
        loadGraphFromFile(file);
        FEROMONE_A = maxiter/(double)kNearest;
    }

    public List<City> findNearestNeighbours(int cityIndex) {
        return candidateList.get(cityIndex);
    }

    private List<City> findKNearestNeighbours(City city) {

        List<Vertecy> copyNeghbours = new ArrayList<Vertecy>();
        List<City> cityList = new ArrayList<City>();

        int index = 0;
        for (Double length : verticyMatrix[city.getIndex()]) {

            copyNeghbours.add(new Vertecy(length, index));
            index++;
        }

        Collections.sort(copyNeghbours);

        //the one at index 0 is itself
        int i = 0;
        while (cityList.size() != kNearest) {
            if (copyNeghbours.get(i).index == city.getIndex()) {
                i++;
                continue;
            }

            cityList.add(nodes.get(copyNeghbours.get(i).index));
            i++;
        }

        return cityList;
    }

    public double getHeuristicValue(int city1, int city2) {
        return verticyHeuristicMatrix[city1][city2];
    }

    public double getFeromoneValue(int city1, int city2) {
        return verticyFeromoneMatrix[city1][city2];
    }

    public void evaporateFeromoneTrail() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                verticyFeromoneMatrix[i][j] *= (1 - TSPSolver.RO);

                if (verticyFeromoneMatrix[i][j] < feromoneValueMin) {
                    // bottom bound
                    verticyFeromoneMatrix[i][j] = feromoneValueMin;
                }
            }
        }
    }

    private void createLengthMatrix() {
        verticyMatrix = new double[nodes.size()][nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {

                if (i == j) {
                    continue;
                }
                // set the distance in the matrix
                verticyMatrix[i][j] = euclidianDistance(
                        nodes.get(i),
                        nodes.get(j));
            }
        }
    }

    private void createHeuristicMatrix() {
        verticyHeuristicMatrix = new double[nodes.size()][nodes.size()];

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {

                if (i == j) {
                    continue;
                }
                //set the heuristic matrix value
                if (verticyMatrix[i][j] == 0) {
                    //if len == 0, depend only on the probability
                    verticyHeuristicMatrix[i][j] = 1;
                } else {
                    verticyHeuristicMatrix[i][j] = Math.pow(1 / verticyMatrix[i][j], TSPSolver.BETA);
                }
            }
        }
    }
    private void createFeromoneMatrix() {
        verticyFeromoneMatrix = new double[nodes.size()][nodes.size()];
        setEvaporationValuesToMaximum();
    }

    private double euclidianDistance(City city1, City city2) {

        double dx = Math.abs(city1.getX() - city2.getX());
        double dy = Math.abs(city1.getY() - city2.getY());

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void loadSolutionFromFile(Path path){

        List<String> lines = null;

        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading input file");
            System.exit(-1);
        }

        List<City> cities = new ArrayList<City>();
        for (String line : lines) {
            line = line.toLowerCase();

            if (line.startsWith("name") ||
                    line.startsWith("comment") ||
                    line.startsWith("type") ||
                    line.startsWith("dimension") ||
                    line.startsWith("edge") ||
                    line.startsWith("node") ||
                    line.startsWith("eof") ||
                    line.startsWith("tour")) {

                continue;
            }

            line = line.trim();
            if(line.equals("-1")){
                break;
            }
            cities.add(getCityWithIndex(Integer.parseInt(line) - 1));
        }

        City start = cities.get(0);
        double sum = 0.0;
        for(int i = 1; i < cities.size(); i++){
            sum += getCityDistances(start.getIndex(), cities.get(i).getIndex());
            start = cities.get(i);
        }
        sum += getCityDistances(start.getIndex(), cities.get(0).getIndex());

        System.out.println("\nBest solution distance from TSPLIB: " + sum);
    }

    public void loadGraphFromFile(Path path) {
        List<String> lines = null;

        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading input file");
            System.exit(-1);
        }

        int dimension = -1;
        int weightIndex = 0;

        for (String line : lines) {
            line = line.toLowerCase();

            if (line.startsWith("name") ||
                    line.startsWith("comment") ||
                    line.startsWith("type") ||
                    line.startsWith("edge") ||
                    line.startsWith("node") ||
                    line.startsWith("eof") ||
                    line.startsWith("display") ||
                    line.startsWith("tour")) {

                continue;
            }

            if(line.startsWith("dimension")){
                line = line.substring(line.indexOf(":")+1, line.length()).trim();
                dimension = Integer.parseInt(line);
                continue;
            }

            line = line.trim();
            String[] split = line.split("\\s+");

            if(split.length == 3) {
                City city = new City(Integer.parseInt(split[0]) - 1,
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]));

                nodes.add(city);
            } else {
                //a weight entry line
                if(verticyMatrix == null){
                    verticyMatrix = new double[dimension][dimension];
                }

                int i =0;
                for(String weight : split){
                    verticyMatrix[weightIndex][i] = Double.parseDouble(weight);
                    i++;
                }
                weightIndex++;
            }
        }

        //generate the distance matrix between cities
        if(verticyMatrix == null) {
            createLengthMatrix();
        }

        createHeuristicMatrix();

        //create candidate list for every city (k-nearest)
        createNeighbourList();

        //update feromone values
        updateFeromones(greedyPathLength());

        //create the feromone matrix
        createFeromoneMatrix();
    }

    public double greedyPathLength() {
        Random random = new Random();
        int startIndex = random.nextInt(nodes.size());

        List<Integer> visited = new ArrayList<Integer>();
        visited.add(startIndex);

        double length = 0.0;
        City current = getCityWithIndex(startIndex);
        while(visited.size() != nodes.size()){
            City next = findNearestNeighbours(current.getIndex()).get(0);

            visited.add(next.getIndex());
            length += verticyMatrix[current.getIndex()][next.getIndex()];
            current = next;
        }

        length += verticyMatrix[current.getIndex()][startIndex];
        return length;
    }

    private void createNeighbourList() {
        candidateList = new ArrayList<List<City>>();
        for (int i = 0; i < nodes.size(); i++) {

            List<City> cities = findKNearestNeighbours(nodes.get(i));
            candidateList.add(cities);
        }
    }

    public int numberOfCities() {
        return nodes.size();
    }

    public City getCityWithIndex(int i) {
        return nodes.get(i);
    }

    public Double getCityDistances(int currentCityIndex, int nextCityIndex) {
        return verticyMatrix[currentCityIndex][nextCityIndex];
    }

    public void evaporateAntTrail(Ant bestAnt, double E) {

        List<City> visited = bestAnt.getVisitedCities();

        double length = bestAnt.evaluateDistanceTraveled();

        int initial = visited.get(0).getIndex();
        for (int i = 1; i < visited.size(); i++) {

            int current = visited.get(i).getIndex();
            verticyFeromoneMatrix[initial][current] += ((1-E)*(1/(double)length));

            if (verticyFeromoneMatrix[initial][current] > feromoneValueMax) {
                //upper feromone bound
                verticyFeromoneMatrix[initial][current] = feromoneValueMax;
            }

            initial = current;
        }
    }

    public void updateFeromones(double globalBestDistance) {
        feromoneValueMax = 1 / (TSPSolver.RO * globalBestDistance);
        feromoneValueMin = feromoneValueMax / FEROMONE_A;
    }

    public void setEvaporationValuesToMaximum() {
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {

                if (i == j) {
                    continue;
                }
                // set the feromone value for that matrix
                verticyFeromoneMatrix[i][j] = feromoneValueMax;
            }
        }
    }

    private class Vertecy implements Comparable<Vertecy> {
        private double length;
        private int index;

        public Vertecy(double length, int index) {
            this.length = length;
            this.index = index;
        }

        public int compareTo(Vertecy vertecy) {
            if (length < vertecy.length) {
                return -1;
            } else if (length > vertecy.length) {
                return 1;
            } else {
                return 0;
            }
        }
    }


}
