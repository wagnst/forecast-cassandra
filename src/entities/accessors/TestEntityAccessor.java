package entities.accessors;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import entities.TestEntity;

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

    //@Query("INSERT INTO complex.accounts (email, name, addr) values (:email, :name, :addr)")
    //ResultSet insert(@Param("email") String email, @Param("name") String name, @Param("addr") Address addr);

    //@Query("UPDATE complex.accounts SET name=:name, addr=:addr  WHERE email = :email")
    //ResultSet update(@Param("email") String email, @Param("name") String name, @Param("addr") Address addr);

    //@Query("DELETE FROM complex.accounts WHERE email = :email")
    //ResultSet delete(@Param("email") String email);
}