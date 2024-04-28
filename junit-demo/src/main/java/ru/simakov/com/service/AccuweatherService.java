package ru.simakov.com.service;

import lombok.RequiredArgsConstructor;
import ru.simakov.com.client.AccuweatherClient;
import ru.simakov.com.exception.ServiceException;
import ru.simakov.com.model.accuweather.CurrentCondition;
import ru.simakov.com.model.accuweather.LocationRoot;
import ru.simakov.com.model.accuweather.TopCitiesCount;

import java.util.Arrays;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class AccuweatherService {
    private final AccuweatherClient accuweatherClient;
    private final EventService eventService;

    public void checkAccuweather() {
        Stream.of(50, 100, 150)
            .findAny()
            .map(TopCitiesCount::findByValue)
            .map(this::getTopCityLocation)
            .map(this::getCurrentConditionByLocation)
            .ifPresent(eventService::sendEvent);
    }

    public CurrentCondition getCurrentConditionByLocation(final LocationRoot locationRoot) {
        return Arrays.stream(accuweatherClient.getCurrentConditionsByLocationKey(locationRoot.getKey()))
            .findFirst()
            .orElseThrow();
    }

    public LocationRoot getTopCityLocation(final TopCitiesCount citiesCount) {
        return Arrays.stream(accuweatherClient.getTopcities(citiesCount))
            .findAny()
            .orElseThrow();
    }

    public void callWithException() {
        throw new ServiceException("Smthing go wrong!", null);
    }
}
