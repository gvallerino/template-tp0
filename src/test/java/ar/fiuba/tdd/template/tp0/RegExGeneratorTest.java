package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private boolean validate(String regEx, int numberOfResults) {

        int maxLength = 10;
        RegExGenerator generator = new RegExGenerator(maxLength);

        List<String> results = null;
        try {
            results = generator.generate(regEx, numberOfResults);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                        (acc, item) -> {
                            Matcher matcher = pattern.matcher(item);
                            return acc && matcher.find();
                        },
                        (item1, item2) -> item1 && item2);
    }

    @Test
    public void testAnyCharacter() {
        assertTrue(validate(".", 1));
    }

    @Test
    public void testMultipleCharacters() {
        assertTrue(validate("...", 1));
    }

    @Test
    public void testLiteral() {
        assertTrue(validate("\\@", 1));
    }

    @Test
    public void testLiteralDotCharacter() {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        assertTrue(validate("[abc]+", 1));
    }

    private String getStringGenerated(String regEx) {
        RegExGenerator generator = new RegExGenerator(10);
        try {
            return generator.generate(regEx,1).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testNumbersOfResultsByQuantifierQuestionMark() {
        String result = this.getStringGenerated("a?");
        assertTrue(result.length() <= 1);
    }

    @Test
    public void testResultByQuantifierQuestionMark() {
        String result = this.getStringGenerated("a?");
        boolean isResultValid = (result.length() == 0 ? "".equals(result) : "a".equals(result));
        assertTrue(isResultValid);
    }

    @Test
    public void testNumbersOfResultsByQuantifierAsterisk() {
        String result = this.getStringGenerated("a*");
        assertTrue(result.length() >= 0);
    }

    @Test
    public void testResultByQuantifierAsterisk() {
        String result = this.getStringGenerated("a*");
        boolean isResultValid = true;

        if (result.length() == 0) {
            isResultValid =  "".equals(result);
        } else {
            for (int i = 0; i < result.length() && isResultValid; i++) {
                isResultValid = ("a".equals(Character.toString(result.charAt(i))));
            }
        }
        assertTrue(isResultValid);
    }

    @Test
    public void testNumbersOfResultsByQuantifierPlus() {
        String result = this.getStringGenerated("a+");
        assertTrue(result.length() >= 1);
    }

    @Test
    public void testResultByQuantifierPlus() {
        String result = this.getStringGenerated("a+");
        boolean isResultValid = true;

        for (int i = 0; i < result.length() && isResultValid; i++) {
            isResultValid = ("a".equals(Character.toString(result.charAt(i))));
        }
        assertTrue(isResultValid);
    }

    @Test
    public void testLiteralEscaped() {
        assertTrue(validate("\\\\", 1));
    }

    @Test
    public void testLiteralDot() {
        assertTrue(validate("\\.", 1));
    }

    @Test
    public void testSetWithFourMatches() {
        assertTrue(validate("[abc]?", 4));
    }

    @Test
    public void testExample() {
        assertTrue(validate("..[ab]*d?c", 10));
    }

    @Test
    public void testRegExNullShouldBeNull() {
        String result = this.getStringGenerated(null);
        assertTrue(result == null);
    }

    @Test
    public void testRegExEmptyShouldBeNull() {
        String result = this.getStringGenerated(null);
        assertTrue(result == null);
    }

    @Test
    public void testRegExIncorrectByDoubleSquareOpenShouldBeNull() {
        String result = this.getStringGenerated("abc[s[ab]");
        assertTrue(result == null);
    }

    @Test
    public void testRegExIncorrectByDoubleSquareCloseShouldBeNull() {
        String result = this.getStringGenerated("abc[ab]s]");
        assertTrue(result == null);
    }

    @Test
    public void testRegExIncorrectByDoubleQuantifierPlusShouldBeNull() {
        String result = this.getStringGenerated("abc[ab]s++w");
        assertTrue(result == null);
    }

    @Test
    public void testRegExIncorrectByDoubleQuantifierAsteriskShouldBeNull() {
        String result = this.getStringGenerated("abc**w");
        assertTrue(result == null);
    }

    @Test
    public void testRegExIncorrectByDoubleQuantifierQuestionMarkShouldBeNull() {
        String result = this.getStringGenerated("a??b");
        assertTrue(result == null);
    }

}
