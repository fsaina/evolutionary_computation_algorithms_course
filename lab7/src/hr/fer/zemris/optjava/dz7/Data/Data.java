package hr.fer.zemris.optjava.dz7.Data;

import hr.fer.zemris.optjava.dz7.Data.Model.IrisModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Data {

    private String filePath;
    private List<IrisModel> models = null;
    private int numberOfElements = 0;

    public Data(String filePath){
        this.filePath = filePath;
    }

    public List<IrisModel> loadIrisData() throws IOException {
        if(models == null){
            loadFromFile();
        }
        return models;
    }

    public List<IrisModel> getModels() {
        return models;
    }

    private void loadFromFile() throws IOException {
        models = new ArrayList<>();
        Path dataFile = Paths.get(filePath);
        List<String> lines = Files.readAllLines(dataFile);

        for (String line : lines) {
            String[] split = line.split(":");

            String mesures = removeParenthesis(split[0]);
            String result = removeParenthesis(split[1]);

            String[] mesuresSplit = mesures.split(",");
            String[] resultsSplit = result.split(",");

            models.add(new IrisModel(
                    Double.parseDouble(mesuresSplit[0]),
                    Double.parseDouble(mesuresSplit[1]),
                    Double.parseDouble(mesuresSplit[2]),
                    Double.parseDouble(mesuresSplit[3]),
                    Integer.parseInt(resultsSplit[0]),
                    Integer.parseInt(resultsSplit[1]),
                    Integer.parseInt(resultsSplit[2])
            ));
            numberOfElements++;

        }

    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    private String removeParenthesis(String s) {
        s = s.replaceAll("\\(", "");
        s = s.replaceAll("\\)", "");
        return s;
    }

    public int getTraningVectorDimension() {
        return 4;
    }
}
