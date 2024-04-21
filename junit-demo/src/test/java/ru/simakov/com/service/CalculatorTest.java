package ru.simakov.com.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    static Stream<Arguments> addShouldBeSuccessProvider() {
        return Stream.of(
            Arguments.of(1, 3, 4L),
            Arguments.of(-14, -28, -42L),
            Arguments.of(0, 0, 0L),
            Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE, 4294967294L)
        );
    }

    @ParameterizedTest
    @MethodSource("addShouldBeSuccessProvider")
    @DisplayName("Успешный кейс сложение положительных чисел")
    void addShouldBeSuccess(final int x, final int y, final long expected) {
        long fact = calculator.add(x, y);

        Assertions.assertThat(fact)
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("Деление на ноль")
    void divideByZeroShouldBeError() {
        Assertions.assertThatThrownBy(() -> calculator.divide(1, 0))
            .isInstanceOf(ArithmeticException.class)
            .hasMessage("/ by zero");
    }

}
