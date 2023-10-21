package ru.simakov.com.model.deal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {
    private String name;
    private String extension;
    private FileType fileType;
}
