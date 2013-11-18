package com.cedarsoft.hsrt.zkb.ourplugin;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.junit.*;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 18.11.13
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class VersionDetailServiceTest {



  @Test
  public void testResolveVersionDetails()
        {

          Artifact artifact = new DefaultArtifact( "org.apache.maven:maven-aether-provider:3.1.0" );

          DefaultDependencyNode defaultDependencyNode = new DefaultDependencyNode(artifact);

          VersionDetailService vDS = new VersionDetailService();

          vDS.resolveVersionDetails( defaultDependencyNode );



        }



}
