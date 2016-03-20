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

    private static final String DOT = ".";
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

        List<String> regExList = new ArrayList<>();
        this.regEx = regEx;

        this.tokenizerRegEx();

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

        if (DOT.equals(character)) {
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

            String characters = ("".equals(tokensList.get(i)) ? this.getCharRandomAscii() : tokensList.get(i));
            String quantifier = quantifiersList.get(i);

            int min = this.getMinLimitRandomByQuantifier(quantifier);
            int max = this.getMaxLimitRandomByQuantifier(quantifier);
            String charsGenerated = this.getCharRandomWithRangeByCharacters(characters, min, max);
            regExGenerated.append(charsGenerated);
        }

        return regExGenerated.toString();
    }

    private String getCharRandomWithRangeByCharacters(String characters, int min, int max) {

        StringBuilder characterBd = new StringBuilder();
        if (min >= max) {

            if (characters.length() > 1) {

                int largeChar = characters.length();
                int indexCharacters = this.getNumberRandom(0, largeChar - 1);
                characterBd.append(characters.charAt(indexCharacters));
            } else {
                characterBd.append(characters);
            }

        } else {

            int range = this.getNumberRandom(min, max);
            for (int j = 0; j < range; j++) {

                int largeChar = characters.length();
                int indexCharacters = this.getNumberRandom(0, largeChar - 1);
                characterBd.append(characters.charAt(indexCharacters));
            }
        }

        return characterBd.toString();
    }

    private int getNumberRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(1 + max - min) + min;
    }

    private String getCharRandomAscii() {
        int minAscii = 32;
        int maxAscii = 255;

        int randomInt = this.getNumberRandom(minAscii, maxAscii);
        return Character.toString((char)randomInt);
    }

    private String getNextCharacter(int index) {

        int nextIndex = index + 1;
        if (nextIndex >= regEx.length()) {
            return null;
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
        Boolean isDot = DOT.equals(character);
        Boolean isSquareOpen = SQUARE_BRACKET_OPEN.equals(character);
        Boolean isSquareClose = SQUARE_BRACKET_CLOSE.equals(character);
        Boolean isBacklash = BACKSLASH.equals(character);
        return (isDot || isSquareOpen || isSquareClose || isBacklash);
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