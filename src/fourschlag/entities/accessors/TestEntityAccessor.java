package fourschlag.entities.accessors;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import fourschlag.entities.TestEntity;

@Accessor
public interface TestEntityAccessor {

    @Query("SELECT * FROM test")
    Result<TestEntity> getAll();

    @Query("SELECT * FROM test WHERE key = :key ALLOW FILTERING")
    TestEntity getById(@Param("key") String key);

    @Query("SELECT * FROM test WHERE age = :age ALLOW FILTERING")
    Result<TestEntity> getByAge(@Param("age") int age);

    @Query("SELECT * FROM test WHERE name = :name ALLOW FILTERING")
    Result<TestEntity> getByName(@Param("name") String name);

    @Query("INSERT INTO test (key, name, age) values (:key, :name, :age)")
    ResultSet insert(@Param("key") String key, @Param("name") String name, @Param("age") int age);

    @Query("UPDATE test SET name=:name, age=:age  WHERE key = :key")
    ResultSet update(@Param("key") String key, @Param("name") String name, @Param("age") int age);

    @Query("DELETE FROM test WHERE key = :key")
    ResultSet delete(@Param("key") String key);
}