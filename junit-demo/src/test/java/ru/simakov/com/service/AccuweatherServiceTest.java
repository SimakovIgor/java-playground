package ru.simakov.com.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.simakov.com.client.AccuweatherClient;
import ru.simakov.com.exception.ServiceException;
import ru.simakov.com.model.accuweather.CurrentCondition;
import ru.simakov.com.model.accuweather.LocationRoot;
import ru.simakov.com.model.accuweather.TopCitiesCount;
import support.DataProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AccuweatherServiceTest {

    @Mock
    private AccuweatherClient accuweatherClient;
    @Spy
    private EventService eventService;
    @InjectMocks
    private AccuweatherService accuweatherService;

    @AfterEach
    void afterEach() {
        Mockito.verifyNoMoreInteractions(accuweatherClient, eventService);
    }

    @Test
    void getCurrentConditionByLocationShouldWork() {
        //GIVEN
        final CurrentCondition currentCondition = Mockito.mock(CurrentCondition.class);
        final CurrentCondition[] currentConditions = {currentCondition};

        final LocationRoot request = DataProvider.prepareLocationRoot().build();
        Mockito.when(accuweatherClient.getCurrentConditionsByLocationKey(request.getKey()))
            .thenReturn(currentConditions);

        //WHEN
        final CurrentCondition result = accuweatherService.getCurrentConditionByLocation(request);
        assertThat(result).isEqualTo(currentCondition);

        //THEN
        Mockito.verify(accuweatherClient, Mockito.times(1))
            .getCurrentConditionsByLocationKey(request.getKey());
    }

    @ParameterizedTest
    @EnumSource(
        value = TopCitiesCount.class,
        mode = EnumSource.Mode.EXCLUDE,
        names = {"FIFTY"}
    )
    void getTopCityLocationShouldWork(final TopCitiesCount topCitiesCount) {
        //GIVEN
        final LocationRoot locationRoot = DataProvider.buildLocationRoot().build();
        final LocationRoot[] locationRoots = {locationRoot};

        Mockito.when(accuweatherClient.getTopcities(any(TopCitiesCount.class)))
            .thenReturn(locationRoots);

        //WHEN
        final LocationRoot result = accuweatherService.getTopCityLocation(topCitiesCount);

        //THEN
        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("englishName")
            .isEqualTo(locationRoot);

        Mockito.verify(accuweatherClient).getTopcities(topCitiesCount);
    }

    @Test
    void callWithExceptionShouldThrowServiceException() {
        Assertions.assertThatThrownBy(() -> accuweatherService.callWithException())
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("Smthing go wrong!")
            .hasStackTraceContaining("ru.simakov.com.service.AccuweatherService.callWithException");
    }

    @Test
    void checkAccuweatherShouldWork() {
        //GIVEN
        final LocationRoot locationRoot = DataProvider.buildLocationRoot().build();
        final LocationRoot[] locationRoots = {locationRoot};
        final CurrentCondition currentCondition = DataProvider.prepareCurrentConditions();
        Mockito.when(accuweatherClient.getTopcities(any(TopCitiesCount.class)))
            .thenReturn(locationRoots);
        Mockito.when(accuweatherClient.getCurrentConditionsByLocationKey(any()))
            .thenReturn(new CurrentCondition[]{currentCondition});

        //WHEN
        accuweatherService.checkAccuweather();

        //THEN
        Mockito.verify(accuweatherClient).getTopcities(any());
        Mockito.verify(accuweatherClient).getCurrentConditionsByLocationKey("123");
        verifySendEvent();
    }

    private void verifySendEvent() {
        final ArgumentCaptor<CurrentCondition> captor =
            ArgumentCaptor.forClass(CurrentCondition.class);
        Mockito.verify(eventService).sendEvent(captor.capture());
        assertThat(captor.getValue())
            .isNotNull()
            .satisfies(currentCondition1 -> assertThat(currentCondition1)
                .extracting(CurrentCondition::getEpochTime, CurrentCondition::getWeatherText, CurrentCondition::isHasPrecipitation)
                .containsExactly(123_456_789, "Sunny", false))
            .extracting(CurrentCondition::getTemperature)
            .extracting(CurrentCondition.Temperature::getImperial, CurrentCondition.Temperature::getMetric)
            .containsExactly(
                CurrentCondition.Imperial.builder()
                    .value(77)
                    .unit("Fahrenheit")
                    .unitType(18)
                    .build(),
                CurrentCondition.Metric.builder()
                    .value(25.0)
                    .unit("Celsius")
                    .unitType(17)
                    .build()
            );
    }

}
