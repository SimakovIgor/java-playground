package ru.simakov.com.model.accuweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LocationRoot {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("EnglishName")
    private String englishName;
}

