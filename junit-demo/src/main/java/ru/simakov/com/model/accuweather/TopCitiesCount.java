package ru.simakov.com.model.accuweather;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TopCitiesCount {
    FIFTY(50),
    HUNDRED(100),
    HUNDRED_FIFTY(150);

    private final int value;

    TopCitiesCount(final int value) {
        this.value = value;
    }

    public static TopCitiesCount findByValue(final int value) {
        return Arrays.stream(values())
            .filter(topCitiesCount -> topCitiesCount.getValue() == value)
            .findAny()
            .orElseThrow(() -> new IllegalStateException("Unknown value" + value));
    }
}
