package service;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import entities.TestEntity;

import java.util.ArrayList;
import java.util.List;

public class TestEntityService {

    public static List<TestEntity> get() {
        List<TestEntity> list = new ArrayList<TestEntity>();

        CassandraConnection connection = new CassandraConnection("141.19.145.144", "keyspace2");
        Session session = connection.getSession();

        MappingManager manager = new MappingManager(session);
        Mapper<TestEntity> mapper = manager.mapper(entities.TestEntity.class);

        try {
            ResultSet rs = session.execute("SELECT * FROM test");
            Result<TestEntity> testResults = mapper.map(rs);
            for (TestEntity e:testResults) {
                list.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.closeConnection();
        }
        return list;
    }

    @Accessor
    public interface TestAccessor {
        @Query("SELECT * FROM test")
        Result<TestEntity> getAll();
    }

    public static Result<TestEntity> getTestAccessor() {
        CassandraConnection connection = new CassandraConnection("141.19.145.144", "keyspace2");
        Session session = connection.getSession();

        MappingManager manager = new MappingManager(session);

        TestAccessor testAccessor = manager.createAccessor(TestAccessor.class);
        Result<TestEntity> testEntities = testAccessor.getAll();

        return testEntities;
    }

}
