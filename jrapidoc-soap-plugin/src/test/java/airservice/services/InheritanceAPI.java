package airservice.services;

import javax.jws.WebService;

@WebService(targetNamespace = "nsapi")
public interface InheritanceAPI extends InheritanceAPISuper {

    public void foo2();
}
