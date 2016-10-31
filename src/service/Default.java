package service;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")
public class Default {
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String get() {
        CassandraConnection connection = new CassandraConnection("141.19.145.144", "keyspace2");
        Session session = connection.getSession();
        ResultSet results = session.execute("SELECT sum(age) AS summe FROM test");
        StringBuilder sb = new StringBuilder();
        for (Row row: results) {
            sb.append(row.getInt("summe") + "\n");
        }
        connection.closeConnection();
        return sb.toString();
    }
}