package fourschlag.entities.jpaAccessors;

import fourschlag.services.db.JpaConnection;

import javax.persistence.EntityManagerFactory;

public class Accessor {
    private JpaConnection connection;

    public Accessor(JpaConnection connection) {
        this.connection = connection;
    }

    public JpaConnection getConnection() {
        return connection;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return connection.getEntityManagerFactory();
    }
}
