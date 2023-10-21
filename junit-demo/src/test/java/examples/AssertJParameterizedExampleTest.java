package examples;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class AssertJParameterizedExampleTest {

    static Stream<Arguments> provideNumbersForSubtraction() {
        return Stream.of(
            Arguments.of(5, 3, 2),
            Arguments.of(10, 7, 3),
            Arguments.of(8, 8, 0)
        );
    }

    static Stream<Arguments> provideNumbersForAddition() {
        return Stream.of(
            Arguments.of(1, 2, 3),
            Arguments.of(-5, 5, 0),
            Arguments.of(0, 0, 0)
        );
    }

    static Stream<Arguments> provideSecondNumbersForAddition() {
        return Stream.of(
            Arguments.of(3, 3, 6),
            Arguments.of(1, 9, 10),
            Arguments.of(-1, 1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNumbersForSubtraction")
    void testSubtraction(final int a, final int b, final int expected) {
        int result = a - b;
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource({"provideNumbersForAddition", "provideSecondNumbersForAddition"})
    void testAddition(final int a, final int b, final int expected) {
        int result = a + b;
        Assertions.assertThat(result).isEqualTo(expected);
    }
}
