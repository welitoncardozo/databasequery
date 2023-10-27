package com.welitoncardozo.databasequery.repositories;

import com.welitoncardozo.databasequery.entities.Client;
import com.welitoncardozo.databasequery.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID>, QuerydslPredicateExecutor<Sale> {
    Optional<Sale> findByClient(final Client client);

    Sale findByClient_Id(final UUID client);

    boolean existsByClient_Id(final UUID client);

    @Query("SELECT sale FROM Sale sale")
    List<Sale> findAllJpql();

    @Query(value = "SELECT * FROM databasequery_data.sale", nativeQuery = true)
    List<Sale> findAllNative();

    @Query(
            " SELECT sale" +
            " FROM Sale sale" +
            " INNER JOIN Client client" +
            "   ON sale.client = client" +
            " WHERE sale.value >= ?1"
    )
    List<Sale> findAllJpql(final Double value);

    @Query(
            value = " SELECT * " +
                    " FROM databasequery_data.sale sale" +
                    " INNER JOIN databasequery_data.client client" +
                    "   ON sale.client = client.id" +
                    " WHERE sale.value >= ?1"
            , nativeQuery = true
    )
    List<Sale> findAllNative(final Double value);
}
