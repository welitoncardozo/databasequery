package com.welitoncardozo.databasequery.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class SaleDtoSimple {
    @Setter
    private UUID id;

    private Double value;

    public SaleDtoSimple() {
    }

    public SaleDtoSimple(UUID id, Double value) {
        this.id = id;
        this.value = value;
    }
}
