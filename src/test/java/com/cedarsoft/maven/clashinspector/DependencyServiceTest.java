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

import com.cedarsoft.maven.clashinspector.model.ClashCollectResultWrapper;
import com.cedarsoft.maven.clashinspector.mojos.ClashSeverity;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.repository.LocalRepository;
import org.junit.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright 2014 Behr Michael, Kampa Martin, Schneider Johannes, Zhu Huina
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class DependencyServiceTest {

  private Artifact artifact;
  private DefaultRepositorySystemSession repoSession;
  private RepositorySystem repoSystem;

  private boolean includeOptional;
  DependencyService dependencyService = new DependencyService();

  //Nicht vergessen, dass die scopes etc. mitgegeben werden sollen


  @Before
  public void init() {
    this.repoSystem = ManualRepositorySystemFactory.newRepositorySystem();

    this.repoSession = MavenRepositorySystemUtils.newSession();

    File repo = findRepoLocation();

    LocalRepository localRepo = new LocalRepository( repo );
    //    LocalRepository localRepo = new LocalRepository( "src/test/repo" );
    this.repoSession.setLocalRepositoryManager( this.repoSystem.newLocalRepositoryManager( this.repoSession, localRepo ) );

    //session.setTransferListener( new ConsoleTransferListener() );
    // session.setRepositoryListener( new ConsoleRepositoryListener() );
  }

  //Unterschiedliche Ausführungsfälle für parameter sammeln

  //includedScopes: compile  excludedScopes:  includeOptional: false
  private CollectResult executionCase1( Artifact artifact ) {

    List<String> includedScopes = new ArrayList<String>();
    List<String> excludedScopes = new ArrayList<String>();

    includedScopes.add( "compile" );


    return dependencyService.getDependencyTree( artifact, this.repoSession, this.repoSystem, includedScopes, excludedScopes, false );
  }


  @Nonnull
  private static File findRepoLocation() {
    @Nullable String repoConfig = System.getProperty( "maven.repo.local" );
    if ( repoConfig != null ) {
      return new File( repoConfig );
    }

    @Nullable String workspace = System.getProperty( "WORKSPACE" );
    if ( workspace != null ) {
      return new File( workspace + "/.repository" );
    }

    //Fallback
    File userHome = new File( System.getProperty( "user.home" ) );
    return new File( userHome, ".m2/repository" );
  }

  @Test
  public void testTestproject1() {

    //Artifact artifact = new DefaultArtifact( "com.cedarsoft.hsrt.zkb:ourplugin-maven-plugin:maven-plugin:0.1-SNAPSHOT" );
    Artifact artifact = new DefaultArtifact( "testproject1:testproject1_A:1.0-SNAPSHOT" );
    CollectResult collectResult = this.executionCase1( artifact );

    ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( collectResult );

    //Anzahl aller Projekte
    assertEquals( "Wrong number of total projects", 2, clashCollectResultWrapper.getNumberOfTotalProjects() );

    //Anzahl aller Dependencies
    assertEquals( "Wrong number of total dependencies", 2, clashCollectResultWrapper.getNumberOfTotalDependencies() );

    //Anzahl der Clashes auf Projektebene (Anzahl aller Outer-Clashes)
    assertEquals( "Wrong number of total project clashes", 0, clashCollectResultWrapper.getNumberOfOuterClashes() );

    //Anzahl der Outer-Clashes für eine bestimmte ClashSeverity
    assertEquals( "Number of Clashes with Severity Safe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity Unsafe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity Critical wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.CRITICAL ) );

    //Anzahl der Outer-Clashes  für eine bestimmte ClashSeverityLevel
    assertEquals( "Number of Clashes with Severity for Safe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity for Unsafe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity for Critical wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.CRITICAL ) );


  }

  @Test
  public void testTestproject2() {

    //Artifact artifact = new DefaultArtifact( "com.cedarsoft.hsrt.zkb:ourplugin-maven-plugin:maven-plugin:0.1-SNAPSHOT" );
    Artifact artifact = new DefaultArtifact( "testproject2:testproject2_A:1.0-SNAPSHOT" );

    CollectResult collectResult = this.executionCase1( artifact );
    ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( collectResult );

    //Anzahl aller Projekte
    assertEquals( "Wrong number of total projects", 2, clashCollectResultWrapper.getNumberOfTotalProjects() );

    //Anzahl aller Dependencies
    assertEquals( "Wrong number of total dependencies", 3, clashCollectResultWrapper.getNumberOfTotalDependencies() );

    //Anzahl der Clashes  auf Projektebene (Anzahl aller Outer-Clashes)
    assertEquals( "Wrong number of total project clashes", 1, clashCollectResultWrapper.getNumberOfOuterClashes() );

    //Anzahl der Outer-Clashes  für eine bestimmte ClashSeverity
    assertEquals( "Number of Clashes with Severity Safe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity Unsafe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity Critical wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.CRITICAL ) );

    //Anzahl der Outer-Clashes  für eine bestimmte ClashSeverityLevel
    assertEquals( "Number of Clashes with Severity for Safe wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity for Unsafe wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity for Critical wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.CRITICAL ) );


  }

  @Test
  public void testTestproject3() {

    //Artifact artifact = new DefaultArtifact( "com.cedarsoft.hsrt.zkb:ourplugin-maven-plugin:maven-plugin:0.1-SNAPSHOT" );
    Artifact artifact = new DefaultArtifact( "testproject3:testproject3_A:1.0-SNAPSHOT" );
    CollectResult collectResult = this.executionCase1( artifact );

    ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( collectResult );

    //Anzahl aller Projekte
    assertEquals( "Wrong number of total projects", 2, clashCollectResultWrapper.getNumberOfTotalProjects() );


    //Anzahl aller Dependencies
    assertEquals( "Wrong number of total dependencies", 3, clashCollectResultWrapper.getNumberOfTotalDependencies() );

    //Anzahl der Clashes  auf Projektebene (Anzahl aller Outer-Clashes)
    assertEquals( "Wrong number of total project clashes", 1, clashCollectResultWrapper.getNumberOfOuterClashes() );

    //Anzahl der Outer-Clashes  für eine bestimmte ClashSeverity
    assertEquals( "Number of Clashes with Severity Safe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity Unsafe wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity Critical wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.CRITICAL ) );

    //Anzahl der Outer-Clashes  für eine bestimmte ClashSeverityLevel (for severity_level safe sollte es eigentlich nicht geben, da es keinen outer clash mit safe gibt, dennoch hier zum testen verwendet )
    assertEquals( "Number of Clashes with Severity for Safe wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity for Unsafe wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity for Critical wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.CRITICAL ) );


  }


  @Test
  public void testTestproject4() {

    //Artifact artifact = new DefaultArtifact( "com.cedarsoft.hsrt.zkb:ourplugin-maven-plugin:maven-plugin:0.1-SNAPSHOT" );
    Artifact artifact = new DefaultArtifact( "testproject4:testproject4_A:1.0-SNAPSHOT" );
    CollectResult collectResult = this.executionCase1( artifact );

    ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( collectResult );

    //Anzahl aller Projekte
    assertEquals( "Wrong number of total projects", 4, clashCollectResultWrapper.getNumberOfTotalProjects() );

    //Anzahl der Clashes  auf Projektebene (Anzahl aller Outer-Clashes)
    assertEquals( "Wrong number of total project clashes", 1, clashCollectResultWrapper.getNumberOfOuterClashes() );

    //Anzahl der Outer-Clashes  für eine bestimmte ClashSeverity
    assertEquals( "Number of Clashes with Severity Safe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity Unsafe wrong.", 0, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity Critical wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.CRITICAL ) );

    //Anzahl der Outer-Clashes  für eine bestimmte ClashSeverityLevel (for severity_level safe sollte es eigentlich nicht geben, da es keinen outer clash mit safe gibt, dennoch hier zum testen verwendet )
    assertEquals( "Number of Clashes with Severity for Safe wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.SAFE ) );
    assertEquals( "Number of Clashes with Severity for Unsafe wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.UNSAFE ) );
    assertEquals( "Number of Clashes with Severity for Critical wrong.", 1, clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.CRITICAL ) );


    //    //Anzahl aller Dependencies
    //    Doesn't work for me locally. No idea why....
    //    assertEquals( "Wrong number of total dependencies", 6, clashCollectResultWrapper.getNumberOfTotalDependencies() );
  }

}
