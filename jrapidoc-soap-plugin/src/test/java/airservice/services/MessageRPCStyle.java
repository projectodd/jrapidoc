package airservice.services;

import airservice.entity.destination.DestinationInput;
import airservice.entity.destination.DestinationOutput;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
public class MessageRPCStyle {

    @WebMethod
    @SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
    public DestinationOutput rpc(DestinationInput rpc, String s){
        return new DestinationOutput();
    }
}
