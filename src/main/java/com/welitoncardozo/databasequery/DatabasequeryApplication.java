package com.welitoncardozo.databasequery;

import com.welitoncardozo.databasequery.init.InitialData;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DatabasequeryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatabasequeryApplication.class, args);
    }

    @Component
    @AllArgsConstructor
    static class Run {
        private final InitialData initialData;
        private final QueryDslExample queryDslExample;

        @PostConstruct
        @Transactional
        public void init() {
//            initialData.drop();
//            initialData.create();

            queryDslExample.findAllSaleSimple();
//            queryDslExample.findAllSale();
//            queryDslExample.join();
//            queryDslExample.projection();
//            queryDslExample.group();
//            queryDslExample.filter();
//            queryDslExample.findWithItens();
//            queryDslExample.findWithItensToDto();
//            queryDslExample.factories();
//            queryDslExample.selectOneOrFirst();
//            queryDslExample.selectSomeResult();
//            queryDslExample.selectSubselect();
//            queryDslExample.insert();
//            queryDslExample.update();
//            queryDslExample.delete();
        }
    }
}
