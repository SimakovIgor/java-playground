package examples;

import org.junit.jupiter.api.Test;
import ru.simakov.com.model.deal.CreditContractInfo;
import ru.simakov.com.model.deal.Document;
import ru.simakov.com.model.deal.File;
import ru.simakov.com.model.deal.FileType;
import support.DataProvider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AssertjListExampleTest {

    @Test
    void testFileNamesContainsExactlyInAnyOrder() {
        assertThat(DataProvider.createSampleCreditContractInfo())
            .isNotNull()
            .satisfies(deal1 -> {
                assertThat(deal1.getDealName()).isEqualTo("deal 1");
                assertThat(deal1.getDocuments())
                    .hasSize(2)
                    .flatExtracting(Document::getFiles)
                    .hasSize(3)
                    .extracting(File::getName)
                    .containsExactlyInAnyOrder("file3.txt", "file1.txt", "file2.txt")
                    .doesNotHaveDuplicates()
                    .doesNotContain("file4.txt");
            });
    }

    @Test
    void testFilesUsingRecursiveComparison() {
        assertThat(DataProvider.createSampleCreditContractInfo())
            .isNotNull()
            .extracting(CreditContractInfo::getDocuments)
            .satisfies(documents1 -> assertThat(documents1)
                .isNotEmpty()
                .filteredOn(document -> "Document 1" .equals(document.getTitle()))
                .hasSize(1)
                .extracting(Document::getFiles)
                .containsExactly(List.of(
                        File.builder()
                            .name("file1.txt")
                            .extension("txt")
                            .fileType(FileType.PASSPORT_2_3)
                            .build(),
                        File.builder()
                            .name("file2.txt")
                            .extension("txt")
                            .fileType(FileType.PASSPORT_4_5)
                            .build()
                    )
                )
            );
    }
}
