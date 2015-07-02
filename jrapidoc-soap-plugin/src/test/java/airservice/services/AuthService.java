package airservice.services;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

/**
 * Created by papa on 5.2.15.
 */
@WebService
public class AuthService {

    @Resource
    WebServiceContext wsCtx;

    @WebMethod
    public String authentication(){
        try{
            MessageContext msgctx = wsCtx.getMessageContext();
            Map headers = (Map)msgctx.get(MessageContext.HTTP_REQUEST_HEADERS);
            if(((List<String>)headers.get("Username")).get(0).equals("tomas") &&
                    ((List<String>)headers.get("Password")).get(0).equals("1234")){
                return "Authenticated";
            }
        }catch (Exception e){
        }
        return "Not authenticated!!!";
    }
}
