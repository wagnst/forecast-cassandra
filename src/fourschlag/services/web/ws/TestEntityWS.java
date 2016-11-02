package fourschlag.services.web.ws;

import fourschlag.services.data.TestEntityService;
import fourschlag.services.web.Params;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/entities")
public class TestEntityWS {

    TestEntityService testEntityService = new TestEntityService();

    /*
    * read data methods
    * */

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

    /*
    * data modification methods
    * */

    /* delete data by id */
    @DELETE
    @Path("/{testEntityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("testEntityId") String id) {
        if (testEntityService.deleteEntity(id)) {
            return Response.status(Response.Status.ACCEPTED).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /* create entity */
    @POST
    @Path("/add")
    public Response create(@FormParam("key") String key, @FormParam("name") String name, @FormParam("age") int age) {
        if (testEntityService.insertEntity(key, name, age)) {
            return Response.status(Response.Status.ACCEPTED).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}