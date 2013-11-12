package com.cedarsoft.hsrt.zkb.ourplugin;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.util.graph.manager.ClassicDependencyManager;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.util.version.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 01.11.13
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public class DependencyService {


  /**
   * This Method returns a CollectResult which includes the Root-Dependency-Node
   * with all transitive Dependency-Nodes with a scope specified by the included and excluded scope list.
   *
   * @param artifact  The root-Node for the tree.
   * @param repoSession
   * @param repoSystem
   * @param includedScopes
   * @param excludedScopes
   * @return
   *
   * @throws DependencyCollectionException
   */
  public CollectResult getDependencyTree(Artifact artifact, RepositorySystemSession repoSession, RepositorySystem repoSystem, ArrayList<String> includedScopes, ArrayList<String> excludedScopes ) throws DependencyCollectionException {
    DefaultRepositorySystemSession session = new DefaultRepositorySystemSession( repoSession );

    session.setConfigProperty( ConflictResolver.CONFIG_PROP_VERBOSE, true );
    session.setConfigProperty( DependencyManagerUtils.CONFIG_PROP_VERBOSE, true );

    OptionalDependencySelector oDS = new OptionalDependencySelector( );




    ScopeDependencySelector sDS = new ScopeDependencySelector( includedScopes, excludedScopes );




    AndDependencySelector aDS = new AndDependencySelector( oDS,sDS );

    session.setDependencySelector( aDS );

    CollectRequest collectRequest = new CollectRequest();



    collectRequest.setRoot( new org.eclipse.aether.graph.Dependency( artifact, "" ) );
    collectRequest.setRootArtifact( artifact );
    //collectRequest.addManagedDependency(  )


    CollectResult collectResult = repoSystem.collectDependencies( session, collectRequest );

    session.setConfigProperty( ConflictResolver.CONFIG_PROP_VERBOSE, false );

    return collectResult;

  }

  /**
   * This Method returns a CollectResult which includes the Root-Dependency-Node
   * with all transitive Dependency-Nodes. Included scopes of the dependencies are: (provided,test,compile,runtime,system).
   *
   * @param artifact    The root-Node for the tree.
   * @param repoSession
   * @param repoSystem
   * @return
   *
   * @throws DependencyCollectionException
   */
  public CollectResult getDependencyTree( Artifact artifact, RepositorySystemSession repoSession, RepositorySystem repoSystem ) throws DependencyCollectionException {


    ArrayList<String> includes = new ArrayList<String>();
    includes.add( "provided" );
    includes.add( "test" );
    includes.add( "compile" );
    includes.add( "runtime" );
    includes.add( "system" );

    ArrayList<String> excludes = new ArrayList<String>();

    return this.getDependencyTree( artifact, repoSession, repoSystem, includes, excludes );

  }


  @Deprecated
  public List<Dependency> getAllDependencies( Artifact artifact, RepositorySystemSession repoSession, RepositorySystem repoSystem ) {         //Not completely implemented
    try {
      for ( Dependency dependency : getDirectDependencies( artifact, repoSession, repoSystem ) ) {

        if ( dependency.getScope().equals( "test" ) || dependency.isOptional() || dependency.getScope().equals( "provided" ) ) {
          continue;
        }
        // System.out.println("-------------------------------------------------");

        // System.out.println( Strings.repeat( "  ", depth )+ dependency );
        //System.out.println( dependency.getScope() );

        //System.out.println( "dependency.isOptional() = " + dependency.isOptional() );

        //this.getAllDependencies( dependency.getArtifact(),depth+1 );
      }
    } catch ( Exception e ) {

    }
    return new ArrayList<Dependency>();
  }

  @Deprecated
  public List<Dependency> getDirectDependencies( Artifact artifact, RepositorySystemSession repoSession, RepositorySystem repoSystem ) throws ArtifactDescriptorException {

    ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
    descriptorRequest.setArtifact( artifact );


    ArtifactDescriptorResult descriptorResult = repoSystem.readArtifactDescriptor( repoSession, descriptorRequest );


    return descriptorResult.getDependencies();
  }


}
