package fourschlag.services.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaConnection {
    private String persistanceUnitName;
    private EntityManagerFactory factory;

    public JpaConnection(String persistanceUnitName) {
        this.persistanceUnitName = persistanceUnitName;
        factory = Persistence.createEntityManagerFactory(persistanceUnitName);
    }

    public String getPersistanceUnitName() {
        return persistanceUnitName;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }
}
