package com.cedarsoft.hsrt.zkb.ourplugin;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import com.google.common.base.Strings;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;

import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;


import java.util.ArrayList;
import java.util.List;

/**
 * Get all Dependencies of the project
 */


@Mojo(name = "dependencies", requiresProject = true)
public class DependencyMojo extends AbstractMojo {

  @Component()
  private RepositorySystem repoSystem;


  @Parameter(defaultValue = "${repositorySystemSession}")
  private RepositorySystemSession repoSession;


  @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
  private List<RemoteRepository> remoteRepos;


  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {


    Artifact artifact;
    try {
      artifact = new DefaultArtifact( project.getArtifact().toString() );

      ArrayList<String> includes = new ArrayList<String>();
      // includes.add( "test" );
      includes.add( "compile" );
      //includes.add( "provided" );

      System.out.println( this.remoteRepos.toString() );

      ArrayList<String> excludes = new ArrayList<String>();


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer( getLog() );

      consoleVisualizer.visualize( dependencyService.getDependencyTree( artifact, this.repoSession, this.repoSystem, includes, excludes, false ) );
      // consoleVisualizer.visualize( dependencyService.getDependencyTree( artifact, this.repoSession, this.repoSystem, false ) );


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }


  }


}


