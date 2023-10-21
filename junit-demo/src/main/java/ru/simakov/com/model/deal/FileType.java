package ru.simakov.com.model.deal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileType {
    PASSPORT_2_3("PASSPORT_2_3"),
    PASSPORT_4_5("PASSPORT_4_5"),
    PASSPORT_6_7("PASSPORT_6_7"),
    PASSPORT_8_9("PASSPORT_8_9"),
    PASSPORT_10_11("PASSPORT_10_11");
    private final String value;
}
