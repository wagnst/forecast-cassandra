package service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
public class WebServices {

    @GET
    @Path("/TestEntity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTest() {

        return Response.ok(TestEntityService.get(), Params.MEDIATYPE).build();
    }

    @GET
    @Path("/TestEntity/{testId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneTest(@PathParam("testId") String testId) {

        return Response.ok(TestEntityService.getOneTest(testId), Params.MEDIATYPE).build();
    }
}