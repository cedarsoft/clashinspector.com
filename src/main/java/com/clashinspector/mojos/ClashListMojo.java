package com.clashinspector.mojos;


import com.clashinspector.DependencyService;
import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.visualize.ConsoleVisualizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * Displays the dependencies with version clashes and a statistical summary of the clashes.
 *
 * @since 0.3
 */
@Mojo( name = "list", requiresProject = true, defaultPhase = LifecyclePhase.NONE )
public class ClashListMojo extends AbstractClashMojo {


  //big tree .. small tree und level mitgeben
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    super.execute();
    super.printStartParameter( "list" );

    Artifact artifact;
    try {

      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );

      consoleVisualizer.visualize( clashCollectResultWrapper, this.getSeverity(), this );


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }
  }


}


