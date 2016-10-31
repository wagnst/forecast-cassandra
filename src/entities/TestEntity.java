package entities;
//https://datastax.github.io/java-driver/manual/object_mapper/using/

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "keyspace2", name = "test",
        readConsistency = "QUORUM",
        writeConsistency = "QUORUM",
        caseSensitiveKeyspace = false,
        caseSensitiveTable = false)
public class TestEntity {
    @PartitionKey
    @Column(name = "key")
    String key;
    @Column(name = "age")
    int age;
    @Column(name = "name")
    String name;

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String getKey() { return this.key; }

    public TestEntity(String key, String name, int age) {
        this.key = key;
        this.name = name;
        this.age = age;
    }
}
