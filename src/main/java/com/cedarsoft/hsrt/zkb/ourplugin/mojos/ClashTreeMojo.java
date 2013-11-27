package com.cedarsoft.hsrt.zkb.ourplugin.mojos;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import com.cedarsoft.hsrt.zkb.ourplugin.ClashCollectResultWrapper;
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
public class ClashTreeMojo extends AbstractClashMojo {




  //big tree .. small tree und level mitgeben
  public void execute() throws MojoExecutionException, MojoFailureException {

            super.execute();



    Artifact artifact;
    try {
      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer( );

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper(dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() )  ) ;

      consoleVisualizer.visualize(clashCollectResultWrapper , this.getClashDetectionLevel(), this );


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }
  }


}


