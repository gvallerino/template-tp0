package ar.fiuba.tdd.template.tp0;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegExGenerator {

    private int maxLength;
    private String regEx;
    private List<String> tokensList;
    private List<String> quantifiersList;

    private String character;
    private String nextCharacter;

    private Boolean literalIsActive = Boolean.FALSE;
    private Boolean setIsActive = Boolean.FALSE;

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

        this.regEx = regEx;
        this.tokenizerRegEx();
        List<String> regExList = new ArrayList<>();

        for (int i = 0; i < numberOfResults; i++) {

            String regExGenerated = this.generateToken();
            regExList.add(regExGenerated);
        }

        return regExList;
    }

    private void tokenizerRegEx() {

        tokensList = new ArrayList<>();
        quantifiersList = new ArrayList<>();

        StringBuilder set = new StringBuilder();

        for (int i = 0; i < regEx.length(); i++) {

            character = Character.toString(regEx.charAt(i));
            nextCharacter = this.getNextCharacter(i);

            if (!this.characterIsQuantifier(character)) {
                set = this.processCharNotQuantifier(set);
            }
        }

    }

    private StringBuilder processCharNotQuantifier(StringBuilder set) {

        if (literalIsActive) {
            tokensList.add(character);
            literalIsActive = Boolean.FALSE;
            quantifiersList.add(this.characterIsQuantifier(nextCharacter) ? nextCharacter : "");

        } else {
            literalIsActive = BACKSLASH.equals(character);
            setIsActive = (setIsActive || SQUARE_BRACKET_OPEN.equals(character));

            if (setIsActive) {

                set = this.processSet(set);
                setIsActive = !(SQUARE_BRACKET_CLOSE.equals(character));
            }

            this.saveCharAndQuantifier();
        }

        return set;
    }

    private void saveCharAndQuantifier() {

        if (POINT.equals(character)) {
            tokensList.add("");
            quantifiersList.add(this.characterIsQuantifier(nextCharacter) ? nextCharacter : "");

        } else if (!this.characterIsSpecial(character) && !setIsActive) {
            tokensList.add(character);
            quantifiersList.add(this.characterIsQuantifier(nextCharacter) ? nextCharacter : "");
        }
    }

    private StringBuilder processSet(StringBuilder set) {

        if (!SQUARE_BRACKET_OPEN.equals(character) && !SQUARE_BRACKET_CLOSE.equals(character)) {
            set.append(character);
        } else if (SQUARE_BRACKET_CLOSE.equals(character)) {

            tokensList.add(set.toString());
            quantifiersList.add(this.characterIsQuantifier(nextCharacter) ? nextCharacter : "");
            set = new StringBuilder();
        }
        return set;
    }

    private String generateToken() {

        if (tokensList.size() != quantifiersList.size()) {
            return "";
        }

        StringBuilder regExGenerated = new StringBuilder();

        for (int i = 0; i < quantifiersList.size(); i++) {

            //get code ascii
            String characters = ("".equals(tokensList.get(i)) ? "H" : tokensList.get(i));
            String quantifier = quantifiersList.get(i);

            int min = this.getMinLimitRandomByQuantifier(quantifier);
            int max = this.getMaxLimitRandomByQuantifier(quantifier);
            regExGenerated.append(this.getCharRandomWithRange(characters, min, max));
        }

        return regExGenerated.toString();
    }

    private String getCharRandomWithRange(String characters, int min, int max) {

        StringBuilder character = new StringBuilder();
        if (min >= max) {
            character.append(characters);

        } else {
            int largeChar = characters.length();
            for (int j = min; j < max; j++) {

                Random random = new Random();
                int randomInt = random.nextInt(largeChar) + min;
                //int randomInt = random.nextInt(max - min) + min;
                character.append(characters.charAt(randomInt));
            }
        }

        return character.toString();
    }

    private String getNextCharacter(int index) {

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

    private int getMinLimitRandomByQuantifier(String quantifier) {
        int min = 0;
        if (PLUS.equals(quantifier)) {
            min = 1;
        }
        return min;
    }

    private int getMaxLimitRandomByQuantifier(String quantifier) {
        int max = 0;
        if (QUESTION_MARK.equals(quantifier)) {
            max = 1;
        } else if (ASTERISK.equals(quantifier)) {
            max = maxLength;
        } else if (PLUS.equals(quantifier)) {
            max = maxLength;
        }
        return max;
    }

}