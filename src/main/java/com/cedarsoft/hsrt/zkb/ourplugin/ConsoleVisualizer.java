package com.cedarsoft.hsrt.zkb.ourplugin;

import com.google.common.base.Strings;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 01.11.13
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleVisualizer implements Visualizer {

  private Log log;
  private VersionDetailService service;

  public ConsoleVisualizer( Log log ) {
    this.log = log;
  }

  private void printTree( DependencyNode dependencyNode, int depth )  {
    DependencyNodeVersionDetails dNVersionDetails;
    for ( DependencyNode dN : dependencyNode.getChildren() ) {
      dNVersionDetails = service.resolveVersionDetails( dN );
      log.info( Strings.repeat( "  ", depth ) + dN.toString() );

      switch ( dNVersionDetails.getClashType() ) {
        case NONE:
          break;
        case EQUAL:
          break;
        case USED_VERSION_HIGHER:
          log.warn(  "[Version Clash] Maven is using a higher version " + dNVersionDetails.getInMavenUsedVersion() );
          log.warn( "----------------------------" );
          break;
        case USED_VERSION_LOWER:
          log.warn( "[FATAL Version Clash] Maven is using a lower version " + dNVersionDetails.getInMavenUsedVersion() );
          log.warn( "----------------------------" );
          break;
      }

      this.printTree( dN, depth + 1 );
    }
  }

  public void visualize( CollectResult collectResult, DependencyMojo dependencyMojo ){
    this.service = new VersionDetailService();
    printTree( collectResult.getRoot(), 0 );
  }

}
