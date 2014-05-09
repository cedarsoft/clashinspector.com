package com.clashinspector.service;

import com.clashinspector.model.ClashCollectResultWrapper;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.server.JSONP;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 02.05.14
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
@Path( "dependencies" )
public class DependencyService {

  private static ClashCollectResultWrapper clashCollectResultWrapper;

  @GET
  @JSONP(queryParam="callback")
  @Produces("application/x-javascript")
  public String getAllDependencies(@QueryParam("callback") String callback)
  {
    ObjectMapper mapper = new ObjectMapper(  );
    //mapper.setVisibility( JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY );
    //mapper.configure( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    String value = "";
    try
    {
      value = mapper.writeValueAsString( clashCollectResultWrapper.getRoot().getProject());

    }
    catch (Exception e)
    {
      System.out.println(e);
    }

    return value;
  }

  @Path("project/{projectid}")
  @GET
  @JSONP(queryParam="callback")
  @Produces("application/x-javascript")
  public String getAllDependenciesProject(@QueryParam("callback") String callback)
  {
    ObjectMapper mapper = new ObjectMapper(  );
    //mapper.setVisibility( JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY );
    //mapper.configure( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    String value = "";
    try
    {
      value = mapper.writeValueAsString( clashCollectResultWrapper.getRoot() );

    }
    catch (Exception e)
    {
      System.out.println(e);
    }

    return value;
  }


  public static void setClashCollectResultWrapper( ClashCollectResultWrapper clashCollectResultWrapper ) {
    DependencyService.clashCollectResultWrapper = clashCollectResultWrapper;
  }

}

