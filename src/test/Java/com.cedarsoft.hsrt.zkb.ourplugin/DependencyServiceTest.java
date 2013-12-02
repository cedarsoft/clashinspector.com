package com.cedarsoft.hsrt.zkb.ourplugin;

import com.cedarsoft.hsrt.zkb.ourplugin.model.ClashCollectResultWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.repository.LocalRepository;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 18.11.13
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class DependencyServiceTest {

  private Artifact artifact;
  private DefaultRepositorySystemSession repoSession;
  private RepositorySystem repoSystem;
  private ArrayList<String> includedScopes;
  private java.util.ArrayList<String> excludedScopes;
  private boolean includeOptional;
  DependencyService dependencyService = new DependencyService();

  //Nicht vergessen, dass die scopes etc. mitgegeben werden sollen

  @Before
  public void init()
  {
    this.repoSystem = ManualRepositorySystemFactory.newRepositorySystem();

    this.repoSession = MavenRepositorySystemUtils.newSession();

    LocalRepository localRepo = new LocalRepository( "C:\\Users\\m\\.m2\\repository" );
    this.repoSession.setLocalRepositoryManager( this.repoSystem.newLocalRepositoryManager(  this.repoSession, localRepo ) );

    //session.setTransferListener( new ConsoleTransferListener() );
    // session.setRepositoryListener( new ConsoleRepositoryListener() );

  }

  @Test
  public void testTestprojekt1()
        {

          //Artifact artifact = new DefaultArtifact( "com.cedarsoft.hsrt.zkb:ourplugin-maven-plugin:maven-plugin:0.1-SNAPSHOT" );
          Artifact artifact = new DefaultArtifact( "Testprojekt1:Testprojekt1_A:1.0-SNAPSHOT" );
         CollectResult collectResult = dependencyService.getDependencyTree( artifact, repoSession, repoSystem, false );

          ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( collectResult );


          assertEquals(0,clashCollectResultWrapper.getNumberOfOuterClashes());
          assertEquals(2,clashCollectResultWrapper.getNumberOfTotalDependencies());



        }

  @Test
  public void testTestprojekt2()
  {

    //Artifact artifact = new DefaultArtifact( "com.cedarsoft.hsrt.zkb:ourplugin-maven-plugin:maven-plugin:0.1-SNAPSHOT" );
    Artifact artifact = new DefaultArtifact( "Testprojekt2:Testprojekt2_A:1.0-SNAPSHOT" );
    CollectResult collectResult = dependencyService.getDependencyTree( artifact, repoSession, repoSystem, false );

    ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( collectResult );



    //Anzahl aller Dependencies
    assertEquals("Wrong number of total dependencies",3,clashCollectResultWrapper.getNumberOfTotalDependencies());

    //Anzahl der Clashes auf Projektebene
    assertEquals("Wrong number of total project clashes",1,clashCollectResultWrapper.getNumberOfOuterClashes());

    //Anzahl der Clashes für eine bestimmte ClashSeverity
    assertEquals("Number of Clashes with Severity Safe wrong.",0,clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.SAFE));
    assertEquals("Number of Clashes with Severity Unsafe wrong.",0,clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.UNSAFE));
    assertEquals("Number of Clashes with Severity Critical wrong.",1,clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.CRITICAL));

    //Anzahl der Clashes für eine bestimmte ClashSeverityLevel
    assertEquals("Number of Clashes with Severity for Safe wrong.",1,clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.SAFE));
    assertEquals("Number of Clashes with Severity for Unsafe wrong.",1,clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.UNSAFE));
    assertEquals("Number of Clashes with Severity for Critical wrong.",1,clashCollectResultWrapper.getNumberOfOuterClashesForSeverityLevel( ClashSeverity.CRITICAL));

  }




}
