package com.clashinspector.visualize;

import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.model.DependencyNodeWrapper;
import com.clashinspector.model.InnerVersionClash;
import com.clashinspector.model.OuterVersionClash;
import com.clashinspector.mojos.ClashListMojo;
import com.clashinspector.mojos.ClashPhaseMojo;
import com.clashinspector.mojos.ClashSeverity;
import com.clashinspector.mojos.ClashTreeMojo;
import com.clashinspector.visualize.util.ConsoleVisualizeHelper;
import com.google.common.base.Strings;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.version.Version;


public class ConsoleVisualizer implements Visualizer {

  private Log log;
  private ClashSeverity clashSeverity;

  private void printFullTree( DependencyNodeWrapper dependencyNodeWrapper, int depth ) {


    if ( depth == 0 ) {

      log.info( ConsoleVisualizeHelper.createSectionHeader( " tree ", ConsoleVisualizeHelper.LogLevel.INFO ) );
      log.info( "" );
    }


    for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {


      String preDependencyString = "";
      String preClashString = "";
      String preVersionString = "";


      for ( int i = 0; i < depth; i++ ) {
        preDependencyString += "|  ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preClashString += "|  ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preVersionString += "|  ";
      }

      if ( !dNW.getChildren().isEmpty() ) {
        preDependencyString += "+ ";
        preVersionString += "   ";
        preClashString += "  ";

      } else {
        preDependencyString += "- ";
        preVersionString += "   ";
        preClashString += "  ";
      }


      log.info( preDependencyString + dNW.toString());


      if ( dNW.getProject().hasOuterVersionClashForClashSeverityLevel( this.clashSeverity ) ) {

        String clashMessage = "";
        switch ( dNW.getRelationShipToUsedVersion().getClashSeverity() ) {


          case SAFE:
            //Eventuell untescheidung hinzufügen wennn die version auch die höchste ist, keine probleme anzeigen
            clashMessage = "[SAFE Version Clash] Details:";

            break;
          case UNSAFE:
            clashMessage = "[UNSAFE Version Clash] Referred Version lower than used Version.";

            break;
          case CRITICAL:
            clashMessage = "[CRITICAL Version Clash] Referred Version higher than used Version.";

            break;


        }
        log.info( preClashString + clashMessage );

        for ( Version version : dNW.getProject().getAllDifferentVersions() ) {
          String details = "";


          if ( version.toString().equals( dNW.getProject().getUsedVersion().toString() ) ) {
            details += " (used)";
          }
          if ( version.toString().equals( dNW.getProject().getHighestVersion().toString() ) ) {
            details += " (highest)";
          }
          if ( version.toString().equals( dNW.getProject().getLowestVersion().toString() ) ) {
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


  private void printListPartHeader( OuterVersionClash outerVersionClash ) {
    log.info( "-------------------------------------" );
    log.info( "[" + outerVersionClash.getClashSeverity() + " Version Clash] " + " " + outerVersionClash.getProject().toString() + "" );
    log.info( " (used: " + outerVersionClash.getProject().getUsedVersion() + " highest: " + outerVersionClash.getProject().getHighestVersion() + " lowest: " + outerVersionClash.getProject().getLowestVersion() + ")" );
    log.info( "-------------------------------------" );
  }

  private void printListSmallTree( DependencyNodeWrapper dNW ) {

    String clashMessage = dNW.getRelationShipToUsedVersion().getClashSeverity().name();


    log.info( clashMessage );

    for ( DependencyNodeWrapper dNWA : dNW.getAllAncestors() ) {
      log.info( Strings.repeat( " ", dNWA.getGraphDepth() ) + dNWA.toString() );
    }
  }

  private void printList( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity ) {


    log.info( ConsoleVisualizeHelper.createSectionHeader( " list ", ConsoleVisualizeHelper.LogLevel.INFO ) );
    log.info( "" );
    for ( OuterVersionClash outerVersionClash : clashCollectResultWrapper.getOuterVersionClashList() ) {


      if ( outerVersionClash.isClashForSeverityLevel( clashSeverity ) ) {

        this.printListPartHeader( outerVersionClash );

        for ( InnerVersionClash innerVersionClash : outerVersionClash.getInnerVersionClashForClashSeverityLevel( clashSeverity ) ) {
          this.printListSmallTree( innerVersionClash.getReferredDependencyNodeWrapper() );
        }
        log.info( "" );


      }


    }

  }


  public void printSummary( ClashCollectResultWrapper clashCollectResultWrapper ) {
   /* log.info( "" );
    log.info( "Statistical information:" );
    log.info( "Number of Clashes with severity SAFE: " + clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.SAFE ) );




    for ( Map.Entry e : clashCollectResultWrapper.getClashMap( ClashSeverity.SAFE ).entrySet() ) {
      ArrayList<DependencyNodeWrapper> list = ( ArrayList ) e.getValue();
      String s = list.get( 0 ).getWorstRelationshipToUsedVersion().toString();

      log.info( "["+s+" Version Clash] " + " " + e.getKey().toString() + "" );


    }  */

    log.info( ConsoleVisualizeHelper.createSectionHeader( " summary ", ConsoleVisualizeHelper.LogLevel.INFO ) );
    log.info( "" );
    log.info( "Number of Clashes with severity UNSAFE: " + clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.UNSAFE ) );

    for ( OuterVersionClash outerVersionClash : clashCollectResultWrapper.getOuterVersionClashList() ) {


      if ( outerVersionClash.getClashSeverity() == ClashSeverity.UNSAFE ) {
        log.info( " [" + outerVersionClash.getClashSeverity() + " Version Clash] " + " " + outerVersionClash.getProject().toString() + "" );
      }
    }

    log.info( "Number of Clashes with severity CRITICAL: " + clashCollectResultWrapper.getNumberOfOuterClashes( ClashSeverity.CRITICAL ) );
    for ( OuterVersionClash outerVersionClash : clashCollectResultWrapper.getOuterVersionClashList() ) {


      if ( outerVersionClash.getClashSeverity() == ClashSeverity.CRITICAL ) {
        log.info( " [" + outerVersionClash.getClashSeverity() + " Version Clash] " + " " + outerVersionClash.getProject().toString() + "" );
      }
    }
    log.info( "" );
  }

  private void printErrors( ClashCollectResultWrapper clashCollectResultWrapper, String errorMessage ) {


    if ( clashCollectResultWrapper.getExceptions().size() > 0 ) {
      log.info( "" );
      log.info( ConsoleVisualizeHelper.createSectionHeader( " error information ", ConsoleVisualizeHelper.LogLevel.INFO ) );
      log.info( "" );
      log.info( "Attention!!! Following Errors occured while dependency-resolving." + errorMessage );
      for ( Exception exception : clashCollectResultWrapper.getExceptions() ) {
        log.info( exception.getMessage() );
      }

    }


  }


  @Override
  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashTreeMojo clashTreeMojo ) {
    this.log = clashTreeMojo.getLog();
    this.clashSeverity = clashSeverity;
    printFullTree( clashCollectResultWrapper.getRoot(), 0 );
    log.info( "" );
    printSummary( clashCollectResultWrapper );


    printErrors( clashCollectResultWrapper, "Tree potentially incomplete!" );
  }


  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashListMojo clashListMojo ) {
    this.log = clashListMojo.getLog();
    this.clashSeverity = clashSeverity;
    printList( clashCollectResultWrapper, clashSeverity );
    printSummary( clashCollectResultWrapper );
    printErrors( clashCollectResultWrapper, "List potentially incomplete!" );
  }

  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashPhaseMojo clashPhaseMojo ) {
    this.log = clashPhaseMojo.getLog();
    this.clashSeverity = clashSeverity;
    printList( clashCollectResultWrapper, clashSeverity );
    printSummary( clashCollectResultWrapper );
    printErrors( clashCollectResultWrapper, "List potentially incomplete!" );


  }


}
