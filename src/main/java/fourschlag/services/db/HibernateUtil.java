package fourschlag.services.db;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    static {
        try {
            Configuration configuration = new Configuration().configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException he) {
            System.err.println("Error creating Session, check database connection: " + he);
            throw new ExceptionInInitializerError(he);
        }
    }
    protected static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
