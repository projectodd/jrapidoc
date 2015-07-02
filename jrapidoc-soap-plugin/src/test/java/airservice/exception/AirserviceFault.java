package airservice.exception;

import javax.xml.ws.WebFault;

/**
 * Created by papa on 3.2.15.
 */
@WebFault(
    name = "AirserviceException"
)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public class AirserviceFault extends Exception{

    private AirserviceException faultInfo;

    public AirserviceFault(String message, AirserviceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public AirserviceFault(String message, Throwable cause, AirserviceException faultInfo) {
        super(message + ": " + cause.getMessage(), cause);
        this.faultInfo = faultInfo;
    }

    public AirserviceException getFaultInfo() {
        return faultInfo;
    }
}
