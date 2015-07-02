package airservice.services;

import airservice.entity.destination.Destination;
import airservice.exception.AirserviceException;
import airservice.exception.AirserviceFault;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.Holder;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * Created by papa on 1.2.15.
 */
@WebService
public class TestService {

    @WebMethod
    public Integer test(
            @WebParam(header = true) String a, String b) {
        System.out.println("test");
        return 5;
    }

    public void test2(@WebParam(header = true) Holder<Destination> ss) {
        System.out.println(ss);
    }

    @WebMethod
    public int divide(int first, int second) throws AirserviceFault {
        int result;
        try {
            result = first / second;
        } catch (Exception e) {
            throw new AirserviceFault("message for fault", e, new AirserviceException().setMessage("message in airexception"));
        }
        return result;
    }

    @WebMethod(operationName = "explicitName")
    public void testException(int exType) throws SOAPException {
        if (exType == 0) {
            throw new NumberFormatException("Number in wrong format");
        }
        if (exType == 1) {
            throw new WebServiceException("Exception of type WebServiceException");
        }
        if (exType == 2) {
            throw new ProtocolException("Exception of type ProtocolException");
        }
        if (exType == 3) {
            SOAPFault fault = SOAPFactory.newInstance().createFault();
            fault.setFaultCode("A code for identifying the fault");
            fault.setFaultString("A human readable explanation of the fault");
            fault.setFaultActor("Information about who caused the fault to happen");
            Detail detail = fault.addDetail();
            detail.setValue("Holds application specific error information related to the Body element");
            detail.addDetailEntry(new QName("explanation")).setValue("SOAP Fault Codes\n" +
                    "\n" +
                    "The faultcode values defined below must be used in the faultcode element when describing faults:\n" +
                    "Error \t\t\tDescription\n" +
                    "VersionMismatch \tFound an invalid namespace for the SOAP Envelope element\n" +
                    "MustUnderstand \tAn immediate child element of the Header element, with the mustUnderstand attribute set to \"1\", was not understood\n" +
                    "Client \t\t\tThe message was incorrectly formed or contained incorrect information\n" +
                    "Server \t\t\tThere was a problem with the server so the message could not proceed");
            throw new SOAPFaultException(fault);
        }
    }
}
