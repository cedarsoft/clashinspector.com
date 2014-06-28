package com.clashinspector.mojos;


import com.clashinspector.DependencyService;
import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.model.OuterVersionClash;
import com.clashinspector.visualize.ConsoleVisualizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

import java.util.List;

/**
 * Displays the dependencies with version clashes during a specified build phase.
 *
 * @since 0.9
 */
@Mojo( name = "listPhase", requiresProject = true, defaultPhase = LifecyclePhase.COMPILE )
public class ClashPhaseMojo extends AbstractClashMojo {

  /**
   * Defines if the specified build phase will be aborted if a version clash of the defined severity was detected.
   *
   * @since 0.9
   */
  @Parameter( alias = "failOnClash", defaultValue = "true", property = "failOnClash" )
  private String failOnClash;

  /**
   * Defines the dependencies which will be ignored of this mojo.
   *
   * @since 0.9
   */
  @Parameter( alias = "whiteList", property = "whiteList" )
  private List<WhiteListDependency> whiteList;


  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    super.execute();
    super.printStartParameter( "listPhase", "failOnClash = " + this.getFailOnClash() );

    Artifact artifact;
    try {


      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );


      consoleVisualizer.visualize( clashCollectResultWrapper, this.getSeverity(), this );


      if ( this.getFailOnClash() && clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( this.getSeverity() ) > 0 ) {


        //if every outer version clash is in the whiteList, then no Excpetion has to be thrown
        for ( OuterVersionClash outerVersionClash : clashCollectResultWrapper.getOuterVersionClashList() ) {

          if ( outerVersionClash.hasAllInnerClashesInWhiteList( whiteList ) == false ) {
            throw new MojoExecutionException( "Version Clashes for Detection-Level " + this.getSeverity() + " detected!!" );
          }
        }

        //beim failen muss die white liste berücksichtigt werden

      }


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }

  }

  //Searching for entrys in whitelist
  private void resolveWhiteList() {

  }

  public boolean getFailOnClash() {
    return Boolean.valueOf( failOnClash );
  }

}


