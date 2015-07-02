package airservice.services;

import airservice.entity.destination.DestinationInput;
import airservice.entity.destination.DestinationOutput;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by papa on 14.3.15.
 */
@WebService
public class MessageDocumentStyle {

    @WebMethod
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
    public DestinationOutput wrapped(DestinationInput wrapped, String s){
        return new DestinationOutput();
    }

    @WebMethod
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public DestinationOutput bare(DestinationInput bare){
        return new DestinationOutput();
    }
}
