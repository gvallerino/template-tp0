package ar.fiuba.tdd.template.tp0;


import java.util.ArrayList;
import java.util.List;

public class RegExGenerator {

    private int maxLength;

    private static final String POINT = ".";
    private static final String SQUARE_BRACKET_OPEN = "[";
    private static final String SQUARE_BRACKET_CLOSE = "]";
    private static final String BACKSLASH = "\\";

    private static final String ASTERISK = "*";
    private static final String PLUS = "+";
    private static final String QUESTION_MARK = "?";


    public RegExGenerator(int maxLength) {

        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) {

//      Map<String, List<String>> mapRegEx = this.generateMapsOfRegEx(regEx);
        List<String> regExList = new ArrayList<>();

        for (int i = 0; i < numberOfResults; i++) {

//          String regExGenerated = this.generateToken(mapRegEx);
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

    private String getNextCharacter(String regEx, int index) {

        int nextIndex = index + 1;
        if (nextIndex >= regEx.length()) {
            return "";
        }
        return Character.toString(regEx.charAt(nextIndex));
    }

    private Boolean characterIsQuantifier(String character) {
        Boolean isAsterisk = (ASTERISK.equals(character));
        Boolean isPlus = (PLUS.equals(character));
        Boolean isQuestionMark = (QUESTION_MARK.equals(character));
        return (isAsterisk || isPlus || isQuestionMark);
    }

    private Boolean characterIsSpecial(String character) {
        Boolean isPoint = POINT.equals(character);
        Boolean isSquareOpen = SQUARE_BRACKET_OPEN.equals(character);
        Boolean isSquareClose = SQUARE_BRACKET_CLOSE.equals(character);
        Boolean isBacklash = BACKSLASH.equals(character);
        return (isPoint || isSquareOpen || isSquareClose || isBacklash);
    }

    /*
    private Map<String,List<String>> generateMapsOfRegEx(String regEx);

    private String generateToken(Map<String,List<String>> mapRegEx);

     */
}