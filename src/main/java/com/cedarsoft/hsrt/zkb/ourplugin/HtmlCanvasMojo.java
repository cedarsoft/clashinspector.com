package com.cedarsoft.hsrt.zkb.ourplugin;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.RemoteRepository;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.List;
/**
 * Get all Dependencies of the project
 *
 */



public class HtmlCanvasMojo extends AbstractMojo {
  /**
   * The entry point to Aether, i.e. the component doing all the work.
   *
   * @component
   */
  private RepositorySystem repoSystem;

  /**
   * The current repository/network configuration of Maven.
   *
   * @parameter default-value="${repositorySystemSession}"
   */
  private RepositorySystemSession repoSession;

  /**
   * The project's remote repositories to use for the resolution.
   *
   * @parameter default-value="${project.remoteProjectRepositories}"
   * @readonly
   */
  private List<RemoteRepository> remoteRepos;

  //TemplateEngine:
  //VElocity
  //Freemarker
  /**
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      File file = new File( "D:\\tefdst.html" );

      if ( !file.exists() ) {
        file.createNewFile();
      }

      FileWriter fw = new FileWriter( file.getAbsoluteFile() );
      BufferedWriter bw = new BufferedWriter( fw );
      bw.write( "<!DOCTYPE html>\n" +
                  "<html>\n" +
                  "<head>\n" +
                  "    <title></title>\n" +
                  "\n" +
                  "    <script>\n" +
                  "\n" +
                  "        function draw()\n" +
                  "        {\n" +
                  "                  var canvas = document.getElementById(\"canvas\");\n" +
                  "        if(canvas.getContext)\n" +
                  "        {\n" +
                  "            var context = canvas.getContext(\"2d\");\n" +
                  "       // context.fillStyle=\"rgb(255,0,255)\";\n" +
                  "        context.fillRect(0,0,canvas.width,canvas.height);\n" +
                  "ctx.font='30px Arial';" +
                  "ctx.fillText('Dependency',10,50)" +
                  "               }\n" +
                  "        }\n" +
                  "\n" +
                  "\n" +
                  "\n" +
                  "\n" +
                  "    </script>\n" +
                  "\n" +
                  "</head>\n" +
                  "<body onload=\"draw()\">\n" +
                  "<canvas id=\"canvas\" width=\"100%\" height=\"100%\">\n" +
                  "                               Fallback\n" +
                  "</canvas>\n" +
                  "\n" +
                  "\n" +
                  "</body>\n" +
                  "</html>" );


      bw.close();

      Desktop desktop = Desktop.getDesktop();

      //Adresse mit Standardbrowser anzeigen
      URI uri;

      uri = new URI( "D:/tefdst.html" );
      desktop.browse( uri );

    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }


}


