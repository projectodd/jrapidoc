package airservice.resources;

import airservice.entity.ExceptionEntity;
import airservice.entity.Wrapper;
import airservice.entity.bean.FromStringBean;
import airservice.entity.bean.ParamConverBean;
import airservice.entity.bean.StringConstBean;
import airservice.entity.bean.ValueOfBean;
import airservice.entity.destination.*;
import airservice.exception.MyException;
import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.rest.DocIsRequired;
import org.projectodd.jrapidoc.annotation.rest.DocPathExample;
import org.projectodd.jrapidoc.annotation.rest.DocReturn;
import org.projectodd.jrapidoc.annotation.rest.DocReturns;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST Web Service
 *
 * @author Tomas "sarzwest" Jiricek
 */
@Path("/test/{pathparam}/")
@Produces("text/plain")
@DocDescription("Description of resource")
@DocPathExample("/test/5")
public class TestResource extends Class1 implements IFace2, ParentInterface,
        Parent2Interface {

    @DefaultValue("defvaluematrix")
    @DocIsRequired
    @DocDescription("qwerty")
    @MatrixParam("matrixparam")
    String matrix;
    @DocIsRequired(false)
    @QueryParam("queryparam")
    @DocDescription("qqq")
    int query;
    @PathParam("pathparam")
    @DocDescription("www")
    @DocIsRequired(true)
    int path;
    @CookieParam("cookieparam")
    @DocDescription("eee")
    @DocIsRequired(true)
    String cookie;
    @DocDescription("rrr")
    @DocIsRequired(true)
    @HeaderParam("headerparam")
    String header;
    @DocDescription("ttt")
    @FormParam("formparam")
    String form;

    @DefaultValue("string const def value")
    @QueryParam("stringconstbean")
    StringConstBean stringConstBean;
    @QueryParam("paramconverbean")
    ParamConverBean paramConverBean;
    @QueryParam("valueofbean")
    ValueOfBean valueOfBean;
    @QueryParam("fromstringbean")
    FromStringBean fromStringBean;

    /**
     * listbean=a&listbean=okoni
     */
    @DefaultValue("string list def value")
    @DocDescription("asd")
    @QueryParam("listbean")
    List<FromStringBean> listbean;
    /**
     * setbean=a&setbean=okoni
     */
    @QueryParam("setbean")
    Set<FromStringBean> setbean;
    /**
     * sortedsetbean=a&sortedsetbean=okoni
     */
    @QueryParam("sortedsetbean")
    SortedSet<FromStringBean> sortedsetbean;

    @QueryParam("n")
    int i;
    @QueryParam("n1")
    Integer ii;
    @Context
    private UriInfo context;

    @DocDescription("hdsa")
    @HeaderParam("headerFromSetter")
    public void setHeaderFromSetter(BigDecimal headerFromSetter) {
        this.headerFromSetter = headerFromSetter;
    }

    private BigDecimal headerFromSetter;

    /**
     * Creates a new instance of TestResource
     */
    public TestResource(@DocDescription("aaa")@DocIsRequired @QueryParam("queryconst") String query) {
        System.out.println(query);
    }

    @GET
    @POST
    @Path(value = "/encoded")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response encoded(@QueryParam("text") @Encoded String text, @QueryParam("fromstringbeanparam") FromStringBean f) {

        return Response.status(Response.Status.OK).entity("je to ok").build();
    }

    @GET
    @Path("pathExample/[a-z]{3}")
    @DocPathExample("/pathExample/aaa")
    public void pathExample() {

    }

    @GET
    @Path(value = "/annotatedfield")
    @Produces(MediaType.TEXT_PLAIN)
    public Response annotatedField() {
        return Response.status(Response.Status.OK).entity("je to ok").build();
    }

    /**
     *
     */
    @POST
    @Path("entityparam/{id}")
    public Response newDestination(DestOutChild dest, @PathParam("id") String id) {
        return Response.ok().build();
    }


    @GET
    @Path(value = "/retgenericentity")
    @Produces(MediaType.APPLICATION_XML)
    public GenericEntity<List<DestinationOutput>> collection() {
        List<DestinationOutput> aList = new ArrayList<DestinationOutput>();
        DestinationOutput dest = new DestinationOutput();
        dest.setName("Praha");
        aList.add(dest);
        dest = new DestinationOutput();
        dest.setName("Pv City");
        aList.add(dest);
        GenericEntity<List<DestinationOutput>> genericEntity = new GenericEntity<List<DestinationOutput>>(
                aList) {
        };
        return genericEntity;
    }

    @GET
    @Path(value = "/retgenericentityinnerdoublelist")
    @Produces(MediaType.APPLICATION_JSON)
    public GenericEntity<List<List<DestinationOutput>>> doubleInnerCollection() {
        List<DestinationOutput> aList = new ArrayList<DestinationOutput>();
        DestinationOutput dest = new DestinationOutput();
        dest.setName("Praha");
        aList.add(dest);
        dest = new DestinationOutput();
        dest.setName("Pv City");
        aList.add(dest);
        List<List<DestinationOutput>> outerList = new ArrayList<List<DestinationOutput>>();
        outerList.add(aList);
        GenericEntity<List<List<DestinationOutput>>> genericEntity = new GenericEntity<List<List<DestinationOutput>>>(
                outerList) {
        };
        return genericEntity;
    }

    @POST
    @Path(value = "/rawtype")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Wrapper<DestinationOutput> rawType(Wrapper<DestOutChild> incomeWrap) {
        Wrapper<DestinationOutput> wrapper = new Wrapper<DestinationOutput>(
                new DestOutChild());
        return wrapper;
    }

    @POST
    @Path(value = "/reqlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reqList(List<DestinationOutput> destination) {
        for (DestinationOutput d : destination) {
            System.out.println(d.getName());
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("pathparam/{path}")
    public void getPathParam(@PathParam("path") String path) {
        System.out.println(path);
    }


    @GET
    @Path("queryparam")
    public void getQueryParam(@QueryParam("q") String q) {
        System.out.println(q);
        System.out.println();
    }

    @POST
    @Path("matrixparam")
    public void getMatrixParam(@MatrixParam("m") List<String> m) {
        System.out.println(m);
    }


    @POST
    @Path("cookieparam")
    public void getCookieParam(@CookieParam("c") String c) {
        System.out.println(c);
    }


    @POST
    @Path("headerparam")
    public void getHeaderParam(@HeaderParam("h") String h) {
        System.out.println(h);
    }


    @POST
    @Path("formparam")
    public void getFormParam(@FormParam("f") String f) {
        System.out.println(f);
    }


    @POST
    @Path(value = "/rawtype2")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public <T> List<T> rawType2(List<T> src) {
        List<T> list = new ArrayList<T>();
        for (T t : src) {
            list.add(t);
        }
        return list;
    }

    @POST
    @Path(value = "/object")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public <T> T object(T src) {
        return src;
    }

    @POST
    @Path(value = "/array")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public int[] object(Integer[] src) {
        return null;
    }

    /**
     * Gets the status info
     */
    @GET
    @Path("status")
    public Response getStatus() {
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Gets the status info
     */
    @GET
    @Path("retcontenttype")
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,
            "application/xml"})
    public Response getContentType() {
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Return type is classic object
     */
    @GET
    @Path("retclassictype")
    @Produces({MediaType.APPLICATION_ATOM_XML, "application/json"})
    public DestinationOutput getClassicType() {
        DestinationOutput dstO = new DestinationOutput();
        dstO.setId(1234);
        dstO.setName("some name");
        return dstO;
    }

    /**
     * Return type is collection of classic objects
     */
    @GET
    @Path("retlistclassictype")
    @Produces({MediaType.APPLICATION_JSON})
    public List<DestinationOutput> getCollectionClassicTypes() {
        DestinationOutput dstO = new DestinationOutput();
        dstO.setId(1234);
        dstO.setName("some name");
        List<DestinationOutput> list = new ArrayList<DestinationOutput>();
        list.add(dstO);
        list.add(dstO);
        return list;
    }

    @POST
    @Path(value = "/exception")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Wrapper<DestinationOutput> exception(Wrapper<DestOutChild> incomeWrap) {
        if (incomeWrap.varInWrapper.equals("a")) {
            throw new WebApplicationException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.TEXT_PLAIN).entity("exception text")
                    .build());
        }
        if (incomeWrap.varInWrapper.equals("b")) {
            throw new WebApplicationException(Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ExceptionEntity()).build());
        }
        Wrapper<DestinationOutput> wrapper = new Wrapper<DestinationOutput>(
                new DestOutChild());
        return wrapper;
    }

    @Path("subresourcelocator")

    public SubResource getSubResource() {
        return new SubResource("tadaaa");
    }

    @Override
    public Response getInheritance() {
        return Response.ok("toto je inheritance", MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public Response getInheritance2() {
        return Response.ok("toto je inheritance", MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    public Response getInheritance3() {
        return Response.ok("toto je inheritance", MediaType.APPLICATION_JSON)
                .build();
    }


    @Override
    public Response getInheritance5() {
        return Response.ok("toto je inheritance", MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("exceptionmapper")
    @Produces({MediaType.APPLICATION_XML})
    public Response exceptionMapper() throws MyException {
        throw new MyException();
    }

    @Override
    public Response foo() {
        return Response.ok("toto je inheritance", MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("compressed")
    public Response getCompressed() {
        return Response.ok("Called named interceptor for compress response",
                MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("async")
    public void async(@Suspended final AsyncResponse ar) {
        ar.setTimeoutHandler(new TimeoutHandler() {
            @Override
            public void handleTimeout(AsyncResponse ar) {
                ar.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation timed out -- please try again")
                        .build());
            }
        });
        ar.setTimeout(10, TimeUnit.SECONDS);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestResource.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
                System.out.println("Done");
                ar.resume("Cau kamo");
            }
        });
    }

    @GET
    @Path("asdfg")
    @Produces("application/json")
    public Response getCollection(@QueryParam("what") boolean b) {
        if (b) {
            System.out.println("array");
            return Response.ok().entity(new DestinationOutput[]{new DestinationOutput().setId(45), new DestinationOutput().setId(78)}).build();
        } else {
            System.out.println("collection");
            return Response.ok().entity(new ArrayList<DestinationOutput>(Arrays.asList(new DestinationOutput[]{new DestinationOutput().setId(45), new DestinationOutput().setId(78)}))).build();
        }
    }

    @GET
    @Path("serializer")
    @Produces({"application/json", "application/xml", "application/destination+xml"})
    public Response serializerTest() {
        DestinationOutput out = new DestinationOutput();
        out.setId(789);
        out.setUri("qwert");
        out.setName("asdf");
        return Response.ok().entity(out).build();
    }

    @POST
    @Path("foo")
    @Consumes("application/json")
    public void fooo(Destination d) {
        return;
    }

    @POST
    @DocReturn(http = 200, type = Object.class, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"})
    public void foo9() {
    }

    @POST
    @DocReturns({
            @DocReturn(http = 200, type = Object.class, structure = DocReturn.Structure.ARRAY, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"}),
            @DocReturn(http = 200, type = Object.class, structure = DocReturn.Structure.MAP, headers = {"X-Header", "X-Option"}, cookies = {"sessionid"})
    })
    public void foo2() {
    }

    @POST
    public void foo4() {
    }

    @POST
    public Response foo3() {
        return null;
    }

    @POST
    public GenericEntity foo5() {
        return null;
    }

    @POST
    public GenericEntity<List<String>> foo6() {
        return null;
    }

    @POST
    public GenericEntity<String> foo7() {
        return null;
    }

    @POST
    public GenericEntity<Boolean[]> foo8() {
        return null;
    }

    @POST
    public boolean[] foo10() {
        return null;
    }

    @POST
    @Path("decription")
    @DocDescription("Description of method")
    @DocReturn(http = 200, description = "Description of return option", type = DestinationExample.class, typeDescription = "Description of DestinationExample type")
    public DestinationExample description(@DocDescription("Description of query param") @QueryParam("qp") String s) {
        return null;
    }

    @GET
    @Path("aa")
    public void foo(DestinationEntity d) {

    }

    @GET
    @Path("map")
    public Map<String, DestinationEntity> map(Map<String, Destination> mde) {
        return null;
    }

    @GET
    @Path("emptyentitybody")
    public void foo11() {

    }

    @POST
    @Path("emptyentitybody")
    public void foo12() {

    }

    @GET
    @Path("getdestination")
    @Produces({"application/json", "application/xml"})
    public DestinationExample getDestination() {
        DestinationExample d = new DestinationExample();
        d.setId(54);
        d.setName("Prague");
        return d;
    }
}
