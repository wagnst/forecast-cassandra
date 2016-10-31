package service;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import entities.TestEntity;

import java.util.ArrayList;
import java.util.List;

public class TestEntityService {

    public static List<TestEntity> get() {
        List<TestEntity> list = new ArrayList<>();

        CassandraConnection connection = new CassandraConnection("141.19.145.144", "keyspace2");
        Session session = connection.getSession();

        MappingManager manager = new MappingManager(session);
        Mapper<TestEntity> mapper = manager.mapper(entities.TestEntity.class);

        try {
            ResultSet rs = session.execute("SELECT * FROM test");
            Result<TestEntity> testResults = mapper.map(rs);
            for (TestEntity e : testResults) {
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
    public interface TestAccessorAll {
        @Query("SELECT * FROM test")
        Result<TestEntity> getAll();
    }

    @Accessor
    public interface TestAccessorOne {
        @Query("SELECT * FROM test WHERE key=:key")
        Result<TestEntity> getOne(@Param("key") String key);
    }

    public static Result<TestEntity> getAllTest() {
        TestAccessorAll accessor = getMappingManager().createAccessor(TestAccessorAll.class);
        Result<TestEntity> result = accessor.getAll();

        return result;
    }

    public static Result<TestEntity> getOneTest(String key) {
        TestAccessorOne accessor = getMappingManager().createAccessor(TestAccessorOne.class);
        Result<TestEntity> result = accessor.getOne(key);

        return result;
    }

    private static MappingManager getMappingManager() {
        CassandraConnection connection = new CassandraConnection("141.19.145.144", "keyspace2");
        Session session = connection.getSession();

        MappingManager manager = new MappingManager(session);

        return manager;
    }
}