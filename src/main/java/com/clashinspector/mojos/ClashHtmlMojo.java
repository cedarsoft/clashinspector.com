package com.clashinspector.mojos;




import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.rest.DependencyRestService;
import com.clashinspector.rest.UserParameterWrapper;

import com.clashinspector.visualize.ConsoleVisualizer;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.sun.net.httpserver.HttpServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


import javax.ws.rs.ProcessingException;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * Opens a HTML tool for dynamic analysis of dependencies and dependency version clashes.
 *
 * @since 0.9
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



      UserParameterWrapper userParameterWrapper = new UserParameterWrapper(this.getIncludedScopesList(),this.getExcludedScopesList(),this.isIncludeOptional(),this.getSeverity()  );
        //ViewScopeManager initialisieren
      DependencyRestService.init( artifact,this.getRepoSystem(),this.getRepoSession(), userParameterWrapper );

      //TODO port eventuell variabel machen

      BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

      ResourceConfig config = new ResourceConfig(DependencyRestService.class);
      HttpServer server = this.startServer( config ) ;

      if (Desktop.isDesktopSupported())
      {
        Desktop desktop=Desktop.getDesktop();


        File tempDir = com.google.common.io.Files.createTempDir();
        tempDir.deleteOnExit();

        this.transferResourceToTmp( "clashInspectorStyle", "css",tempDir,false );
        this.transferResourceToTmp( "jquery-1.11.0", "js",tempDir ,false);
        this.transferResourceToTmp( "main", "js",tempDir,true );
        this.transferResourceToTmp( "openDepNode", "png",tempDir,false );
        this.transferResourceToTmp( "openDepNode_hasWinner", "png",tempDir,false );
        this.transferResourceToTmp( "clashinspectorLogo", "jpg",tempDir,false );
        this.transferResourceToTmp( "fhReutlingenLogo", "jpg",tempDir,false );
        this.transferResourceToTmp( "legendArrowHasDependencies", "png",tempDir,false );
        this.transferResourceToTmp( "legendArrowHasUnsafeDependencies", "png",tempDir,false );
        this.transferResourceToTmp( "legendArrowHasCriticalDependencies", "png",tempDir,false );
        this.transferResourceToTmp( "legendArrowHasUnresolvedDependencies", "png",tempDir,false );
        this.transferResourceToTmp( "legendScopes_long", "png",tempDir,false );
        this.transferResourceToTmp( "legendOptional", "png",tempDir,false );




        desktop.browse(this.transferResourceToTmp( "clashInspector", "html",tempDir,false ));
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


      private HttpServer startServer(ResourceConfig resourceConfig) throws Exception {


        try
        {
          HttpServer server = JdkHttpServerFactory.createHttpServer(new URI( "http://localhost:"+port+"/"), resourceConfig );
          return server;
        }
        catch(ProcessingException processingException)
        {
          if(this.port>8050)
          {
                this.port = this.port -1;
               return this.startServer( resourceConfig );
          }
          else
          {
            super.getLog().error( "No free port for clashinspector-server found" );
            throw new Exception("No free port for clashinspector-server found");
          }
        }



      }




       //Puts file into tmp-folder
     private URI transferResourceToTmp(String fileName,String fileEnding, File tmpDir,boolean replacePortInJSFile)
     {
       fileEnding = fileEnding.replace( ".","" );
            fileEnding = "."+fileEnding;

       InputStream resource = getClass().getResourceAsStream("/"+fileName+fileEnding );

       try
       {

       if(replacePortInJSFile)
       {
         String replace = new String( ByteStreams.toByteArray( resource ), Charsets.UTF_8 ).replace( "{replacePort}", String.valueOf( this.port ) );
         resource = new ByteArrayInputStream( replace.getBytes(Charsets.UTF_8));
       }





         File file = new File(tmpDir,fileName+fileEnding);


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


