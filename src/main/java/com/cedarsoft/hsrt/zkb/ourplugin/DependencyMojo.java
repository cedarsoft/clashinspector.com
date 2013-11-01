package com.cedarsoft.hsrt.zkb.ourplugin;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactDescriptorRequest;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;

import java.util.List;
/**
 * Get all Dependencies of the project
 *
 */


  @Mojo( name="dependencies",requiresProject = true)
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
      this.getAllDependencies( artifact );
    } catch ( Exception e ) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }


  public void resolveTransitiveDependencies( Artifact artifact ) {
    // DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter( JavaScopes.COMPILE );

    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot( new Dependency( artifact, JavaScopes.COMPILE ) );


      /*DependencyRequest dependencyRequest = new DependencyRequest( collectRequest, classpathFlter );

      List<ArtifactResult> artifactResults =
        system.resolveDependencies( session, dependencyRequest ).getArtifactResults();

      for ( ArtifactResult artifactResult : artifactResults )
      {
        System.out.println( artifactResult.getArtifact() + " resolved to " + artifactResult.getArtifact().getFile() );
      }      */

  }

  public void getAllDependencies( Artifact artifact ) throws MojoExecutionException, MojoFailureException, Exception {

    for ( Dependency dependency : getDirectDependencies( artifact ) ) {
      System.out.println( dependency );
      System.out.println( dependency.getScope() );
      this.getAllDependencies( dependency.getArtifact() );
    }


  }


  public void getDependencyTree( Artifact artifact ) throws Exception {


    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot( new Dependency( artifact, "" ) );


    CollectResult collectResult = this.repoSystem.collectDependencies( this.repoSession, collectRequest );


    for ( DependencyNode dN : collectResult.getRoot().getChildren() ) {
      System.out.println( dN );
      for ( DependencyNode dN1 : dN.getChildren() ) {
        System.out.println( dN1 );
      }

    }


  }

  public List<Dependency> getDirectDependencies( Artifact artifact ) throws MojoExecutionException, MojoFailureException, Exception {


    //  ArtifactRequest request = new ArtifactRequest();
    //LocalArtifactRequest lrequest = new LocalArtifactRequest(  );

    ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
    descriptorRequest.setArtifact( artifact );


    ArtifactDescriptorResult descriptorResult = this.repoSystem.readArtifactDescriptor( this.repoSession, descriptorRequest );

    for ( Dependency dependency : descriptorResult.getDependencies() ) {
      // System.out.println( dependency );
    }
    return descriptorResult.getDependencies();


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


