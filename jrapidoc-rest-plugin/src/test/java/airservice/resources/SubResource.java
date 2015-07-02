/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package airservice.resources;

import airservice.entity.destination.DestinationInput;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Tomas "sarzwest" Jiricek
 */
public class SubResource {

    String text;

    public SubResource() {
    }

    public SubResource(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @GET
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(MediaType.APPLICATION_JSON)
    public Response getSubResource(DestinationInput d) {
        return Response.status(Response.Status.OK).entity("toto je subresource").build();
    }

    @GET
    @Path("getsomething")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSomething() {
        return Response.status(Response.Status.OK).entity("toto je subresource 2").build();
    }

    public String getText() {
        return text;
    }
}
