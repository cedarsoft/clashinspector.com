package com.cedarsoft.hsrt.zkb.ourplugin;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashFullTreeMojo;
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
  private AbstractClashMojo.ClashDetectionLevel clashDetectionLevel;

  private void printFullTree( DependencyNode dependencyNode, int depth ) {
    DependencyNodeVersionDetails dNVersionDetails;


    for ( DependencyNode dN : dependencyNode.getChildren() ) {


      dNVersionDetails = ( DependencyNodeVersionDetails ) dN.getData().get( "DependencyNodeVersionDetails" );

      String preDependencyString = "";
      String preClashString = "";
      String preVersionString ="";



      for ( int i = 0; i < depth; i++ ) {
        preDependencyString = preDependencyString + "|  ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preClashString = preClashString + "   ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preVersionString = preVersionString + "|  ";
      }

      if ( dN.getChildren().size() > 0 ) {
        preDependencyString = preDependencyString + "+ ";

      } else {
        preDependencyString = preDependencyString + "- ";
        preVersionString =  preVersionString + "   ";
        preClashString = preClashString + " ";
      }


      log.info( preDependencyString + dN.toString() );

      if ( dNVersionDetails.hasVersionClash( this.clashDetectionLevel ) ) {

        String clashMessage = "";
        switch ( dNVersionDetails.getRelationShipToUsedVersion() ) {


          case EQUAL:
            //Eventuell untescheidung hinzufügen wennn die version auch die höchste ist, keine probleme anzeigen
            clashMessage =  "[Version Clash] Details:";

            break;
          case USED_VERSION_HIGHER:
            clashMessage =  "[CRITICAL Version Clash] Maven is using a higher version.";

            break;
          case USED_VERSION_LOWER:
            clashMessage =   "[FATAL Version Clash] Maven is using a lower version.";

            break;


        }
        log.error(preClashString + clashMessage );

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

          log.info( preVersionString + version.toString() + details );
        }
      }

      /*
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
          log.warn(  clashString.substring( 0,s.length()-3 ) + "[Version Clash] Maybe Maven should use higher Version. Details:" );
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

            log.info( clashString + version.toString() + details );
        }


          }
          break;
        case USED_VERSION_HIGHER:
          log.warn(    clashString.substring( 0,s.length()-3 )  + "[Version Clash] Maven is using a higher version. Details:" );
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
          log.error(  clashString.substring( 0,s.length()-1 ) + "[CRITICAL Version Clash] Maven is using a lower version. Details:" );
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
      }  */


      this.printFullTree( dN, depth + 1 );
    }
  }

  public void visualize( CollectResult collectResult, AbstractClashMojo.ClashDetectionLevel clashDetectionLevel, ClashFullTreeMojo clashFullTreeMojo ) {
    this.log = clashFullTreeMojo.getLog();
    this.clashDetectionLevel = clashDetectionLevel;
    printFullTree( collectResult.getRoot(), 0 );
  }

}
