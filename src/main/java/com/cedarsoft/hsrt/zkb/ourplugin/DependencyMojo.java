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
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;




import org.sonatype.aether.resolution.ArtifactDescriptorRequest;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;



import java.util.List;
/**
 * Get all Dependencies of the project
 *
 */


  @Mojo( name="dependencies",requiresProject = true )
public class DependencyMojo extends AbstractMojo {

  @Component()
  private RepositorySystem repoSystem;


  @Parameter(defaultValue = "${repositorySystemSession}")
  private RepositorySystemSession repoSession;


  @Parameter(defaultValue = "${project.remoteProjectRepositories}",readonly = true)
  private List<RemoteRepository> remoteRepos;


  @Parameter(defaultValue = "${project}",readonly = true,required = true)
  private MavenProject project;

  public void execute() throws MojoExecutionException, MojoFailureException {


    Artifact artifact;
    try {
      artifact = new DefaultArtifact( project.getArtifact().toString() );
    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }

    try {
      //this.getAllDependencies( artifact,0 );
      this.getDependencyTree( artifact );
    } catch ( Exception e ) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }


  public void resolveTransitiveDependencies( Artifact artifact ) {
    // DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter( JavaScopes.COMPILE );

    CollectRequest collectRequest = new CollectRequest();
    //collectRequest.setRoot( new Dependency( artifact, JavaScopes.COMPILE ) );


      /*DependencyRequest dependencyRequest = new DependencyRequest( collectRequest, classpathFlter );

      List<ArtifactResult> artifactResults =
        system.resolveDependencies( session, dependencyRequest ).getArtifactResults();

      for ( ArtifactResult artifactResult : artifactResults )
      {
        System.out.println( artifactResult.getArtifact() + " resolved to " + artifactResult.getArtifact().getFile() );
      }      */

  }

  public void getAllDependencies( Artifact artifact , int depth) throws MojoExecutionException, MojoFailureException, Exception {





  }


  public void getDependencyTree( Artifact artifact ) throws Exception {


    DependencyService dependencyService = new DependencyService();

         ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();
   consoleVisualizer.visualize(dependencyService.getDependencyTree( artifact,this.repoSession,this.repoSystem ,getLog()) ,getLog() );

  }

  public void getDirectDependencies( Artifact artifact ) throws MojoExecutionException, MojoFailureException, Exception {


    //  ArtifactRequest request = new ArtifactRequest();
    //LocalArtifactRequest lrequest = new LocalArtifactRequest(  );




    //this.remoteRepos.add( rR );
    //request.setRepositories( this.remoteRepos );


    // LocalArtifactRequest request2 = new LocalArtifactRequest(  );


    // CollectRequest collectRequest = new CollectRequest(  ) ;
    //ArtifactResolutionRequest     request1 = new   ArtifactResolutionRequest();
    // request1.setRemoteRepositories( this.project.getRemoteArtifactRepositories() ) ;
    //   request1.setLocalRepository( new ArtifactRepository() ) ;


    //  ArtifactResolutionRequest aRRequest = new  ArtifactResolutionRequest();

        /*  getLog().info( "Resolving artifact " + artifact + " from " + remoteRepos );

          ArtifactResult result;
          try
          {

            result = repoSystem.resolveArtifact( repoSession, request );
        repoSystem.resolveArtifact( repoSession,  new ArtifactRequest(....).getArtifact())    ;
                    getLog().info( result.getArtifact(). );
          }
          catch ( ArtifactResolutionException e )
          {
            throw new MojoExecutionException( e.getMessage(), e );
          }

          getLog().info( "Resolved artifact " + artifact + " to " + result.getArtifact().getFile() + " from "
                           + result.getRepository() );
        }
               */
  }

}


