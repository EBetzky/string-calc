package com.ebetzky.elim.preprocessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ebetzky.elim.preprocessor.Input.HEADER_PREFIX;
import static com.ebetzky.elim.preprocessor.Input.NEW_LINE;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static java.util.Arrays.asList;

class HeaderPreprocessor {

    private final Input input;

    private static final String DEFAULT_DELIMITER = ",";
    private static final String DELIMITER_OPEN_TAG = "[";
    private static final String DELIMITER_CLOSE_TAG = "]";
    private static final Set<Character> REGEX_RESERVED_CHARS = new HashSet<>(asList('[', '\\', '^', '$', '.', '|', '?', '*', '+', '(', ')', '{', '}'));

    HeaderPreprocessor(Input input) {
        this.input = input;
    }

    List<String> acquireDelimiters() {
        return input.isHeaderDefined ? extractDelimiters(input.unprocessed) : singletonList(DEFAULT_DELIMITER);
    }

    private List<String> extractDelimiters(String unprocessed) {
        List<String> extractedDelimiters = new ArrayList<>();
        String header = extractHeader(unprocessed);

        if (doesContainBracketDelimiter(header)) {
            do {
                int openTagPosition = header.indexOf(DELIMITER_OPEN_TAG);
                int closeTagPosition = header.indexOf(DELIMITER_CLOSE_TAG);

                String extractedDelimiter = header.substring(openTagPosition + 1, closeTagPosition);
                extractedDelimiters.add(extractedDelimiter);
                header = header.substring(closeTagPosition + 1);
            } while (doesContainBracketDelimiter(header));
        } else {
            extractedDelimiters.add(header);
        }

        return unmodifiableList(
                extractedDelimiters.stream().
                        map(this::escapeRegexChars).
                        collect(toList()));
    }

    private String extractHeader(String unprocessed) {
        return unprocessed.substring(0, unprocessed.indexOf(NEW_LINE)).substring(HEADER_PREFIX.length());
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
}
