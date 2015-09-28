package airservice.services;

import airservice.entity.destination.D1;
import airservice.entity.destination.Destination;
import airservice.exception.AirserviceException;
import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.soap.DocReturn;
import org.projectodd.jrapidoc.annotation.soap.DocReturns;
import org.projectodd.jrapidoc.annotation.soap.wsprovider.DocParam;
import org.projectodd.jrapidoc.annotation.soap.wsprovider.DocParams;
import org.projectodd.jrapidoc.annotation.soap.wsprovider.DocSOAPBinding;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

@DocDescription("WebServiceProvider description")
@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class SimpleWebServiceProvider implements Provider<SOAPMessage> {

    @Override
    @DocDescription("Operation description")
    @DocSOAPBinding(parameterStyle = DocSOAPBinding.ParameterStyle.WRAPPED, style = DocSOAPBinding.Style.DOCUMENT, use = DocSOAPBinding.Use.ENCODED)
    @DocParams({
            @DocParam(isHeader = true, isRequired = true, type = Destination.class, description = "Destination object description"),
            @DocParam(type = D1.class, description = "D1 object description")
    })
    @DocReturns({
            @DocReturn(type = Destination.class, description = "Some description", typeDescription = "Destination object description"),
            @DocReturn(http = 500, type = AirserviceException.class),
            @DocReturn(http = 500, type = D1.class)
    })
    public SOAPMessage invoke(SOAPMessage request) {
        System.out.println(request);
        return request;
    }
}
