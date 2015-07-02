package airservice.services;

import javax.jws.WebService;

/**
 * Created by papa on 11.3.15.
 */
@WebService(targetNamespace = "nsapi")
public interface InheritanceAPI extends InheritanceAPISuper {

    public void foo2();
}
