package com.clashinspector.rest;

import com.clashinspector.jacksonSerializer.VersionSerializer;
import com.clashinspector.model.ClashCollectResultWrapper;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.glassfish.jersey.server.JSONP;
import org.eclipse.aether.version.Version;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 02.05.14
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
@Path( "dependencies" )
public class DependencyRestService {

  private static ClashCollectResultWrapper clashCollectResultWrapper;

  @GET
  @JSONP(queryParam="callback")
  @Produces("application/x-javascript")
  public String getAllDependencies(@QueryParam("callback") String callback)
  {
    ObjectMapper mapper = new ObjectMapper(  );
    SimpleModule module = new SimpleModule( "MyModule", new org.codehaus.jackson.Version(1, 0, 0, null));

    module.addSerializer(Version.class, new VersionSerializer());
    mapper.registerModule( module );
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

  @Path("clashes")
  @GET
  @JSONP(queryParam="callback")
  @Produces("application/x-javascript")
  public String getClashList(@QueryParam("callback") String callback)
  {
    ObjectMapper mapper = new ObjectMapper(  );
    SimpleModule module = new SimpleModule( "MyModule", new org.codehaus.jackson.Version(1, 0, 0, null));

    module.addSerializer(Version.class, new VersionSerializer());
    mapper.registerModule( module );
    //mapper.setVisibility( JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY );
    //mapper.configure( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    String value = "";
    try
    {
      value = mapper.writeValueAsString( clashCollectResultWrapper.getOuterVersionClashList() );

    }
    catch (Exception e)
    {
      System.out.println(e);
    }

    return value;
  }














  public static void setClashCollectResultWrapper( ClashCollectResultWrapper clashCollectResultWrapper ) {
    DependencyRestService.clashCollectResultWrapper = clashCollectResultWrapper;
  }
}

