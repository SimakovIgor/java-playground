package ru.simakov.com.model.deal;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreditContractInfo {
    private String dealName;
    private List<Document> documents;
}
