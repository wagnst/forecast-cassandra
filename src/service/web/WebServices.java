package service.web;

import service.TestEntityService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/entities")
public class WebServices {

    TestEntityService testEntityService = new TestEntityService();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(testEntityService.getAll(), Params.MEDIATYPE).build();
    }

    @GET
    @Path("/byId/{testEntityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("testEntityId") String id) {
        return Response.ok(testEntityService.getById(id), Params.MEDIATYPE).build();
    }

    @GET
    @Path("/byName/{testEntityName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByName(@PathParam("testEntityName") String name) {
        return Response.ok(testEntityService.getByName(name), Params.MEDIATYPE).build();
    }

    @GET
    @Path("/byAge/{testEntityAge}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByAge(@PathParam("testEntityAge") int age) {
        return Response.ok(testEntityService.getByAge(age), Params.MEDIATYPE).build();
    }
}