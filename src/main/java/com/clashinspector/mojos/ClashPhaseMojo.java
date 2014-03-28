package com.clashinspector.mojos;



import com.clashinspector.DependencyService;
import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.visualize.ConsoleVisualizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * [Definition of Phase Mojo]
 */
//clashinspector
//clashfinder
@Mojo(name = "listPhase", requiresProject = true, defaultPhase = LifecyclePhase.COMPILE)
public class ClashPhaseMojo extends AbstractClashMojo {

  @Parameter( alias = "failOnError", defaultValue = "true", property = "failOnError")
  private String failOnError;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    super.execute();
    super.printStartParameter( "listPhase" , "failOnError = " +this.getFailOnError() );

    Artifact artifact;
    try {
      this.getLog().info("");
      this.getLog().info( "Starting goal :listPhase with parameters: " +  super.getStartParameter());
      this.getLog().info("");

      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );


      consoleVisualizer.visualize( clashCollectResultWrapper, this.getSeverity(), this );
      if ( this.getFailOnError() && clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( this.getSeverity())>0 ) {
        throw new MojoExecutionException( "Version Clashes for Detection-Level " + this.getSeverity() + " detected!!" );


      }


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }

  }

  public boolean getFailOnError(){
    return Boolean.valueOf(failOnError);
  }

}


