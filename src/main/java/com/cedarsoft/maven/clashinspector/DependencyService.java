/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cedarsoft.maven.clashinspector;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2014 Behr Michael, Kampa Martin, Schneider Johannes, Zhu Huina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DependencyService {


  /**
   * This Method returns a CollectResult which includes the Root-Dependency-Node
   * with all transitive Dependency-Nodes with a scope specified by the included and excluded scope list.
   *
   * @param artifact       The root-Node for the tree.
   * @param repoSession
   * @param repoSystem
   * @param includedScopes
   * @param excludedScopes
   * @return
   *
   * @throws DependencyCollectionException
   */
  public CollectResult getDependencyTree( Artifact artifact, RepositorySystemSession repoSession, RepositorySystem repoSystem, List<String> includedScopes, List<String> excludedScopes, boolean includeOptional ) {
    DefaultRepositorySystemSession session = new DefaultRepositorySystemSession( repoSession );

    session.setConfigProperty( ConflictResolver.CONFIG_PROP_VERBOSE, true );
    session.setConfigProperty( DependencyManagerUtils.CONFIG_PROP_VERBOSE, true );

    OptionalDependencySelector oDS = new OptionalDependencySelector();
    ScopeDependencySelector sDS = new ScopeDependencySelector( includedScopes, excludedScopes );
    AndDependencySelector aDS;
    if ( includeOptional ) {
      aDS = new AndDependencySelector( sDS );
    } else {
      aDS = new AndDependencySelector( oDS, sDS );
    }
    session.setDependencySelector( aDS );


    //ConflictMarker cM = new ConflictMarker();
    //JavaScopeDeriver jSD = new JavaScopeDeriver();
    // ConflictResolver cR = new ConflictResolver();
    // session.setDependencyGraphTransformer(cM  )    ;
    // session.setDependencyGraphTransformer( cR );


    CollectRequest collectRequest = new CollectRequest();


    collectRequest.setRoot( new Dependency( artifact, "" ) );
    collectRequest.setRootArtifact( artifact );
    CollectResult collectResult;

    try {

      collectResult = repoSystem.collectDependencies( session, collectRequest );
    } catch ( DependencyCollectionException e ) {


      collectResult = e.getResult();

    }

            //System.out.println("Errrrors: "+ collectResult.getExceptions().size());
    //Fill list of versions for every dependency
    //Über alle depependency nodes iterieren und  map mit key und version erstellen


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
  public CollectResult getDependencyTree( Artifact artifact, RepositorySystemSession repoSession, RepositorySystem repoSystem, boolean includeOptional ) {


    List<String> includes = new ArrayList<String>();
    includes.add( "provided" );
    includes.add( "test" );
    includes.add( "compile" );
    includes.add( "runtime" );
    includes.add( "system" );

    List<String> excludes = new ArrayList<String>();

    return this.getDependencyTree( artifact, repoSession, repoSystem, includes, excludes, includeOptional );

  }


  @Deprecated
  public List<Dependency> getAllDependencies( Artifact artifact, RepositorySystemSession repoSession, RepositorySystem repoSystem ) {         //Not completely implemented
    try {
      for ( Dependency dependency : getDirectDependencies( artifact, repoSession, repoSystem ) ) {

        if ( dependency.getScope().equals( "test" ) || dependency.isOptional() || dependency.getScope().equals( "provided" ) ) {
        }
        // System.out.println("-------------------------------------------------");

        // System.out.println( Strings.repeat( "  ", depth )+ dependency );
        //System.out.println( dependency.getScope() );

        //System.out.println( "dependency.isOptional() = " + dependency.isOptional() );

        //this.getAllDependencies( dependency.getArtifact(),depth+1 );
      }
    } catch ( Exception e ) {
      throw new RuntimeException( e );
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
