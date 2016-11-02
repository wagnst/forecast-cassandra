package service;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import entities.TestEntity;
import entities.accessors.TestEntityAccessor;

import java.util.ArrayList;
import java.util.List;


public class TestEntityService extends Service {

    private TestEntityAccessor accessor;

    public TestEntityService() {
        super("141.19.145.142", "keyspace2");
        MappingManager manager = new MappingManager(this.getSession());
        accessor = manager.createAccessor(TestEntityAccessor.class);
    }

    public List<TestEntity> getAll() {
        List<TestEntity> resultList = new ArrayList<>();
        Result<TestEntity> testEntities = accessor.getAll();

        for (TestEntity e : testEntities) {
            resultList.add(e);
        }

        return resultList;
    }

    public TestEntity getById(String id) {
        TestEntity testEntities = accessor.getById(id);
        return testEntities;
    }

    public List<TestEntity> getByName(String name) {
        List<TestEntity> resultList = new ArrayList<>();
        Result<TestEntity> testEntities = accessor.getByName(name);

        for (TestEntity e : testEntities) {
            resultList.add(e);
        }

        return resultList;
    }

    public List<TestEntity> getByAge(int age) {
        List<TestEntity> resultList = new ArrayList<>();
        Result<TestEntity> testEntities = accessor.getByAge(age);

        for (TestEntity e : testEntities) {
            resultList.add(e);
        }

        return resultList;
    }

    /*
    public static List<TestEntity> getAllTest() {
        List<TestEntity> resultList = new ArrayList<>();
        Mapper<TestEntity> mapper = getMappingManager().mapper(TestEntity.class);
        //prepare the statement
        Statement statement = QueryBuilder.select().all().from("test");

        ResultSet results = getMappingManager().getSession().execute(statement);
        // use auto mapper of datastax
        Result<TestEntity> result = mapper.map(results);

        resultList.addAll(result.all());

        return resultList;
    }

    public static List<TestEntity> getOneTest(String key) {
        List<TestEntity> resultList = new ArrayList<>();
        Mapper<TestEntity> mapper = getMappingManager().mapper(TestEntity.class);
        //prepare the statement
        Statement statement = QueryBuilder.select().all().from("test")
                .where(eq("key", key));

        ResultSet results = getMappingManager().getSession().execute(statement);
        // use auto mapper of datastax
        Result<TestEntity> result = mapper.map(results);

        resultList.addAll(result.all());

        return resultList;
    }
    */
}