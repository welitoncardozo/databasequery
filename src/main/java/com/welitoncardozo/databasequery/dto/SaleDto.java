package com.welitoncardozo.databasequery.dto;

import com.welitoncardozo.databasequery.entities.Sale;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toUnmodifiableList;

@Getter
public class SaleDto {
    private UUID id;
    private Double value;
    private String clientName;
    private List<SaleItemDto> items;

    public SaleDto(UUID id, Double value) {
        this.id = id;
        this.value = value;
    }

    public SaleDto(Sale sale) {
        this.id = sale.getId();
        this.value = sale.getValue();
        this.clientName = sale.getClient().getName();
        this.items = sale.getItems()
                .stream()
                .map(it -> new SaleItemDto(it.getId(), it.getValue()))
                .collect(toUnmodifiableList());
    }

    @AllArgsConstructor
    @Getter
    public static class SaleItemDto {
        private UUID id;
        private Double value;
    }
}
