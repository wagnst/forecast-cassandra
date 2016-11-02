package service;

import com.datastax.driver.core.Session;
import service.db.CassandraConnection;

public class Service {

    private CassandraConnection connection;
    private Session session;
    private String ip;
    private String keyspace;

    public Service() {

    }

    public Service(String ip, String keyspace) {
        this.ip = ip;
        this.keyspace = keyspace;
        connection = new CassandraConnection(this.ip, this.keyspace);
        session = connection.getSession();
    }

    public Session getSession() {
        return this.session;
    }

    public String getKeyspace() {
        return this.keyspace;
    }

    public String getIp() {
        return this.ip;
    }
}