package fourschlag.entities.jpaAccessors;

import fourschlag.services.db.JpaConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Accessor {
    private JpaConnection connection;

    Accessor(JpaConnection connection) {
        this.connection = connection;
    }

    public JpaConnection getConnection() {
        return connection;
    }

    EntityManagerFactory getEntityManagerFactory() {
        return connection.getEntityManagerFactory();
    }

    EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
}
