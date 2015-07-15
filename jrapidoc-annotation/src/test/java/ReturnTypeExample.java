import org.projectodd.jrapidoc.annotation.rest.DocReturn;
import org.projectodd.jrapidoc.annotation.rest.DocReturns;

public class ReturnTypeExample {

    @DocReturn(http = 200, type = Object.class, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"}, description = "Some description")
    public void foo(){}

    @DocReturns({
            @DocReturn(http = 200, type = Object.class, structure = DocReturn.Structure.ARRAY, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"}),
            @DocReturn(http = 200, type = Object.class, structure = DocReturn.Structure.MAP, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"})
    })
    public void foo2(){}
}
