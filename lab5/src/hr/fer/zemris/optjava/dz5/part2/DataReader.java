package hr.fer.zemris.optjava.dz5.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataReader {

    private Integer[][] costs;
    private Integer[][] distances;
    private int numberOfVariables;

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public DataReader(Path path, int n) throws IOException {
        numberOfVariables = n;

        List<String> lines = Files.readAllLines(path).stream().map(String::trim)
            .filter(s -> !s.isEmpty()).collect(Collectors.toList());

        distances = new Integer[n][n];
        costs = new Integer[n][n];

        for (int i = 0; i < n; i++) {
            int j = i + 1;

            distances[i] = Arrays.stream(lines.get(j).split("\\s+")).map(Integer::parseInt)
                .collect(Collectors.toList()).toArray(distances[i]);
        }

        for (int i = 0; i < n; i++) {
            int j = n + i + 1;

            costs[i] = Arrays.stream(lines.get(j).split("\\s+")).map(Integer::parseInt)
                .collect(Collectors.toList()).toArray(costs[i]);
        }
    }

    public Integer[][] getDistances() {
        return distances;
    }

    public Integer[][] getCosts() {
        return costs;
    }
}