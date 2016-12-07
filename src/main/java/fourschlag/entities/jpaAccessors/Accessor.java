package fourschlag.entities.jpaAccessors;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Accessor {
    private static EntityManager entityManager = Persistence.createEntityManagerFactory("fourschlag")
            .createEntityManager();

    public static EntityManager getEntityManager() {
        return entityManager;
    }
}
