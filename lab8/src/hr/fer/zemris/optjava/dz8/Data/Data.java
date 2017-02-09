package hr.fer.zemris.optjava.dz8.Data;

import hr.fer.zemris.optjava.dz8.Data.Model.LaserModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Data implements Iterable<LaserModel>{

    private String filePath;
    private int windowSize;
    private int beginningOffset;

    private List<LaserModel> models = null;
    private List<LaserModel> testSet= null;
    private int numberOfElements = 0;

    private int largest;
    private int smallest;
    private int a = -1;
    private int b = 1;

    public Data(String filePath, int windowSize, int beginningOffset){
        this.filePath = filePath;
        this.windowSize = windowSize;
        this.beginningOffset = beginningOffset;
    }

    public List<LaserModel> loadIrisData() throws IOException {
        if(models == null){
            loadFromFile();
        }
        return models;
    }

    public List<LaserModel> getTestSet() {
        return testSet;
    }

    public List<LaserModel> getModels() {
        return models;
    }

    private void loadFromFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        if(beginningOffset == -1){
            beginningOffset = lines.size();
        }

        List<LaserModel> evalutionPairs = new ArrayList<>();
        List<LaserModel> testSet = new ArrayList<>();

        largest = Integer.MIN_VALUE;
        smallest = Integer.MAX_VALUE;
        for(String line : lines){
            int num = Integer.parseInt(line.trim());

            if(num > largest) largest = num;
            if(num < smallest) smallest = num;
        }

        int currentSize = 0;
        while(currentSize+windowSize < lines.size()){
            List<Double> pair = new ArrayList<>();
            for(int i =currentSize ; i< currentSize+windowSize ; i++){
               int number = Integer.parseInt(lines.get(i).trim());

                double normalized = normalize(number);
               pair.add(normalized);
            }

            double normalized = normalize(Integer.parseInt(lines.get(currentSize+windowSize).trim()));

           if(currentSize+windowSize < beginningOffset) {
               evalutionPairs.add(new LaserModel(pair, normalized));
           }else {
               testSet.add(new LaserModel(pair, normalized));
           }

            currentSize++;
        }

        models = evalutionPairs;
        this.testSet = testSet;
        numberOfElements = evalutionPairs.size();
    }

    private double normalize(int number) {
        return a + (number - smallest) * (b-a) / (double) (largest - smallest);
    }

    public double fromNormalized(double normalized){
        return ( (normalized - a) * (largest - smallest) / (b-a) + smallest);
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    @Override
    public Iterator<LaserModel> iterator() {
        if(models == null){
            throw new IllegalStateException("Models are not initialized!");
        }
        return models.iterator();
    }

}
