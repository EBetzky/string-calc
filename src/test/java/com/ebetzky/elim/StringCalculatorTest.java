package com.ebetzky.elim;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringCalculatorTest {

    // given
    private StringCalculator calc = new StringCalculator();;

    @ParameterizedTest
    @MethodSource("correctInputProvider")
    void testAdd(String numbers, int expected) {
        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

    private static Stream correctInputProvider() {
        return Stream.of(
                Arguments.of("", 0),
                Arguments.of("1", 1),
                Arguments.of("1,2", 3),
                Arguments.of("1,2,3,4", 10),
                Arguments.of("1,2\n3,4", 10),
                Arguments.of("1,2\n1001,4", 7),
                Arguments.of("//d\n3dd4", 7),
                Arguments.of("//[***]\n1***2***3", 6),
                Arguments.of("//[%%]\n1%%2%%3%%4", 10),
                Arguments.of("//[%%][***]\n1***2%%3***4", 10),
                Arguments.of("//[%%][***][&]\n1***2%%3&4", 10),
                Arguments.of("//[%%][***][&]\n1***2%%3&2000", 6)
        );
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("incorrectInputProvider")
    void testAddWithIncorrectInput(String numbers) {
        // when
        assertThrows(IllegalArgumentException.class, () -> {
            calc.add(numbers);
        });
    }

    private static Stream incorrectInputProvider() {
        return Stream.of(
                //Arguments.of(null),
                Arguments.of("1,2\n,4"),
                Arguments.of("1,2,\n4"),
                Arguments.of("1,-50,24,-4")
        );
    }
}
