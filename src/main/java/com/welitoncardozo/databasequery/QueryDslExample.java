package com.welitoncardozo.databasequery;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.welitoncardozo.databasequery.dto.SaleClientDto;
import com.welitoncardozo.databasequery.dto.SaleDto;
import com.welitoncardozo.databasequery.dto.SaleDtoSimple;
import com.welitoncardozo.databasequery.dto.SaleItemMaxDto;
import com.welitoncardozo.databasequery.entities.QClient;
import com.welitoncardozo.databasequery.entities.QSale;
import com.welitoncardozo.databasequery.entities.QSaleItem;
import com.welitoncardozo.databasequery.repositories.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Map;

import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class QueryDslExample {
    private static final QSale SALE = QSale.sale;
    private static final QSaleItem SALE_ITEM = QSaleItem.saleItem;
    private static final QClient CLIENT = QClient.client;

    private final EntityManager em;
    private final SaleRepository saleRepository;

    public void findAllSaleSimple() {
//        final var repositoryAll = saleRepository.findAll();
//        System.out.printf("repositoryAll: %s%n", repositoryAll.size());

        final var queryDsl = new JPAQueryFactory(em)
                .selectFrom(SALE)
                .fetch();
        System.out.printf("queryDsl: %s%n", queryDsl.size());

        final var jpqlAll = saleRepository.findAllJpql();
        System.out.printf("jpqlAll: %s%n", jpqlAll.size());

        final var nativeAll = saleRepository.findAllNative();
        System.out.printf("nativeAll: %s%n", nativeAll.size());
    }

    public void findAllSale() {
        final var valueFilter = 1d;

        final var queryDsl = new JPAQueryFactory(em)
                .selectFrom(SALE)
                .innerJoin(SALE.client, CLIENT)
                .where(SALE.value.goe(valueFilter))
                .fetch();
        System.out.printf("queryDsl: %s%n", queryDsl.size());

        final var jpqlAll = saleRepository.findAllJpql(valueFilter);
        System.out.printf("jpqlAll: %s%n", jpqlAll.size());

        final var nativeAll = saleRepository.findAllNative(valueFilter);
        System.out.printf("nativeAll: %s%n", nativeAll.size());
    }

    public void join() {
        final var innerJoinClient = new JPAQueryFactory(em)
                .select(Projections.fields(SaleClientDto.class, SALE.value.as("valueSale"), CLIENT.name.as("clientName")))
                .from(SALE)
                .innerJoin(SALE.client, CLIENT)
//                .innerJoin(CLIENT).on(SALE.client.eq(CLIENT))
                .fetch();
        System.out.printf("innerJoinClient: %s%n", innerJoinClient.size());

        final var leftJoinClient = new JPAQueryFactory(em)
                .select(Projections.fields(SaleClientDto.class, SALE.value.as("valueSale"), CLIENT.name.as("clientName")))
                .from(SALE)
                .leftJoin(SALE.client, CLIENT)
                .fetch();
        System.out.printf("leftJoinClient: %s%n", leftJoinClient.size());

        final var rightJoinClient = new JPAQueryFactory(em)
                .select(Projections.fields(SaleClientDto.class, SALE.value.as("valueSale"), CLIENT.name.as("clientName")))
                .from(SALE)
                .rightJoin(SALE.client, CLIENT)
                .fetch();
        System.out.printf("rightJoinClient: %s%n", rightJoinClient.size());

        final var rightJoinSale = new JPAQueryFactory(em)
                .select(Projections.fields(SaleClientDto.class, SALE.value.as("valueSale"), CLIENT.name.as("clientName")))
                .from(CLIENT)
                .rightJoin(SALE).on(CLIENT.eq(SALE.client))
                .fetch();
        System.out.printf("rightJoinSale: %s%n", rightJoinSale.size());
    }

    public void projection() {
        final var projectionBean = new JPAQueryFactory(em)
                .select(Projections.bean(SaleDtoSimple.class, SALE.id, SALE.value))
                .from(SALE)
                .fetch();
        projectionBean.forEach(it -> {
            System.out.println("projectionBean");
            System.out.printf("ID: %s%n", it.getId());
            System.out.printf("Value: %s%n", it.getValue());
        });

        final var projectionConstructor = new JPAQueryFactory(em)
                .select(Projections.constructor(SaleDtoSimple.class, SALE.id, SALE.value))
                .from(SALE)
                .fetch();
        projectionConstructor.forEach(it -> {
            System.out.println("projectionConstructor");
            System.out.printf("ID: %s%n", it.getId());
            System.out.printf("Value: %s%n", it.getValue());
        });

        final var projectionFields = new JPAQueryFactory(em)
                .select(Projections.fields(SaleDtoSimple.class, SALE.id, SALE.value))
                .from(SALE)
                .fetch();
        projectionFields.forEach(it -> {
            System.out.println("projectionFields");
            System.out.printf("ID: %s%n", it.getId());
            System.out.printf("Value: %s%n", it.getValue());
        });

        final var projectionTuple = new JPAQueryFactory(em)
                .select(SALE.id, SALE.value)
                .from(SALE)
                .fetch();
        projectionTuple.forEach(it -> {
            System.out.println("projectionTuple");
            System.out.printf("ID: %s%n", it.get(SALE.id));
            System.out.printf("Value: %s%n", it.get(SALE.value));
        });
    }

    public void group() {
        final var group = new JPAQueryFactory(em)
                .select(Projections.fields(SaleItemMaxDto.class, SALE.id.as("saleId"), SALE_ITEM.value.max().as("value")))
                .from(SALE_ITEM)
                .innerJoin(SALE_ITEM.sale, SALE)
                .groupBy(SALE.id)
                .fetch();
        System.out.printf("Group: %s%n", group.size());
    }

    public void filter() {
        final var filter = Map.of(fromString("5ea8fae2-ed57-4928-b49f-887e0b5bb3db"), 20d, fromString("698aedae-be8d-422a-ad8e-152c517d71f6"), 10d)
                .entrySet()
                .stream()
                .map(it -> SALE_ITEM.sale.id.eq(it.getKey()).and(SALE_ITEM.value.gt(it.getValue())))
                .reduce(BooleanExpression::or)
                .orElseThrow();
        final var result = new JPAQueryFactory(em)
                .selectFrom(SALE_ITEM)
                .where(filter)
                .fetch();
        System.out.printf("filter: %s%n", result.size());
    }

    public void findWithItens() {
        final var queryDsl = new JPAQueryFactory(em)
                .selectFrom(SALE)
                .innerJoin(SALE.client, CLIENT)
                .where(SALE.value.gt(25))
                .fetch();

        System.out.printf("queryDsl: %s%n", queryDsl.size());
        queryDsl.forEach(sale -> {
            System.out.printf("ID: %s%n", sale.getId());
            System.out.printf("VALUE: %s%n", sale.getValue());

            sale.getItems().forEach(it -> {
                System.out.printf("ID ITEM: %s%n", it.getId());
                System.out.printf("VALUE ITEM: %s%n", it.getValue());
            });
        });
    }

    public void findWithItensToDto() {
        final var queryDsl = new JPAQueryFactory(em)
                .select(SALE)
                .from(SALE)
                .innerJoin(SALE.client, CLIENT)
                .where(SALE.value.gt(25))
                .fetch()
                .stream()
                .map(SaleDto::new)
                .collect(toList());

        System.out.printf("queryDsl: %s%n", queryDsl.size());
        queryDsl.forEach(sale -> {
            System.out.println("------------");
            System.out.printf("ID SALE: %s%n", sale.getId());
            System.out.printf("VALUE SALE: %s%n", sale.getValue());

            sale.getItems().forEach(it -> {
                System.out.printf("ID ITEM: %s%n", it.getId());
                System.out.printf("VALUE ITEM: %s%n", it.getValue());
            });
            System.out.println("------------");
        });
    }
}
