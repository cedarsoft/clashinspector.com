package com.cedarsoft.hsrt.zkb.ourplugin.mojos;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import com.cedarsoft.hsrt.zkb.ourplugin.ConsoleVisualizer;
import com.cedarsoft.hsrt.zkb.ourplugin.DependencyService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Get all Dependencies of the project
 */
                //clashinspector
              //clashfinder
@Mojo(name = "clashdetectorphase", requiresProject = true, defaultPhase = LifecyclePhase.COMPILE)
public class ClashPhaseMojo extends AbstractMojo {

  @Component()
  private RepositorySystem repoSystem;

  @Parameter(defaultValue = "${repositorySystemSession}")
  private RepositorySystemSession repoSession;

  @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
  private List<RemoteRepository> remoteRepos;

  @Parameter( alias = "includedScopes" , defaultValue ="provided")
  private String[] includedScopes;

  @Parameter( alias = "excludedScopes" , defaultValue ="provided")
  private String[] excludedScopes;


  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {


    Artifact artifact;
    try {
      artifact = new DefaultArtifact( project.getArtifact().toString() );
      System.out.println( "Artifact to string: " +project.getArtifact().toString() );
      ArrayList<String> includes = new ArrayList<String>();
      // includes.add( "test" );
      includes.add( "compile" );
      //includes.add( "provided" );

      System.out.println( this.remoteRepos.toString() );

      ArrayList<String> excludes = new ArrayList<String>();


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer( getLog() );

      //consoleVisualizer.visualize( dependencyService.getDependencyTree( artifact, this.repoSession, this.repoSystem, includes, excludes, false ), this );
      // consoleVisualizer.visualize( dependencyService.getDependencyTree( artifact, this.repoSession, this.repoSystem, false ) );


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }
  }


  public void setIncludedScopes( String[] includedScopes ) {
    this.includedScopes = includedScopes;
  }

  public void setExcludedScopes( String[] excludedScopes ) {
    this.excludedScopes = excludedScopes;
  }
}


