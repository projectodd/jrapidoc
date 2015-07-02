/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airservice.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tomas "sarzwest" Jiricek
 */
public interface ParentInterface {

    @GET
    @Path("inheritancefrominterface")
    public Response getInheritance();

    @GET
    @Path("inheritance2")
    public Response getInheritance2();

    @GET
    @Path("inheritanceinterface1")
    public Response getInheritance3();

    @GET
    @Path("inheritanceinterface5a")
    public Response getInheritance5();
}
