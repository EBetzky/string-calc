package com.ebetzky.elim.preprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ebetzky.elim.preprocessor.Input.NEW_LINE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class NumbersPreprocessor {

    private final Input input;
    private final HeaderPreprocessor headerPreprocessor;

    public NumbersPreprocessor(String unprocessed) {
        this.input = new Input(unprocessed);
        this.headerPreprocessor = new HeaderPreprocessor(input);
    }

    public List<Integer> acquireNumbers() {
        final String body = input.isHeaderDefined ? extractBody(input.unprocessed) : input.unprocessed;

        List<String> delimiters = headerPreprocessor.acquireDelimiters();
        List<String> delimitedNumbers = splitNumbers(delimiters, singletonList(body));
        input.validate(containsInvalidSequence(delimitedNumbers),
                "Invalid format in numbers: addends should be separated by either new line or delimiter, but not both.");

        return prepareNumberValues(delimitedNumbers);
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

    private String extractBody(String numbers) {
        return numbers.substring(numbers.indexOf(NEW_LINE) + 1);
    }

    private boolean containsInvalidSequence(List<String> numbers) {
        return numbers.stream().anyMatch(s -> s.startsWith(NEW_LINE) || s.endsWith(NEW_LINE));
    }
}
