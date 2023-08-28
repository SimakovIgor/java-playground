package examples;

import lombok.Builder;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

class AssertJCommonExampleTest {

    private static BookDto prepareBookDto() {
        return BookDto.builder()
            .title("Title")
            .author("Author")
            .build();
    }

    private static Book prepareBook() {
        return Book.builder()
            .title("Title")
            .author("Author")
            .build();
    }

    @Test
    void testStrictComparison() {
        Book book = prepareBook();
        BookDto bookDto = prepareBookDto();

        Assertions.assertThat(book)
            .usingRecursiveComparison(
                RecursiveComparisonConfiguration.builder()
                    .withStrictTypeChecking(true)
                    .build()
            )
            .isNotEqualTo(bookDto);
    }

    @Test
    void testLenientComparison() {
        Book book = prepareBook();
        BookDto bookDto = prepareBookDto();

        Assertions.assertThat(book)
            .usingRecursiveComparison()
            .isEqualTo(bookDto);
    }

    @Data
    @Builder
    private static class Book {
        private String title;
        private String author;
    }

    @Data
    @Builder
    private static class BookDto {
        private String title;
        private String author;
    }
}
