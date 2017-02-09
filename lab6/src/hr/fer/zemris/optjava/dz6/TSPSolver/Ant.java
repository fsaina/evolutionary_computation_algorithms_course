package hr.fer.zemris.optjava.dz6.TSPSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fsaina-lenovo on 12/2/16.
 */
public class Ant {
    private Graph graph;
    private List<City> visitedCities = new ArrayList<City>();
    private List<Double> distances = new ArrayList<Double>();
    private int currentCityIndex;
    private City startCity;

    public Ant(Graph graph) {
        this.graph = graph;
        randomPositionAnt();
    }

    public void resetAnt() {
        randomPositionAnt();
        visitedCities.clear();
        distances.clear();
    }

    private void randomPositionAnt() {
        Random random = new Random();
        currentCityIndex = random.nextInt(graph.numberOfCities());
        startCity = graph.getCityWithIndex(currentCityIndex);
    }

    private double randomProportionalRule(int city1, int city2, List<City> possibleCities) {
        double probabilityValue = sumProportionalValue(city1, possibleCities);
        double feromoneValue = graph.getFeromoneValue(city1, city2);

        feromoneValue = Math.pow(feromoneValue, TSPSolver.ALPHA);
        double heuristicValue = graph.getHeuristicValue(city1, city2);
        double result = (feromoneValue * heuristicValue) / probabilityValue;

        return result;
    }

    private double sumProportionalValue(int city1, List<City> possibleCities) {
        double sum = 0.0;
        for (City city : possibleCities) {

            double feromoneVal = Math.pow(graph.getFeromoneValue(city1, city.getIndex()), TSPSolver.ALPHA);
            sum += feromoneVal * graph.getHeuristicValue(city1, city.getIndex());
        }

        return sum;
    }

    public void runSolution() {

        do {
            visitedCities.add(graph.getCityWithIndex(currentCityIndex));

            List<City> unvisited = unvisitedCityList(graph.findNearestNeighbours(currentCityIndex));
            if (unvisited.isEmpty()) {
                unvisited = globalUnvisitedCities(currentCityIndex);
                if (unvisited.isEmpty()) {
                    break;
                }
            }
            //go to next one
            int nextCityIndex = chooseNextCity(unvisited, currentCityIndex);
            distances.add(graph.getCityDistances(currentCityIndex, nextCityIndex));
            currentCityIndex = nextCityIndex;

        } while (visitedCities.size() != graph.numberOfCities());

        //add the starting city to the end
        distances.add(graph.getCityDistances(currentCityIndex, startCity.getIndex()));
        visitedCities.add(startCity);

    }

    private int chooseNextCity(List<City> unvisited, int currentCityIndex) {
        List<Double> nextCityProbability = new ArrayList<Double>();
        for (City city : unvisited) {
            nextCityProbability.add(randomProportionalRule(currentCityIndex, city.getIndex(), unvisited));
        }

        Random random = new Random();
        double pick = random.nextDouble();

        double currentProb = 0.0;
        int index = 0;
        for (double probabilityNext : nextCityProbability) {
            if (pick >= currentProb && pick < currentProb + probabilityNext) {
                return unvisited.get(index).getIndex();
            }
            currentProb += probabilityNext;
            index++;
        }

        throw new IllegalStateException("No probability was choosen!");
    }

    private List<City> globalUnvisitedCities(int currentCityIndex) {
        List<City> unvisitedGlobal = new ArrayList<City>();

        for (int i = 0; i < graph.numberOfCities(); i++) {
            if (visitedCities.contains(graph.getCityWithIndex(i)) || i == currentCityIndex) {
                continue;
            }
            unvisitedGlobal.add(graph.getCityWithIndex(i));
        }
        return unvisitedGlobal;
    }

    private List<City> unvisitedCityList(List<City> neighbouringCities) {
        List<City> unvisited = new ArrayList<City>();

        for (City neigbour : neighbouringCities) {
            if (!visitedCities.contains(neigbour)) {
                unvisited.add(neigbour);
            }
        }

        return unvisited;
    }

    public double evaluateDistanceTraveled() {
        double sum = 0.0;
        for (Double distance : distances) {
            sum += distance;
        }

        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean oneVisited = false;
        for (City city : visitedCities) {
            if (city.getIndex() == 0)
                oneVisited = true;
            if (oneVisited) {
                sb.append(city.getIndex()+1);
                sb.append(" ");
            }
        }
        for(City city: visitedCities){
            if(city.getIndex() == 0){
                break;
            }
            if(city.getIndex() == visitedCities.get(visitedCities.size()-1).getIndex()){
                continue;
            }
            sb.append(city.getIndex()+1);
            sb.append(" ");
        }

        sb.append(1);

        return sb.toString();
    }

    public List<City> getVisitedCities() {
        return visitedCities;
    }

    public Ant copy() {
        Ant ant = new Ant(graph);
        ant.visitedCities = new ArrayList<City>(visitedCities);
        ant.distances = new ArrayList<Double>(distances);
        ant.startCity = startCity;
        return ant;
    }
}