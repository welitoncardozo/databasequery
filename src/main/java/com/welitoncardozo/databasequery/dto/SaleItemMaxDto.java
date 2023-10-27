package com.welitoncardozo.databasequery.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SaleItemMaxDto {
    private UUID saleId;
    private Double value;
}
