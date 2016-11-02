package fourschlag.services.web.ws;

import fourschlag.services.data.TestEntityService;
import fourschlag.services.web.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/entities")
public class TestEntityWS {

    TestEntityService testEntityService = new TestEntityService();

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            return Response.ok(testEntityService.getAll(), Params.MEDIATYPE).build();
        } finally {
            testEntityService.closeConnection();
        }
    }

    @GET
    @Path("/byId/{testEntityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("testEntityId") String id) {
        try {
            return Response.ok(testEntityService.getById(id), Params.MEDIATYPE).build();
        } finally {
            testEntityService.closeConnection();
        }
    }

    @GET
    @Path("/byName/{testEntityName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByName(@PathParam("testEntityName") String name) {
        try {
            return Response.ok(testEntityService.getByName(name), Params.MEDIATYPE).build();
        } finally {
            testEntityService.closeConnection();
        }
    }

    @GET
    @Path("/byAge/{testEntityAge}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByAge(@PathParam("testEntityAge") int age) {
        try {
            return Response.ok(testEntityService.getByAge(age), Params.MEDIATYPE).build();
        } finally {
            testEntityService.closeConnection();
        }
    }
}