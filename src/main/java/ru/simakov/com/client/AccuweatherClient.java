package ru.simakov.com.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.simakov.com.model.accuweather.CurrentCondition;
import ru.simakov.com.model.accuweather.LocationRoot;
import ru.simakov.com.model.accuweather.TopCitiesCount;

import java.io.IOException;

@RequiredArgsConstructor
public class AccuweatherClient {
    private static final String BASE_URL = "http://dataservice.accuweather.com";
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public LocationRoot[] getTopcities(final TopCitiesCount citiesCount) {
        String httpUrl = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addPathSegment("currentconditions")
                .addPathSegment("v1")
                .addPathSegment("topcities")
                .addPathSegment(String.valueOf(citiesCount.getValue()))
                .addQueryParameter("apikey", apiKey)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        TypeReference<LocationRoot[]> locationRootTypeReference = new TypeReference<>() {
        };

        return call(request, locationRootTypeReference);
    }

    public CurrentCondition[] getCurrentConditionsByLocationKey(final String locationKey) {
        String url = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addPathSegment("currentconditions")
                .addPathSegment("v1")
                .addPathSegment(locationKey)
                .addQueryParameter("apikey", apiKey)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        TypeReference<CurrentCondition[]> currentConditionsTypeReference = new TypeReference<>() {
        };

        return call(request, currentConditionsTypeReference);
    }

    private <T> T call(final Request request, final TypeReference<T> typeReference) {
        try {
            System.out.println("Sending rq... " + request);
            Response response = okHttpClient.newCall(request).execute();
            System.out.println("Receive rs... " + response);
            String responseString = response.body().string();
            System.out.println("Receive json... " + responseString);
            return objectMapper.readValue(responseString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}