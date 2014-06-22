package com.clashinspector.rest;

import com.clashinspector.jacksonSerializer.DependencyNodeWrapperSerializer;
import com.clashinspector.jacksonSerializer.InnerVersionClashSerializer;
import com.clashinspector.jacksonSerializer.OuterVersionClashSerializer;
import com.clashinspector.jacksonSerializer.ProjectSerializerForDependencyNodeWrapper;
import com.clashinspector.jacksonSerializer.VersionSerializer;
import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.model.DependencyNodeWrapper;
import com.clashinspector.model.InnerVersionClash;
import com.clashinspector.model.OuterVersionClash;
import com.clashinspector.model.Project;
import com.clashinspector.mojos.ClashSeverity;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.version.Version;
import org.glassfish.jersey.server.JSONP;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 02.05.14
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
@Path( "dependencies" )
public class DependencyRestService {

  //Was pasiiert wenn zwei browserfenster geöffnet werden?    resultWrapper in sessionmap speichern


  private static Artifact mainArtifact;
  private static RepositorySystem repositorySystem;
  private static RepositorySystemSession repositorySystemSession;
  private static UserParameterWrapper startParameter;

  public static void init( Artifact artifact, RepositorySystem repositorySystem1, RepositorySystemSession repositorySystemSession1, UserParameterWrapper userParameterWrapper ) {
    startParameter = userParameterWrapper;
    mainArtifact = artifact;
    repositorySystem = repositorySystem1;
    repositorySystemSession = repositorySystemSession1;
  }


  @GET
  @JSONP( queryParam = "callback" )
  @Produces( "application/x-javascript" )
  public String getAllDependenciesWithClashes( @QueryParam( "callback" ) String callback, @QueryParam( "includedScope" ) List<String> includedScopes, @QueryParam( "excludedScope" ) List<String> excludedScopes, @QueryParam( "includeOptional" ) boolean includeOptional, @QueryParam( "clashSeverity" ) ClashSeverity clashSeverity ) {


    UserParameterWrapper userParameterWrapper;

    //Bei ninitialanfrage sind userparameter leer, deshalb checke ob es sich um initalfrage handelt
    if ( includedScopes.size() == 0 ) {
      userParameterWrapper = startParameter;

    } else {
      userParameterWrapper = new UserParameterWrapper( includedScopes, excludedScopes, includeOptional, clashSeverity );
      //System.out.println("UserParameter aus url hergestellt: " + includedScopes.toString() + " " +  excludedScopes.toString() + " " + includeOptional)    ;
    }

    com.clashinspector.DependencyService dependencyService = new com.clashinspector.DependencyService();
    ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( mainArtifact, repositorySystemSession, repositorySystem, userParameterWrapper.getIncludedScopes(), userParameterWrapper.getExcludedScopes(), userParameterWrapper.getIncludeOptional() ) );


    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule( "MyModule", new org.codehaus.jackson.Version( 1, 0, 0, null ) );

    module.addSerializer( Version.class, new VersionSerializer() );
    module.addSerializer( Project.class, new ProjectSerializerForDependencyNodeWrapper() );
    module.addSerializer( DependencyNodeWrapper.class, new DependencyNodeWrapperSerializer() );
    mapper.registerModule( module );


    String value = "";
    try {

      ResponseObject responseObject = new ResponseObject();

      responseObject.setResult( clashCollectResultWrapper.getRoot() );
      responseObject.setUserParameterWrapper( userParameterWrapper );


      value = mapper.writeValueAsString( responseObject );

    } catch ( Exception e ) {
      System.out.println( e );
    }

    return value;
  }


  @GET
  @Path( "outerVersionClashes" )
  @JSONP( queryParam = "callback" )
  @Produces( "application/x-javascript" )
  public String getClashList( @QueryParam( "callback" ) String callback, @QueryParam( "includedScope" ) List<String> includedScopes, @QueryParam( "excludedScope" ) List<String> excludedScopes, @QueryParam( "includeOptional" ) boolean includeOptional, @QueryParam( "clashSeverity" ) ClashSeverity clashSeverity ) {


    UserParameterWrapper userParameterWrapper;


    //Bei initialanfrage sind userparameter leer, deshalb checken ob es sich um initalfrage handelt
    if ( includedScopes.size() == 0 ) {
      userParameterWrapper = startParameter;
    } else {
      userParameterWrapper = new UserParameterWrapper( includedScopes, excludedScopes, includeOptional, clashSeverity );
    }


    com.clashinspector.DependencyService dependencyService = new com.clashinspector.DependencyService();
    ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( mainArtifact, repositorySystemSession, repositorySystem, userParameterWrapper.getIncludedScopes(), userParameterWrapper.getExcludedScopes(), userParameterWrapper.getIncludeOptional() ) );


    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule( "MyModule", new org.codehaus.jackson.Version( 1, 0, 0, null ) );


    String value = "";
    try {


      ResponseObject responseObject = new ResponseObject();

      responseObject.setResult( clashCollectResultWrapper.getOuterClashesForSeverityLevel( clashSeverity ) );
      responseObject.setUserParameterWrapper( userParameterWrapper );


      module.addSerializer( Version.class, new VersionSerializer() );
      module.addSerializer( OuterVersionClash.class, new OuterVersionClashSerializer( clashSeverity ) );
      module.addSerializer( InnerVersionClash.class, new InnerVersionClashSerializer() );
      module.addSerializer( Project.class, new ProjectSerializerForDependencyNodeWrapper() );

      mapper.registerModule( module );

      value = mapper.writeValueAsString( responseObject );

    } catch ( Exception e ) {
      System.out.println( e );
    }

    return value;
  }

   /*
  @Path("clashes")
  @GET
  @JSONP(queryParam="callback")
  @Produces("application/x-javascript")
  public String getClashList(@QueryParam("callback") String callback,@QueryParam( "clashSeverity" )ClashSeverity clashSeverity)
  {
    System.out.println("Clashseverity: "+clashSeverity);
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



             */


}

