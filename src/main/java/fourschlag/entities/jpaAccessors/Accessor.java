package fourschlag.entities.jpaAccessors;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Accessor {
    private static final String PERSISTENCE_UNIT_NAME = "fourschlag";

    private static EntityManager entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)
            .createEntityManager();

    public static EntityManager getEntityManager() {
        return entityManager;
    }
}
