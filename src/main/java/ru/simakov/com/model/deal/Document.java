package ru.simakov.com.model.deal;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Document {
    private String title;
    private DocumentCopyType documentCopyType;
    private List<File> files;
}
