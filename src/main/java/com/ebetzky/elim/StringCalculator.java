package com.ebetzky.elim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.isNull;
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

    private static final String DEFAULT_DELIMITER = ",";
    private static final String NEW_LINE = "\n";
    private static final String DELIMITER_PREFIX = "//";
    private static final String DELIMITER_OPEN_TAG = "[";
    private static final String DELIMITER_CLOSE_TAG = "]";
    private static final Set<Character> REGEX_RESERVED_CHARS = new HashSet<>(asList('[', '\\', '^', '$', '.', '|', '?', '*', '+', '(', ')', '{', '}'));
    private static final int UPPER_LIMIT = 1000;


    public int add(final String numbers) {
        validate(isNull(numbers), "Cannot calculate the sum from null numbers argument.");

        boolean isHeaderDefined = numbers.startsWith(DELIMITER_PREFIX);
        List<String> delimiters = isHeaderDefined ? extractDelimiters(numbers) : singletonList(DEFAULT_DELIMITER);
        String body = isHeaderDefined ? extractBody(numbers) : numbers;

        List<String> delimitedNumbers = splitNumbers(delimiters, singletonList(body));
        validate(containsInvalidSequence(delimitedNumbers),
                "Invalid format in numbers: addends should be separated by either new line or delimiter, but not both.");

        List<Integer> addends = prepareNumberValues(delimitedNumbers);
        validateNegatives(addends);

        final int initialSum = 0;
        return addends.stream()
                .filter(this::qualifiesForAddition)
                .reduce(initialSum, Integer::sum);
    }

    private List<String> extractDelimiters(String numbers) {
        List<String> extractedDelimiters = new ArrayList<>();
        String header = numbers.substring(0, numbers.indexOf(NEW_LINE)).substring(DELIMITER_PREFIX.length());

        if (doesContainBracketDelimiter(header)) {
            while (doesContainBracketDelimiter(header)) {
                int openTagPosition = header.indexOf(DELIMITER_OPEN_TAG);
                int closeTagPosition = header.indexOf(DELIMITER_CLOSE_TAG);

                String extractedDelimiter = header.substring(openTagPosition + 1, closeTagPosition);
                extractedDelimiters.add(extractedDelimiter);
                header = header.substring(closeTagPosition + 1);
            }
        } else {
            extractedDelimiters.add(header);
        }

        return unmodifiableList(
                extractedDelimiters.stream().
                        map(this::escapeRegexChars).
                        collect(toList()));
    }

    private String extractBody(String numbers) {
        return numbers.substring(numbers.indexOf(NEW_LINE) + 1);
    }

    private List<String> splitNumbers(List<String> delimiters, List<String> tokens) {
        final List<String> result = new ArrayList<>();
        final String firstDelimiter = delimiters.get(0);
        final int processedDelimiterCount = 1;

        for (String token : tokens) {
            result.addAll(asList(token.split(firstDelimiter)));
        }

        return delimiters.size() > processedDelimiterCount ? splitNumbers(delimiters.subList(1, delimiters.size()), result) : result;
    }

    private List<Integer> prepareNumberValues(List<String> numbers) {
        return numbers.stream()
                .map(s -> s.split(NEW_LINE))
                .flatMap(Arrays::stream)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(toList());
    }

    private void validate(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateNegatives(List<Integer> addends) {
        List<Integer> negatives = addends.stream().filter(this::isNegative).collect(toList());
        if (!negatives.isEmpty()) {
            String negativesMessage = negatives.stream().map(Object::toString).collect(joining(", "));
            throw new IllegalArgumentException("Negatives are not allowed. Following negative numbers were found: " + negativesMessage);
        }
    }

    private String escapeRegexChars(String delimiter) {
        StringBuilder escapedDelimiter = new StringBuilder();
        
        for (char c : delimiter.toCharArray()) {
            if (REGEX_RESERVED_CHARS.contains(c)) {
                escapedDelimiter.append('\\').append(c);
            } else {
                escapedDelimiter.append(c);
            }
        }

        return escapedDelimiter.toString();
    }

    private boolean doesContainBracketDelimiter(String header) {
        return header.contains(DELIMITER_OPEN_TAG) && header.contains(DELIMITER_CLOSE_TAG);
    }

    private boolean isNegative(Integer i) {
        return i < 0;
    }

    private boolean qualifiesForAddition(Integer i) {
        return i <= UPPER_LIMIT;
    }

    private boolean containsInvalidSequence(List<String> numbers) {
        return numbers.stream().anyMatch(s -> s.startsWith(NEW_LINE) || s.endsWith(NEW_LINE));
    }

}
