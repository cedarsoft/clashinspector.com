package com.clashinspector.mojos;




import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.rest.DependencyRestService;
import com.clashinspector.rest.UserParameterWrapper;

import com.clashinspector.visualize.ConsoleVisualizer;

import com.sun.net.httpserver.HttpServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


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
          int port = 8090;

  //big tree .. small tree und level mitgeben
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    super.execute();
    super.printStartParameter( "html" );

    Artifact artifact;
    try {

      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );



      UserParameterWrapper userParameterWrapper = new UserParameterWrapper(this.getIncludedScopesList(),this.getExcludedScopesList(),this.isIncludeOptional()  );
        //ViewScopeManager initialisieren
      DependencyRestService.init( artifact,this.getRepoSystem(),this.getRepoSession(), userParameterWrapper );

      //TODO port eventuell variabel machen

      BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

      ResourceConfig config = new ResourceConfig(DependencyRestService.class);
      HttpServer server = JdkHttpServerFactory.createHttpServer(new URI( "http://localhost:"+port+"/"), config );


      if (Desktop.isDesktopSupported())
      {
        Desktop desktop=Desktop.getDesktop();


        this.transferResourceToTmp( "clashInspectorStyle", "css" );
        this.transferResourceToTmp( "jquery-1.11.0", "js" );
        this.transferResourceToTmp( "main", "js" );
        this.transferResourceToTmp( "openDepNode_xs", "png" );
        this.transferResourceToTmp( "openDepNode_s", "png" );
        this.transferResourceToTmp( "openDepNode_m", "png" );
        this.transferResourceToTmp( "openDepNode_l", "png" );
        this.transferResourceToTmp( "openDepNode_xl", "png" );
        this.transferResourceToTmp( "openDepNode_xxl", "png" );
        this.transferResourceToTmp( "clashinspectorLogo", "jpg" );
        this.transferResourceToTmp( "fhReutlingenLogo", "jpg" );

        desktop.browse(this.transferResourceToTmp( "clashInspector", "html" ));
      }
      else
      {
        super.getLog().warn( "Couldn't open File with default-browser. Please open file manually under: " + System.getProperty("java.io.tmpdir")+"clashInspector.html" );
      }


      super.getLog().info( "Local ClashInspector-Server running on port "+port+". To stop server press enter." );


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


