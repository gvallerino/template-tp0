package ar.fiuba.tdd.template.tp0;


import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {

    private int maxLength;

    public RegExGenerator(int maxLength) {

        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) {

        List<String> regExList = new ArrayList<>();

        for (int i = 0; i < numberOfResults; i++) {
            String regExGenerated = this.generateToken(regEx);
            regExList.add(regExGenerated);
        }

        return regExList;
    }

    private String generateToken(String regEx) {
        int random = (int) (Math.random() * regEx.length());

        if (random > this.maxLength) {
            return Character.toString(regEx.charAt(maxLength));
        }
        return Character.toString(regEx.charAt(random));
    }

}