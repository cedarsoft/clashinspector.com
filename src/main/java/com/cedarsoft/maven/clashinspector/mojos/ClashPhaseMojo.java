package com.cedarsoft.maven.clashinspector.mojos;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import com.cedarsoft.maven.clashinspector.DependencyService;
import com.cedarsoft.maven.clashinspector.model.ClashCollectResultWrapper;
import com.cedarsoft.maven.clashinspector.visualize.ConsoleVisualizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * Get all Dependencies of the project
 */
//clashinspector
//clashfinder
@Mojo(name = "listPhase", requiresProject = true, defaultPhase = LifecyclePhase.COMPILE)
public class ClashPhaseMojo extends AbstractClashMojo {

  @Parameter( alias = "failOnError", defaultValue = "true" )
  private boolean failOnError;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    super.execute();


    Artifact artifact;
    try {
      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );


      consoleVisualizer.visualize( clashCollectResultWrapper, this.getClashDetectionLevel(), this );
      if ( this.failOnError ) {
        throw new MojoExecutionException( "Version Clashes for Detection-Level " + this.getClashDetectionLevel() + " detected!!" );


      }


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }

  }


}


