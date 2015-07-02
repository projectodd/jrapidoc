package airservice.services;

import airservice.entity.destination.Destination;
import airservice.exception.AirserviceFault;
import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.rest.DocIsRequired;
import org.projectodd.jrapidoc.annotation.soap.DocReturn;
import org.projectodd.jrapidoc.annotation.soap.DocReturns;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

/**
 * Created by papa on 8.4.15.
 */
@WebService
@DocDescription("Web service description")
@SOAPBinding(use = SOAPBinding.Use.ENCODED)
public class JRAPIDocService {

    public Destination foo(Destination d) {
        return null;
    }

    public Destination foo1(@WebParam(header = true, name = "customName") Destination d) {
        return null;
    }

    @DocDescription("Web method description")
    @DocReturn(http = 200, description = "Return type description")
    public Destination foo2(@DocDescription("Parameter description") @WebParam(header = true, mode = WebParam.Mode.INOUT) Holder<Destination> d) {
        return null;
    }

    @WebMethod(operationName = "uuuuaaaa")
    public int foo5() throws AirserviceFault {
        return 0;
    }

    @Oneway
    public String foo6(@WebParam(mode = WebParam.Mode.OUT) Holder<Integer> i) throws AirserviceFault {
        return null;
    }

    public void foo7() throws AirserviceFault {
    }

    @WebResult(header = true)
    public Destination foo3() {
        return null;
    }

    @WebResult(header = true, name = "customName")
    @DocDescription("This is operation description")
    @DocReturn(description = "This is return option description", typeDescription = "Description for return type")
    public Destination foo4() {
        return null;
    }

    @WebResult(name = "customName")
    @DocDescription("This is operation description")
    @DocReturns(
            {@DocReturn(description = "This is return option description", typeDescription = "Description for return type"),
                    @DocReturn(http = 500, description = "This is second return option description", type = AirserviceFault.class, typeDescription = "Description of exception type")}
    )
    public Destination foo4a() {
        return null;
    }

    @WebResult(name = "customName")
    @DocDescription("This is operation description")
    @DocReturns(
            {@DocReturn(description = "This is return option description", typeDescription = "Description for return type"),
                    @DocReturn(http = 500, description = "This is second return option description", type = AirserviceFault.class, typeDescription = "Description of exception type")}
    )
    public Destination foo4b(@DocIsRequired@DocDescription("Description of input output object")@WebParam(mode = WebParam.Mode.INOUT, header = true)Holder<String> inout) {
        return null;
    }
}
