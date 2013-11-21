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
import java.util.Arrays;
import java.util.List;

/**
 * Get all Dependencies of the project
 */

//tree full tree simple
@Mojo( name = "tree", requiresProject = true, defaultPhase = LifecyclePhase.NONE )
public class ClashTreeMojo extends AbstractMojo {

  static String[] a;

  @Component()
  private RepositorySystem repoSystem;

  @Parameter( defaultValue = "${repositorySystemSession}" )
  private RepositorySystemSession repoSession;

  @Parameter( defaultValue = "${project.remoteProjectRepositories}", readonly = true )
  private List<RemoteRepository> remoteRepos;


  @Parameter( defaultValue = "${project}", readonly = true, required = true )
  private MavenProject project;

  @Parameter( alias = "includedScopes" )
  private String[] includedScopes;

  @Parameter( alias = "excludedScopes" )
  private String[] excludedScopes;

  @Parameter( alias = "includeOptional", defaultValue = "false" )
  private boolean includeOptional;


  //big tree .. small tree und level mitgeben
  public void execute() throws MojoExecutionException, MojoFailureException {

    ArrayList<String> includedScopesList = new ArrayList<String>();
    ArrayList<String> excludedScopesList = new ArrayList<String>();

    if ( this.includedScopes != null ) {
      includedScopesList.addAll( Arrays.asList( this.includedScopes ) );
    } else {
      includedScopesList.add( "compile" );
    }

    if ( this.excludedScopes != null ) {
      excludedScopesList.addAll( Arrays.asList( this.excludedScopes ) );
    } else {

    }

    Artifact artifact;
    try {
      artifact = new DefaultArtifact( project.getArtifact().toString() );


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer( getLog() );

      consoleVisualizer.visualize( dependencyService.getDependencyTree( artifact, this.repoSession, this.repoSystem, includedScopesList, excludedScopesList, includeOptional ), this );


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


