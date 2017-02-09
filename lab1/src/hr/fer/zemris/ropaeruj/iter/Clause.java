package hr.fer.zemris.ropaeruj.iter;

/**
 * Abstract clause representation.
 * String used to represent the logic.
 * 'x' defines an undefined clause element(variable)
 * '1' defines an logical true (as is) variable
 * '0' defines an logical not
 */
public class Clause {
    String elements;

    public Clause(String clauseBinaryRepresentation) {
        this.elements = clauseBinaryRepresentation;
    }

    public int numberOfClauseElements() {
        return elements.length();
    }

    public boolean evaluateClause(String binaryInput) {
        if (binaryInput.length() != elements.length()) {
            //number of clause elements missmatch
            throw new IllegalArgumentException("Wrong input binary string provided for the given" +
                    "clause : " + binaryInput);
        }


        for (int i = 0; i < elements.length(); i++) {

            char c = elements.charAt(i);
            boolean elementFlag = false;

            if (c == 'x') {
                continue;
            }

            if (binaryInput.charAt(i) == '1') {
                elementFlag = true;
            } else {
                elementFlag = false;
            }

            if (c == '0') {
                elementFlag = !elementFlag;
            }

            if (elementFlag) {
                return true;
            }

        }

        return false;
    }
}
