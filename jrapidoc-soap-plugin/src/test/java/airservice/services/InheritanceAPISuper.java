package airservice.services;

import javax.jws.WebMethod;

public interface InheritanceAPISuper {

    @WebMethod(operationName = "renamed")
    public void foo();
}
