package com.ebetzky.elim;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Deprecated
public class StringCalculatorTestOld {

    private StringCalculator calc = new StringCalculator();

    @Test(expected = IllegalArgumentException.class)
    public void testAddWhenNumbersIsNull() {
        // given
        final String numbers = null;

        // when
        calc.add(numbers);
    }

    @Test
    public void testAddWhenNumbersAreEmpty() {
        // given
        final String numbers = "";
        final int expected = 0;

        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void testAddWhenThereIsOneNumber() {
        // given
        final String numbers = "1";
        final int expected = 1;

        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void testAddWhenThereAreTwoNumbers() {
        // given
        final String numbers = "1,2";
        final int expected = 3;

        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void testAddWhenThereAreMoreThanTwoNumbers() {
        // given
        final String numbers = "1,2,3,4";
        final int expected = 10;

        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void testAddWithNewLineDelimiter() {
        // given
        final String numbers = "1,2\n3,4";
        final int expected = 10;

        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithWrongNewLineAndDelimiterSequence() {
        // given
        final String numbers = "1,2\n,4";

        // when
        calc.add(numbers);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithWrongDelimiterAndNewLineSequence() {
        // given
        final String numbers = "1,2,\n4";

        // when
        int result = calc.add(numbers);
    }

    @Test
    public void testAddWithOneThousand() {
        // given
        final String numbers = "1,2\n1001,4";
        final int expected = 7;

        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNegativeNumbers() {
        // given
        final String numbers = "1,-50,24,-4";

        // when
        calc.add(numbers);
    }

    @Test
    public void testAddWithCustomDelimiter() {
        // given
        final String numbers = "//d\n3d4";
        final int expected = 7;

        // when
        int result = calc.add(numbers);

        // then
        assertEquals(expected, result);
    }

}



