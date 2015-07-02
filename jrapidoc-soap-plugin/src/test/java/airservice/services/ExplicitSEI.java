package airservice.services;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by papa on 11.3.15.
 */
@WebService
public interface ExplicitSEI {

    @WebMethod
    public void foo1();

    public void foo2();
}
