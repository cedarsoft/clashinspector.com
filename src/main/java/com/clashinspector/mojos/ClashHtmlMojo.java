package com.clashinspector.mojos;




import com.clashinspector.DependencyService;
import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.visualize.ConsoleVisualizer;
import com.clashinspector.visualize.Visualizer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import javax.ws.rs.*;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

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

      //ResourceConfig config = new DefaultResourceConfig(rest.class);


      HttpServer server = HttpServerFactory.create( "http://localhost:8080/test/" );
      server.start();




      super.getLog().info( "To stop server press enter" );


      BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

      String inLine = in.readLine();
      server.stop( 0 );


    }
    catch(Exception e)
      {
                     super.getLog().error( e );
      }



  }


}


