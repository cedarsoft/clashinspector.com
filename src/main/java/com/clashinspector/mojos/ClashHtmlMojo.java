package com.clashinspector.mojos;




import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.service.DependencyService;
import com.clashinspector.visualize.ConsoleVisualizer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;


import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;

/**
 * Displays the dependency tree for this project. The tree also shows version clashes.
 *
 * @since 0.3
 */
@Mojo(name = "html", requiresProject = true, defaultPhase = LifecyclePhase.NONE)
public class ClashHtmlMojo extends AbstractClashMojo {


  //big tree .. small tree und level mitgeben
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    super.execute();
    super.printStartParameter( "html" );

    Artifact artifact;
    try {

      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );


      com.clashinspector.DependencyService dependencyService = new com.clashinspector.DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );

      //consoleVisualizer.visualize( clashCollectResultWrapper, this.getSeverity(), this );

       ObjectMapper mapper = new ObjectMapper(  );
      //mapper.setVisibility( JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY );
      //mapper.configure( SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

      try
      {
         String value = mapper.writeValueAsString( clashCollectResultWrapper.getRoot() );
          System.out.println(value);
      }
      catch (Exception e)
      {
             System.out.println(e);
      }


      BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

      ResourceConfig config = new DefaultResourceConfig(DependencyService.class);
      HttpServer server = HttpServerFactory.create( "http://localhost:8080/" ,config);
      server.start();

      if (Desktop.isDesktopSupported())
      {
        Desktop desktop=Desktop.getDesktop();


        this.transferResourceToTmp( "clashInspectorStyle", "css" );
        this.transferResourceToTmp( "jquery-1.11.0", "js" );
        this.transferResourceToTmp( "main", "js" );


        desktop.browse(this.transferResourceToTmp( "clashInspector", "html" ));
      }
      else
      {
        super.getLog().warn( "Couldn't open File with default-browser. Please open file manually under: " + System.getProperty("java.io.tmpdir")+"clashInspector.html" );
      }


      super.getLog().info( "To stop local ClashInspector-Server press enter." );


      String inLine = in.readLine();
      server.stop( 0 );


    }
    catch ( Exception e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }



  }







       //Puts file into tmp-folder
     private URI transferResourceToTmp(String fileName,String fileEnding)
     {
       fileEnding = fileEnding.replace( ".","" );
            fileEnding = "."+fileEnding;

       InputStream resource = getClass().getResourceAsStream("/"+fileName+fileEnding );

       try
       {

         String tDir = System.getProperty("java.io.tmpdir");

         File file = new File(tDir+fileName+fileEnding);

         if(file.exists())
         {
           file.delete();
         }


         file.deleteOnExit();
         Files.copy( resource, file.toPath() );

         resource.close();

         return file.toURI();
       }
       catch (IOException ioe)
       {
         super.getLog().error( ioe );
         return null;
       }


     }







}


