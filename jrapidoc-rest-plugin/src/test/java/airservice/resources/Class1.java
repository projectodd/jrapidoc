/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package airservice.resources;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tomas "sarzwest" Jiricek
 */
public abstract class Class1 extends Class1_1 implements IFace1_2{


    @QueryParam("queryparamparent")
    String queryParent;

    public abstract Response foo();
}
