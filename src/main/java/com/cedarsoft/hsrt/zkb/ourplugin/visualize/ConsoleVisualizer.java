package com.cedarsoft.hsrt.zkb.ourplugin.visualize;

import com.cedarsoft.hsrt.zkb.ourplugin.model.ClashCollectResultWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.model.DependencyNodeWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashListMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashPhaseMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashTreeMojo;
import com.google.common.base.Strings;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.version.Version;

import java.util.List;
import java.util.Map;

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

  private void printFullTree( DependencyNodeWrapper dependencyNodeWrapper, int depth ) {


    for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {


      String preDependencyString = "";
      String preClashString = "";
      String preVersionString = "";

      for ( int i = 0; i < depth; i++ ) {
        preDependencyString += "|  ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preClashString += "   ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preVersionString += "|  ";
      }

      if ( dNW.getChildren().isEmpty() ) {
        preDependencyString += "- ";
        preVersionString += "   ";
        preClashString += " ";
      } else {
        preDependencyString += "+ ";
        preVersionString += "   ";
        preClashString += " ";
      }

      log.info( preDependencyString + dNW.toString() );


      if ( dNW.hasVersionClash( this.clashDetectionLevel ) ) {

        String clashMessage = "";
        switch ( dNW.getRelationShipToUsedVersion() ) {


          case EQUAL:
            //Eventuell untescheidung hinzufügen wennn die version auch die höchste ist, keine probleme anzeigen
            clashMessage = "[Version Clash] Details:";

            break;
          case USED_VERSION_HIGHER:
            clashMessage = "[CRITICAL Version Clash] Maven is using a higher version.";

            break;
          case USED_VERSION_LOWER:
            clashMessage = "[FATAL Version Clash] Maven is using a lower version.";

            break;


        }
        log.error( preClashString + clashMessage );

        for ( Version version : dNW.getAllDifferentVersions() ) {
          String details = "";


          if ( version.toString().equals( dNW.getInMavenUsedVersion().toString() ) ) {
            details += " (used)";
          }
          if ( version.toString().equals( dNW.getHighestVersion().toString() ) ) {
            details += " (highest)";
          }
          if ( version.toString().equals( dNW.getLowestVersion().toString() ) ) {
            details += " (lowest)";
          }
          if ( version.toString().equals( dNW.getVersion().toString() ) ) {
            details += " <- referred";
          }

          log.info( preVersionString + version.toString() + details );
        }
      }


      this.printFullTree( dNW, depth + 1 );
    }
  }


  private void printList( ClashCollectResultWrapper clashCollectResultWrapper, AbstractClashMojo.ClashDetectionLevel clashDetectionLevel ) {
    for ( Map.Entry<String, List<DependencyNodeWrapper>> entry : clashCollectResultWrapper.getClashMap( clashDetectionLevel ).entrySet() ) {
      log.info( "-------------------------------------" );
      log.info( "" );
      log.info( "Different Versions for " + entry.getKey() + ":" );


      List<DependencyNodeWrapper> list = entry.getValue();

      log.info( "(used: " + list.get( 0 ).getInMavenUsedVersion() + " highest: " + list.get( 0 ).getHighestVersion() + " lowest: " + list.get( 0 ).getLowestVersion() + ")" );
      log.info( "" );

      for ( DependencyNodeWrapper dNW : list ) {

        String clashMessage = "";
        switch ( dNW.getRelationShipToUsedVersion() ) {


          case EQUAL:
            //Eventuell untescheidung hinzufügen wennn die version auch die höchste ist, keine probleme anzeigen
            clashMessage = "[Version Clash]";

            break;
          case USED_VERSION_HIGHER:
            clashMessage = "[CRITICAL Version Clash]";

            break;
          case USED_VERSION_LOWER:
            clashMessage = "[FATAL Version Clash]";

            break;


        }

        log.info( clashMessage );

        for ( DependencyNodeWrapper nodeWrapper : dNW.getAllAncestors() ) {
          log.info( Strings.repeat( " ", nodeWrapper.getGraphDepth() ) + nodeWrapper.toString() );

        }

        log.info( "" );
      }
    }
  }

  public void printStatistic( ClashCollectResultWrapper clashCollectResultWrapper ) {
    log.info( "" );
    log.info( "Statistical information:" );
    log.info( "Complete Number of Clashes with Detection-Level " + clashDetectionLevel + ": " + clashCollectResultWrapper.getNumberOfClashes( clashDetectionLevel ) );
    log.info( "Number of Clashes with Version equal with used Version: " + clashCollectResultWrapper.getNumberOfClashesWithUsedVersionEqual() );
    log.info( "Number of Clashes with Version lower than used Version: " + clashCollectResultWrapper.getNumberOfClashesWithUsedVersionHigher() );
    log.info( "Number of Clashes with Version higher than used Version: " + clashCollectResultWrapper.getNumberOfClashesWithUsedVersionLower() );
  }

  @Override
  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, AbstractClashMojo.ClashDetectionLevel clashDetectionLevel, ClashTreeMojo clashTreeMojo ) {
    this.log = clashTreeMojo.getLog();
    this.clashDetectionLevel = clashDetectionLevel;
    printFullTree( clashCollectResultWrapper.getRoot(), 0 );
    printStatistic( clashCollectResultWrapper );

  }

  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, AbstractClashMojo.ClashDetectionLevel clashDetectionLevel, ClashListMojo clashListMojo ) {
    this.log = clashListMojo.getLog();
    this.clashDetectionLevel = clashDetectionLevel;
    printList( clashCollectResultWrapper, clashDetectionLevel );
    printStatistic( clashCollectResultWrapper );
  }

  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, AbstractClashMojo.ClashDetectionLevel clashDetectionLevel, ClashPhaseMojo clashPhaseMojo ) {
    this.log = clashPhaseMojo.getLog();
    this.clashDetectionLevel = clashDetectionLevel;
    printList( clashCollectResultWrapper, clashDetectionLevel );
    printStatistic( clashCollectResultWrapper );
  }


}
