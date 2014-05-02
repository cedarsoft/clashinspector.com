package com.clashinspector.mojos;




import com.clashinspector.service.rest;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jndi.toolkit.url.Uri;
import com.sun.net.httpserver.HttpServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;


import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
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



    try
    {

      BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

      ResourceConfig config = new DefaultResourceConfig(rest.class);
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



      // Browse a URL, say google.com

     // URL resourceUrl = getClass().getClassLoader().getResource("main.js");

      //InputStream inputStream =
       // getClass().getClassLoader().getResourceAsStream("config.properties");

      //prop.load(inputStream);
      //filePath = prop.getProperty("json.filepath");
      //getClass().getResourceAsStream(  )
     // File htmlFile = new File(resourceUrl.toURI());
    //  Desktop.getDesktop().browse(new URI("/src/main/resources/main.js"));
     // d.browse(new URI(resourceUrl.toString()));

      //d.open( new URI( resourceUrl.toString() ) );
      super.getLog().info( "To stop local ClashInspector-Server press enter." );


      String inLine = in.readLine();
      server.stop( 0 );


    }
    catch(Exception e)
      {
                     super.getLog().error( e );
      }



  }








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


