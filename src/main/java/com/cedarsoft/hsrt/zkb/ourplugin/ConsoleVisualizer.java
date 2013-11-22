package com.cedarsoft.hsrt.zkb.ourplugin;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashFullTreeMojo;
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


  private void printFullTree( DependencyNode dependencyNode, int depth ) {
    DependencyNodeVersionDetails dNVersionDetails;


    for ( DependencyNode dN : dependencyNode.getChildren() ) {


      dNVersionDetails = ( DependencyNodeVersionDetails ) dN.getData().get( "DependencyNodeVersionDetails" );

      String s ="";

            for(int i=0;i<depth;i++)
            {
              s = s +"|  ";
            }


         if( dN.getChildren().size()>0)
         {
           s = s+ "+ " ;
         }
         else if (dNVersionDetails.hasVersionClash()==true) {
           s = s+ "  ";
         }
      else
         {
           s = s+ "- ";
         }




      log.info( s + dN.toString() );


      switch ( dNVersionDetails.getClashType() ) {
        case NONE:
          break;
        case EQUAL:
                //Wenn die benutzte Version (bereits durch equal klar) in Maven bereits die höchste version ist dann keine probleme anzeigen
         if( dNVersionDetails.getInMavenUsedVersion().toString().equals( dNVersionDetails.getHighestVersion().toString() ))
        {

        }
        else
        {
          log.warn( s+ "[Version Clash] Maybe Maven should use higher Version. Details:" );
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

            log.info( s + version.toString() + details );
        }


          }
          break;
        case USED_VERSION_HIGHER:
          log.warn( s+ "[Version Clash] Maven is using a higher version. Details:" );
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

            log.info( s + version.toString() + details );
          }


          break;
        case USED_VERSION_LOWER:
          log.error( s + "[CRITICAL Version Clash] Maven is using a lower version. Details:" );
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

            log.info( s + version.toString() + details );
          }
          break;
      }


      this.printFullTree( dN, depth + 1 );
    }
  }

  public void visualize( CollectResult collectResult, AbstractClashMojo.ClashDetectionLevel clashDetectionLevel, ClashFullTreeMojo clashFullTreeMojo ) {
    this.log = clashFullTreeMojo.getLog();
    printFullTree( collectResult.getRoot(), 0 );
  }

}
