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

    /**
     * Method to check Accuweather conditions for a specific location.
     * The method retrieves the top cities count, finds the corresponding location,
     * and retrieves the current weather conditions for that location.
     * If no conditions are found, an exception is thrown.
     * Finally, the current conditions are sent as an event using the event service.
     */
    public void checkAccuweather() {
        Stream.of(50, 100, 150)
            .findAny()
            .map(TopCitiesCount::findByValue)
            .map(this::getTopCityLocation)
            .map(this::getCurrentConditionByLocation)
            .ifPresent(eventService::sendEvent);
    }

    /**
     * Retrieves the current weather conditions for a specific location using the Accuweather API.
     *
     * @param locationRoot The location for which to retrieve the weather conditions.
     * @return The current weather conditions for the specified location.
     * @throws RuntimeException if no weather conditions are found for the location.
     */
    public CurrentCondition getCurrentConditionByLocation(final LocationRoot locationRoot) {
        return Arrays.stream(accuweatherClient.getCurrentConditionsByLocationKey(locationRoot.getKey()))
            .findFirst()
            .orElseThrow();
    }

    /**
     * Retrieves the top city location based on the given count using the Accuweather API.
     *
     * @param citiesCount The count of top cities for which to retrieve the location.
     * @return The top city location.
     * @throws RuntimeException if no top city location is found.
     */
    public LocationRoot getTopCityLocation(final TopCitiesCount citiesCount) {
        return Arrays.stream(accuweatherClient.getTopcities(citiesCount))
            .findAny()
            .orElseThrow();
    }

    public void callWithException() {
        throw new ServiceException("Smthing go wrong!", null);
    }
}
