package airservice.services;

import airservice.entity.destination.DestinationInput;
import airservice.entity.destination.DestinationOutput;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;

/**
 * Created by papa on 1.2.15.
 */
@WebService(
        name = "nameNAME",
        portName = "portNameNAME",
        serviceName = "serviceNameNAME",
        targetNamespace = "namespace"
//        targetNamespace = "http://org.jboss.ws/jaxws/cxf/jms"
)
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.ENCODED, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@HandlerChain(file = "/handlers.xml")
public class TestService2 {

    @Resource
    WebServiceContext wsCtx;

    @WebMethod
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
    public Integer test(String a, String b){
        System.out.println("test");
        return 5;
    }

    @WebMethod
    public String longTimeProcess(String s){
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "to to trvalo";
    }

    @WebMethod
    public String handlerTest(String in){
        return in.toUpperCase();
    }

    @WebMethod
    public DestinationOutput sendObjects(DestinationInput input){
        return new DestinationOutput();
    }
}
