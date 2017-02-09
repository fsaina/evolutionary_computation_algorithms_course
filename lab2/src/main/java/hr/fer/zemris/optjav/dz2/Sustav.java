
package hr.fer.zemris.optjav.dz2;

import hr.fer.zemris.optjav.dz2.functions.IHFunction;
import hr.fer.zemris.optjav.dz2.functions.LinearSystemCostFunction;
import hr.fer.zemris.optjav.dz2.functions.PrijenosnaCostFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by fsaina-lenovo on 10/17/16.
 */
public class Sustav {


    public static void main(String[] args) {
        if(args.length != 3){
            System.err.println("Wrong number of arguments provided");
            System.exit(-1);
        }

        String method = args[0];
        Integer maxNumberOfIterations = Integer.parseInt(args[1]);
        String path = args[2];


        IHFunction function = null;
        try {
            function = loadFunctionDataFromFile(Paths.get(path), "sustav", 10);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading the input file");
            System.exit(-1);
        }

        RealVector solution = null;
        ArrayList<Double> initialPoints = new ArrayList<>();
        Jednostavno.generateRandomPointsInVector(initialPoints, 10);

        switch (method){
            case "grad":{
                solution = NumOptAlgorithms.gradientDescent(
                        function,
                        maxNumberOfIterations,
                        initialPoints,
                        true
                );
                break;
            }

            case "newton":{
                solution = NumOptAlgorithms.newtonMethod(
                        function,
                        maxNumberOfIterations,
                        initialPoints,
                        true
                );
                break;
            }
            default:{
                System.err.println("Not a valid method name given");
                System.exit(-1);
            }
        }

        System.out.println(solution);
    }

    public static IHFunction loadFunctionDataFromFile(Path path, String method, int entiesNumber) throws IOException {

        List<String> lines = Files.readAllLines(path);
        int numberOfLines = 0;
        int numberOfVariables = 0;
        RealVector solutions = new ArrayRealVector(entiesNumber);
        ArrayList<ArrayList<Double>> constants = new ArrayList<>();

        for(String line : lines){

           if(line.startsWith("#"))
               continue;

            //removing paretahesis
            String data = line.substring(1, line.length()-1);

            ArrayList<Double> constantsList = new ArrayList<>();

            String[] splitList = data.split(",");
            numberOfVariables = splitList.length - 1;
            for(String value : splitList){
               constantsList.add(Double.parseDouble(value));
            }

            Double solutionToEquation = constantsList.get(constantsList.size()-1);
            constantsList.remove(constantsList.size()-1);

            constants.add(constantsList);
            solutions.setEntry(numberOfLines, solutionToEquation);

            numberOfLines++;
        }

        RealMatrix constMatrix = new Array2DRowRealMatrix(numberOfLines, numberOfVariables);

        for(int i = 0; i<numberOfLines; i++){
            for(int j = 0 ; j < numberOfVariables; j++){
                constMatrix.setEntry(i, j, constants.get(i).get(j));
            }
        }


        switch (method){
            case "sustav":{
                return new LinearSystemCostFunction(constMatrix, solutions);
            }
            case "prijenosna":{
               return new PrijenosnaCostFunction(constMatrix, solutions);
            }
            default:{
                System.err.println("Not valid argument provied");
            }
        }

        return null;
    }
}
