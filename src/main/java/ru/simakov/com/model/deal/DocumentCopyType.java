package ru.simakov.com.model.deal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DocumentCopyType {
    CONSENT_BKI("CONSENT_BKI"),
    CONSENT_BKI_PD("CONSENT_BKI_PD"),
    CONTRACT_EDS("CONTRACT_EDS"),
    PASSPORT("PASSPORT"),
    CLIENT_PHOTO("CLIENT_PHOTO");
    private final String value;
}
