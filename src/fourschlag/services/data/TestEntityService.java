package fourschlag.services.data;

import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import fourschlag.entities.TestEntity;
import fourschlag.entities.accessors.TestEntityAccessor;

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

    public boolean insertEntity(String key, String name, int age) {
        try {
            if (accessor.getById(key) != null) {
                accessor.update(key, name, age);
            } else {
                accessor.insert(key, name, age);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deleteEntity(String key) {
        try {
            accessor.delete(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}