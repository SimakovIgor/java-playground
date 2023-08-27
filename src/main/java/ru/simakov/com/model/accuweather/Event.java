package ru.simakov.com.model.accuweather;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private int id;
    private String name;
}
