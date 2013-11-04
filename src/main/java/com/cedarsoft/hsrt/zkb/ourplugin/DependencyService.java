package com.cedarsoft.hsrt.zkb.ourplugin;

import com.google.common.base.Strings;
import org.apache.maven.plugin.logging.Log;
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
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;

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

  public CollectResult getDependencyTree(Artifact artifact,RepositorySystemSession repoSession, RepositorySystem repoSystem,Log log)   throws DependencyCollectionException
  {
    DefaultRepositorySystemSession session = new DefaultRepositorySystemSession(repoSession);

    session.setConfigProperty( ConflictResolver.CONFIG_PROP_VERBOSE, true );
    session.setConfigProperty( DependencyManagerUtils.CONFIG_PROP_VERBOSE, false );

                   for(int i=0; i<session.getConfigProperties().size();i++)
                   {
                     log.info( session.getConfigProperties().get( i ).toString());
                   }

    //to get dirty tree

    //session.setDependencyGraphTransformer( null );
    //session.setDependencySelector( null );
         log.info( session.getLocalRepository().toString() );
    CollectRequest collectRequest = new CollectRequest();
    //collectRequest.setRootArtifact(  artifact)   ;
    collectRequest.setRoot( new org.eclipse.aether.graph.Dependency( artifact, "" )  );
    //collectRequest.addRepository( new RemoteRepository.Builder( "central", "default", "http://repo1.maven.org/maven2/" ).build() ) ;
    CollectResult collectResult= repoSystem.collectDependencies( session, collectRequest );
    //collectResult.getRoot(). .accept( new ConsoleDependencyGraphDumper() );

    return    collectResult;

  }

    @Deprecated
    public List<Dependency> getAllDependencies(Artifact artifact,RepositorySystemSession repoSession, RepositorySystem repoSystem)
    {         //Not completely implemented
            try
            {
              for ( Dependency dependency : getDirectDependencies( artifact,repoSession,repoSystem ) ) {

                if(dependency.getScope().equals( "test" ) || dependency.isOptional() || dependency.getScope().equals( "provided" ))
                {
                  continue;
                }
                // System.out.println("-------------------------------------------------");

               // System.out.println( Strings.repeat( "  ", depth )+ dependency );
                //System.out.println( dependency.getScope() );

                //System.out.println( "dependency.isOptional() = " + dependency.isOptional() );

                //this.getAllDependencies( dependency.getArtifact(),depth+1 );
              }
            }
            catch(Exception e)
            {

            }
                return new ArrayList<Dependency>();
    }


  public List<Dependency> getDirectDependencies(Artifact artifact,RepositorySystemSession repoSession, RepositorySystem repoSystem) throws ArtifactDescriptorException
  {

    ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
    descriptorRequest.setArtifact( artifact );


    ArtifactDescriptorResult descriptorResult = repoSystem.readArtifactDescriptor( repoSession, descriptorRequest );


    return descriptorResult.getDependencies();
  }



}
