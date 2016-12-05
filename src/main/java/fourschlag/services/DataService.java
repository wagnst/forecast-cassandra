package fourschlag.services;

import fourschlag.entities.types.mysql.QueryStructure;
import fourschlag.services.db.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

public interface DataService {
    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    static <T> List<T> selectQuery(QueryStructure hql, Map<String, Object> parameter) {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery(hql.getQuery());
            parameter.entrySet().forEach(e -> query.setParameter(e.getKey(), e.getValue()));
            return query.list();
        } finally {
            session.close();
        }
    }
}
