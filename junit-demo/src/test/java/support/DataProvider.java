package support;

import lombok.experimental.UtilityClass;
import ru.simakov.com.model.accuweather.CurrentCondition;
import ru.simakov.com.model.accuweather.LocationRoot;
import ru.simakov.com.model.deal.CreditContractInfo;
import ru.simakov.com.model.deal.Document;
import ru.simakov.com.model.deal.DocumentCopyType;
import ru.simakov.com.model.deal.File;
import ru.simakov.com.model.deal.FileType;

import java.util.Date;
import java.util.List;

@UtilityClass
public class DataProvider {
    public static LocationRoot.LocationRootBuilder buildLocationRoot() {
        return LocationRoot.builder()
            .englishName("englishName")
            .key("123");
    }

    public static LocationRoot.LocationRootBuilder prepareLocationRoot() {
        return LocationRoot.builder()
            .englishName("Moscow")
            .key("123");
    }

    public static CurrentCondition prepareCurrentConditions() {
        return CurrentCondition.builder()
            .localObservationDateTime(new Date())
            .epochTime(123_456_789)
            .weatherText("Sunny")
            .weatherIcon(1)
            .hasPrecipitation(false)
            .precipitationType(null)
            .isDayTime(true)
            .temperature(CurrentCondition.Temperature.builder()
                .metric(CurrentCondition.Metric.builder()
                    .value(25.0)
                    .unit("Celsius")
                    .unitType(17)
                    .build())
                .imperial(CurrentCondition.Imperial.builder()
                    .value(77)
                    .unit("Fahrenheit")
                    .unitType(18)
                    .build())
                .build())
            .mobileLink("https://example.com/mobile")
            .link("https://example.com")
            .build();
    }

    public static CreditContractInfo createSampleCreditContractInfo() {
        return CreditContractInfo.builder()
            .dealName("deal 1")
            .documents(List.of(
                Document.builder()
                    .title("Document 1")
                    .documentCopyType(DocumentCopyType.PASSPORT)
                    .files(List.of(
                        File.builder()
                            .name("file1.txt")
                            .extension("txt")
                            .fileType(FileType.PASSPORT_2_3)
                            .build(),
                        File.builder()
                            .name("file2.txt")
                            .extension("txt")
                            .fileType(FileType.PASSPORT_4_5)
                            .build())
                    )
                    .build(),
                Document.builder()
                    .title("Document 2")
                    .documentCopyType(DocumentCopyType.CLIENT_PHOTO)
                    .files(List.of(
                        File.builder()
                            .name("file3.txt")
                            .extension("txt")
                            .fileType(FileType.PASSPORT_10_11)
                            .build())
                    )
                    .build())
            )
            .build();
    }
}
