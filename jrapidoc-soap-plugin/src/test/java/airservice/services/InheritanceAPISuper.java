package airservice.services;

import javax.jws.WebMethod;

/**
 * Created by papa on 11.3.15.
 */
public interface InheritanceAPISuper {

    @WebMethod(operationName = "renamed")
    public void foo();
}
