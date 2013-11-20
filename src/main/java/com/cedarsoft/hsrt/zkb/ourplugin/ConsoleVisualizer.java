package com.cedarsoft.hsrt.zkb.ourplugin;

import com.google.common.base.Strings;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.version.Version;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 01.11.13
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleVisualizer implements Visualizer {

  private Log log;

  public ConsoleVisualizer( Log log ) {
    this.log = log;
  }

  private void printFullTree( DependencyNode dependencyNode, int depth ) {
    DependencyNodeVersionDetails dNVersionDetails;


    for ( DependencyNode dN : dependencyNode.getChildren() ) {


      dNVersionDetails = ( DependencyNodeVersionDetails ) dN.getData().get( "DependencyNodeVersionDetails" );


      log.info( Strings.repeat( "  ", depth ) + dN.toString() );


      switch ( dNVersionDetails.getClashType() ) {
        case NONE:
          break;
        case EQUAL:
          log.warn( Strings.repeat( "  ", depth ) + "[Version Clash] Maybe Maven should use higher Version. Details:" );
          for ( Version version : dNVersionDetails.getAllDifferentVersions() ) {
            String details = "";


            if ( version.toString().equals( dNVersionDetails.getInMavenUsedVersion().toString() ) ) {
              details = details + " (used)";
            }
            if ( version.toString().equals( dNVersionDetails.getHighestVersion().toString() ) ) {
              details = details + " (highest)";
            }
            if ( version.toString().equals( dNVersionDetails.getLowestVersion().toString() ) ) {
              details = details + " (lowest)";
            }
            if ( version.toString().equals( dNVersionDetails.getNodeVersion().toString() ) ) {
              details = details + " <- referred";
            }

            log.info( Strings.repeat( "   ", depth ) + version.toString() + details );
          }
          break;
        case USED_VERSION_HIGHER:
          log.warn( Strings.repeat( "  ", depth ) + "[Version Clash] Maven is using a higher version. Details:" );
          for ( Version version : dNVersionDetails.getAllDifferentVersions() ) {
            String details = "";


            if ( version.toString().equals( dNVersionDetails.getInMavenUsedVersion().toString() ) ) {
              details = details + " (used)";
            }
            if ( version.toString().equals( dNVersionDetails.getHighestVersion().toString() ) ) {
              details = details + " (highest)";
            }
            if ( version.toString().equals( dNVersionDetails.getLowestVersion().toString() ) ) {
              details = details + " (lowest)";
            }
            if ( version.toString().equals( dNVersionDetails.getNodeVersion().toString() ) ) {
              details = details + " <- referred";
            }

            log.info( Strings.repeat( "   ", depth ) + version.toString() + details );
          }


          break;
        case USED_VERSION_LOWER:
          log.error( Strings.repeat( "  ", depth ) + "[CRITICAL Version Clash] Maven is using a lower version. Details:" );
          for ( Version version : dNVersionDetails.getAllDifferentVersions() ) {
            String details = "";


            if ( version.toString().equals( dNVersionDetails.getInMavenUsedVersion().toString() ) ) {
              details = details + " (used)";
            }
            if ( version.toString().equals( dNVersionDetails.getHighestVersion().toString() ) ) {
              details = details + " (highest)";
            }
            if ( version.toString().equals( dNVersionDetails.getLowestVersion().toString() ) ) {
              details = details + " (lowest)";
            }
            if ( version.toString().equals( dNVersionDetails.getNodeVersion().toString() ) ) {
              details = details + " <- referred";
            }

            log.info( Strings.repeat( "   ", depth ) + version.toString() + details );
          }
          break;
      }

      this.printFullTree( dN, depth + 1 );
    }
  }

  public void visualize( CollectResult collectResult, DependencyMojo dependencyMojo ) {
    printFullTree( collectResult.getRoot(), 0 );
  }

}
