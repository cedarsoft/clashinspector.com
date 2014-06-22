package com.clashinspector.mojos;


import com.clashinspector.DependencyService;
import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.visualize.ConsoleVisualizer;
import com.clashinspector.visualize.Visualizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * Displays the dependency tree for this project. The tree also shows version clashes.
 *
 * @since 0.3
 */
@Mojo(name = "tree", requiresProject = true, defaultPhase = LifecyclePhase.NONE)
public class ClashTreeMojo extends AbstractClashMojo {


  //big tree .. small tree und level mitgeben
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    super.execute();
    super.printStartParameter( "tree" );

    try {

      Artifact artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );

      DependencyService dependencyService = new DependencyService();
      Visualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );
      consoleVisualizer.visualize( clashCollectResultWrapper, this.getSeverity(), this );
    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }
  }


}


