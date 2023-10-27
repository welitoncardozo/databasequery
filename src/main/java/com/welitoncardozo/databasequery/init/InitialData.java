package com.welitoncardozo.databasequery.init;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class InitialData {
    private static final String DATABASEQUERY_DATA = "databasequery_data";

    @PersistenceContext
    private EntityManager em;

    public void create() {
        final var query = new StringBuilder();
        ddl(query);
        dml(query);

        try (Session session = em.unwrap(Session.class)) {
            session.doWork(db -> {
                final var preparedStatement = db.prepareStatement(query.toString());
                preparedStatement.execute();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ddl(final StringBuilder query) {
        query.append("CREATE SCHEMA IF NOT EXISTS ").append(DATABASEQUERY_DATA).append(";");

        query.append("CREATE TABLE IF NOT EXISTS ").append(DATABASEQUERY_DATA).append(".client ( ");
        query.append("id UUID NOT NULL, ");
        query.append("name TEXT NOT NULL, ");
        query.append("CONSTRAINT pk_client_id PRIMARY KEY (id) ");
        query.append(");");

        query.append("CREATE TABLE IF NOT EXISTS ").append(DATABASEQUERY_DATA).append(".sale ( ");
        query.append("id UUID NOT NULL, ");
        query.append("value NUMERIC(19,4) NOT NULL, ");
        query.append("client UUID, ");
        query.append("CONSTRAINT pk_sale_id PRIMARY KEY (id), ");
        query.append("CONSTRAINT fk_sale_client FOREIGN KEY (client) REFERENCES ").append(DATABASEQUERY_DATA).append(".client ");
        query.append(");");

        query.append("CREATE TABLE IF NOT EXISTS ").append(DATABASEQUERY_DATA).append(".sale_item ( ");
        query.append("id UUID NOT NULL, ");
        query.append("value NUMERIC(19,4) NOT NULL, ");
        query.append("sale UUID, ");
        query.append("CONSTRAINT pk_sale_item_id PRIMARY KEY (id), ");
        query.append("CONSTRAINT fk_sale_item_sale FOREIGN KEY (sale) REFERENCES ").append(DATABASEQUERY_DATA).append(".sale ");
        query.append(");");
    }

    private void dml(final StringBuilder query) {
        query.append("INSERT INTO ")
                .append(DATABASEQUERY_DATA).append(".client (id, name) VALUES")
                .append(" ('8d414d37-f050-479a-bbaf-e07836214ef8', 'Weliton'),")
                .append(" ('e02b638b-26c5-4ffe-819a-99e858d7fad2', 'Pedro'),")
                .append(" ('6b949d7c-3b19-4b43-b0f2-d6927f438478', 'Maria'),")
                .append(" ('6b949d7c-3b19-4b43-b0f2-d6927f438479', 'Ana')")
                .append(";");

        query.append("INSERT INTO ")
                .append(DATABASEQUERY_DATA).append(".sale (id, value, client) VALUES")
                .append(" ('5ea8fae2-ed57-4928-b49f-887e0b5bb3db', 50, '8d414d37-f050-479a-bbaf-e07836214ef8'),")
                .append(" ('698aedae-be8d-422a-ad8e-152c517d71f6', 25.28, '8d414d37-f050-479a-bbaf-e07836214ef8'),")
                .append(" ('cc5a1a2b-1cda-43d4-843f-8998caf7e856', 10, 'e02b638b-26c5-4ffe-819a-99e858d7fad2'),")
                .append(" ('3932399b-1fb9-4bde-ae08-1dd3f0721a3c', 5, null)")
                .append(";");

        query.append("INSERT INTO ")
                .append(DATABASEQUERY_DATA).append(".sale_item (id, value, sale) VALUES")
                .append(" ('e982e22a-bdd9-436b-b140-9fbac4a71771', 30, '5ea8fae2-ed57-4928-b49f-887e0b5bb3db'),")
                .append(" ('688e97ba-cbee-4703-b0aa-b270151ae5f1', 20, '5ea8fae2-ed57-4928-b49f-887e0b5bb3db'),")
                .append(" ('b2ed0b9e-17ff-4286-a800-2560acff6ea2', 25.28, '698aedae-be8d-422a-ad8e-152c517d71f6'),")
                .append(" ('9b6c6b80-4d36-4d04-8bdc-9edf4c091102', 10, 'cc5a1a2b-1cda-43d4-843f-8998caf7e856'),")
                .append(" ('c117bed5-21e0-4898-adfc-8efe12226168', 5, '3932399b-1fb9-4bde-ae08-1dd3f0721a3c')")
                .append(";");
    }

    public void drop() {
        final var query = new StringBuilder();
        query.append("DROP SCHEMA IF EXISTS ").append(DATABASEQUERY_DATA).append(" CASCADE;");

        try (Session session = em.unwrap(Session.class)) {
            session.doWork(db -> {
                final var preparedStatement = db.prepareStatement(query.toString());
                preparedStatement.execute();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
