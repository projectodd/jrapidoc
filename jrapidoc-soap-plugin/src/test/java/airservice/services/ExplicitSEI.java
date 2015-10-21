package airservice.services;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ExplicitSEI {

    @WebMethod
    public void foo1();

    public void foo2();
}
