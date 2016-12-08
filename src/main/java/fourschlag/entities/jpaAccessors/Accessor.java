package fourschlag.entities.jpaAccessors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Accessor {
    private static final String PERSISTENCE_UNIT_NAME = "fourschlag";

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public static EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    public static String getPersistenceUnitName() {
        return PERSISTENCE_UNIT_NAME;
    }
}
