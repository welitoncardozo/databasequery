package com.welitoncardozo.databasequery.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "sale_item", schema = "databasequery_data")
@EqualsAndHashCode
public class SaleItem {
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column
    private UUID id;

    @Column
    private Double value;

    @ManyToOne
    @JoinColumn(name = "sale")
    private Sale sale;
}
