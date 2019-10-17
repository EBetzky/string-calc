package com.ebetzky.elim;

import com.ebetzky.elim.preprocessor.NumbersPreprocessor;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 *  Create a simple String calculator with a method int Add(string numbers)
 *      OK The method can take 0, 1 or 2 numbers, and will return their sum (for an empty string it will return 0) for example “” or “1” or “1,2”
 *      OK Start with the simplest test case of an empty string and move to 1 and two numbers
 *      OK Remember to solve things as simply as possible so that you force yourself to write tests you did not think about
 *      OK Remember to refactor after each passing test
 *      OK Allow the Add method to handle an unknown amount of numbers
 *      OK Allow the Add method to handle new lines between numbers (instead of commas).
 *          the following input is ok:  “1\n2,3”  (will equal 6)
 *          the following input is NOT ok:  “1,\n” (not need to prove it - just clarifying)
 *      OK Support different delimiters
 *          to change a delimiter, the beginning of the string will contain a separate line that looks like this:   “//[delimiter]\n[numbers…]”
 *          for example “//;\n1;2” should return three where the default delimiter is ‘;’ .
 *          the first line is optional. all existing scenarios should still be supported
 *      OK Calling Add with a negative number will throw an exception “negatives not allowed” - and the negative that was passed.if there are multiple negatives, show all of them in the exception message
 *      OK Numbers bigger than 1000 should be ignored, so adding 2 + 1001  = 2
 *      OK Delimiters can be of any length with the following format:  “//[delimiter]\n” for example: “//[***]\n1***2***3” should return 6
 *      OK Allow multiple delimiters like this:  “//[delim1][delim2]\n” for example “//[*][%]\n1*2%3” should return 6.
 *          make sure you can also handle multiple delimiters with length longer than one char
 */

public class StringCalculator {

    private static final int UPPER_LIMIT = 1000;

    public int add(final String numbers) {

        List<Integer> addends = (new NumbersPreprocessor(numbers)).acquireNumbers();
        validateNegatives(addends);

        final int initialSum = 0;
        return addends.stream()
                .filter(this::isGreaterThanLimit)
                .reduce(initialSum, Integer::sum);
    }

    private void validateNegatives(List<Integer> addends) {
        List<Integer> negatives = addends.stream().filter(this::isNegative).collect(toList());
        if (!negatives.isEmpty()) {
            String negativesMessage = negatives.stream().map(Object::toString).collect(joining(", "));
            throw new IllegalArgumentException("Negatives are not allowed. Following negative numbers were found: " + negativesMessage);
        }
    }

    private boolean isNegative(Integer i) {
        return i < 0;
    }

    private boolean isGreaterThanLimit(Integer i) {
        return i <= UPPER_LIMIT;
    }
}
