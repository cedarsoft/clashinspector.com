package com.cedarsoft.hsrt.zkb.ourplugin;

import com.google.common.base.Strings;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.version.Version;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 01.11.13
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleVisualizer implements Visualizer {
  Map<String, Version> map = new HashMap<String, Version>();
  private Log log;

  public ConsoleVisualizer( Log log ) {
    this.log = log;
  }

  private void printNode( DependencyNode dependencyNode, int depth ) {



    for ( DependencyNode dN : dependencyNode.getChildren() ) {

      log.info( Strings.repeat( "  ", depth ) + dN.toString() );

      String key = dN.getArtifact().getGroupId() + ":" + dN.getArtifact().getArtifactId();
      Version version = map.get( key );

      if ( version != null ) {
        if(!version.toString().equals( dN.getArtifact().getVersion().toString() ))
        {
        log.error( Strings.repeat( "  ", depth ) + "Version Clash " + version.toString() + " and " + dN.getArtifact().getVersion() + " !!!!!!!!!!!!!!!!!!!!!!" );
        }
        } else {
        map.put( key, dN.getVersion() );
      }


        /*
      log.info( Strings.repeat( "  ", depth ) + "Direct Version " + dN.getVersion() );
      log.info( Strings.repeat( "  ", depth ) + "Direct VersionConstraint " + dN.getVersionConstraint() );
      log.info( Strings.repeat( "  ", depth ) + "Artifact Base Version " + dN.getArtifact().getBaseVersion() );

      log.info( Strings.repeat( "  ", depth ) + "Artifact Version " + dN.getArtifact().getVersion() );
                       */
      this.printNode( dN, depth + 1 );
    }
  }

  public void visualize( CollectResult collectResult ) {

    printNode( collectResult.getRoot(), 0 );


  }


}
