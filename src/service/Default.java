package service;

import jetty.Params;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api")
public class Default {
    @GET
    @Path("Default")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest() {
        //return Response.status(Response.Status.OK).entity(TestEntityService.get()).type("text/html").build();
        return Response.ok(TestEntityService.getTestAccessor(), Params.MEDIATYPE).build();
    }


}