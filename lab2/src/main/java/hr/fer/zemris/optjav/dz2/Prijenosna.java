
package hr.fer.zemris.optjav.dz2;

import hr.fer.zemris.optjav.dz2.functions.IHFunction;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * Created by fsaina-lenovo on 10/17/16.
 */
public class Prijenosna {


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
            function = Sustav.loadFunctionDataFromFile(Paths.get(path), "prijenosna", 20);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading the input file");
            System.exit(-1);
        }

        RealVector solution = null;
        ArrayList<Double> initialPoints = new ArrayList<>();
        Jednostavno.generateRandomPointsInVector(initialPoints, 6);

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
}
