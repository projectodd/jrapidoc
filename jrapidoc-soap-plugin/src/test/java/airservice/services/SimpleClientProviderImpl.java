package airservice.services;

import org.w3c.dom.Node;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.*;
import javax.xml.ws.handler.MessageContext;
import java.io.ByteArrayInputStream;
import java.util.*;


/**
 * A simple Provider-based Web service implementation.
 *
 * @author Copyright (c) 2010, Oracle and/or its affiliates.
 * All Rights Reserved.
 */
// The @ServiceMode annotation specifies whether the Provider instance
// receives entire messages or message payloads.
@ServiceMode(value = Service.Mode.PAYLOAD)

// Standard JWS annotation that configures the Provider-based Web service.
@WebServiceProvider(portName = "SimpleClientPort",
        serviceName = "SimpleClientService",
        targetNamespace = "http://jaxws.webservices.examples/"
//        ,wsdlLocation = "SimpleClientService.wsdl"
)
public class SimpleClientProviderImpl implements Provider<Source> {

    @Resource
    WebServiceContext wsCtx;

    //Invokes an operation according to the contents of the request message.
    public Source invoke(Source source) {
        MessageContext msgCtx = wsCtx.getMessageContext();
        for (String key : msgCtx.keySet()) {
            System.out.println(key + ": " + msgCtx.get(key));
        }
        msgCtx.put(MessageContext.QUERY_STRING, "necoKamo");
        msgCtx.put("custom", "custom vole");
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("bla", new ArrayList<String>(Arrays.asList(new String[]{"ahoj", "vole"})));
        msgCtx.put(MessageContext.HTTP_RESPONSE_HEADERS, headers);
        try {
            DOMResult dom = new DOMResult();
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(source, dom);
            Node node = dom.getNode();
            // Get the operation name node.
            Node root = node.getFirstChild();
            String input = root.getFirstChild().getNodeValue();
            // Get the operation name.
            String op = root.getLocalName();
            if ("invokeNoTransaction".equals(op)) {
                return sendSource(input);
            } else {
                return sendSource2(input);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Error in provider endpoint", e);
        }
    }

    private Source sendSource(String input) {
        String body =
                "<ns:invokeNoTransactionResponse  xmlns:ns=\"http://jaxws.webservices.examples/\"><return>"
                + "constructed:" + input
                + "</return></ns:invokeNoTransactionResponse>";
        Source source = new StreamSource(new ByteArrayInputStream(body.getBytes()));
        return source;
    }

    private Source sendSource2(String input) {
        String body =
                "<ns:invokeTransactionResponse xmlns:ns=\"http://jaxws.webservices.examples/\"><return>"
                + "constructed:" + input
                + "</return></ns:invokeTransactionResponse>";
        Source source = new StreamSource(new ByteArrayInputStream(body.getBytes()));
        return source;
    }

}

