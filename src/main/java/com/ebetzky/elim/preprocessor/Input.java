package com.ebetzky.elim.preprocessor;

import static java.util.Objects.isNull;

class Input {
    static final String HEADER_PREFIX = "//";
    static final String NEW_LINE = "\n";

    final String unprocessed;
    final boolean isHeaderDefined;

    Input(String unprocessed) {
        validate(isNull(unprocessed), "Cannot calculate the sum from null numbers argument.");
        this.unprocessed = unprocessed;
        this.isHeaderDefined = unprocessed.startsWith(HEADER_PREFIX);
    }

    void validate(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
