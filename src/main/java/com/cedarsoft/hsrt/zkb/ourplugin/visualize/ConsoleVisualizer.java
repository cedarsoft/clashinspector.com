package com.cedarsoft.hsrt.zkb.ourplugin.visualize;

import com.cedarsoft.hsrt.zkb.ourplugin.model.ClashCollectResultWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.model.DependencyNodeWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.model.Project;
import com.cedarsoft.hsrt.zkb.ourplugin.model.VersionClash;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashListMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashPhaseMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashTreeMojo;
import com.google.common.base.Strings;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.version.Version;

import java.util.ArrayList;
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
  private ClashSeverity clashSeverity;

  private void printFullTree( DependencyNodeWrapper dependencyNodeWrapper, int depth ) {


    for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {


      String preDependencyString = "";
      String preClashString = "";
      String preVersionString = "";


      for ( int i = 0; i < depth; i++ ) {
        preDependencyString = preDependencyString + "|  ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preClashString = preClashString + "|  ";
      }
      for ( int i = 0; i < depth; i++ ) {
        preVersionString = preVersionString + "|  ";
      }

      if ( dNW.getChildren().size() > 0 ) {
        preDependencyString = preDependencyString + "+ ";
        preVersionString = preVersionString + "   ";
        preClashString = preClashString + "  ";

      } else {
        preDependencyString = preDependencyString + "- ";
        preVersionString = preVersionString + "   ";
        preClashString = preClashString + "  ";
      }


      log.info( preDependencyString + dNW.toString() );


      if ( dNW.getProject().hasVersionClashForClashSeverityLevel( this.clashSeverity ) ) {

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
            details = details + " (used)";
          }
          if ( version.toString().equals( dNW.getProject().getHighestVersion().toString() ) ) {
            details = details + " (highest)";
          }
          if ( version.toString().equals( dNW.getProject().getLowestVersion().toString() ) ) {
            details = details + " (lowest)";
          }
          if ( version.toString().equals( dNW.getVersion().toString() ) ) {
            details = details + " <- referred";
          }

          log.info( preVersionString + version.toString() + details );
        }
      }


      this.printFullTree( dNW, depth + 1 );
    }
  }



  private void printListPartHeader(VersionClash versionClash)
  {
    log.info( "-------------------------------------" );
    log.info( "["+versionClash.getWorstClashSeverity()+" Version Clash] " + " " + versionClash.getProject().toString() + "" );
    log.info( " (used: " + versionClash.getProject().getUsedVersion() + " highest: " + versionClash.getProject().getHighestVersion() + " lowest: " + versionClash.getProject().getLowestVersion() + ")" );
    log.info( "-------------------------------------" );
  }
  private void printListSmallTree(DependencyNodeWrapper dNW)
  {

    String clashMessage = dNW.getRelationShipToUsedVersion().getClashSeverity().name();


    log.info( clashMessage );


    ArrayList<DependencyNodeWrapper> listAncestors = ( ArrayList ) dNW.getAllAncestors();

    for ( DependencyNodeWrapper dNWA : listAncestors ) {
      log.info( Strings.repeat( " ", dNWA.getGraphDepth() ) + dNWA.toString() );

    }


  }

  private void printList( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity ) {


    for (VersionClash versionClash : clashCollectResultWrapper.getClashList() ) {


         if( versionClash.isClashForSeverityLevel( clashSeverity ))
         {

               this.printListPartHeader( versionClash );

                 for(DependencyNodeWrapper dependencyNodeWrapper :versionClash.getDependencyNodeWrapperForClashSeverityLevel( clashSeverity ))
                 {
                   this.printListSmallTree( dependencyNodeWrapper );
                 }
           log.info( "" );



         }


      }

    }






  public void printStatistic( ClashCollectResultWrapper clashCollectResultWrapper ) {
   /* log.info( "" );
    log.info( "Statistical information:" );
    log.info( "Number of Clashes with severity SAFE: " + clashCollectResultWrapper.getNumberOfClashes( ClashSeverity.SAFE ) );




    for ( Map.Entry e : clashCollectResultWrapper.getClashMap( ClashSeverity.SAFE ).entrySet() ) {
      ArrayList<DependencyNodeWrapper> list = ( ArrayList ) e.getValue();
      String s = list.get( 0 ).getWorstRelationshipToUsedVersion().toString();

      log.info( "["+s+" Version Clash] " + " " + e.getKey().toString() + "" );


    }  */

    log.info( "Number of Clashes with severity UNSAFE: " + clashCollectResultWrapper.getNumberOfClashes( ClashSeverity.UNSAFE ) );

    for (VersionClash versionClash : clashCollectResultWrapper.getClashList() ) {


      if( versionClash.getWorstClashSeverity().equals( ClashSeverity.UNSAFE ))
      {
        log.info( " ["+versionClash.getWorstClashSeverity()+" Version Clash] " + " " + versionClash.getProject().toString() + "" );
      }
    }

    log.info( "Number of Clashes with severity CRITICAL: " + clashCollectResultWrapper.getNumberOfClashes( ClashSeverity.CRITICAL ) );
    for (VersionClash versionClash : clashCollectResultWrapper.getClashList() ) {


      if( versionClash.getWorstClashSeverity().equals( ClashSeverity.CRITICAL ))
      {
        log.info( " ["+versionClash.getWorstClashSeverity()+" Version Clash] " + " " + versionClash.getProject().toString() + "" );
      }
    }
  }

  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashTreeMojo clashTreeMojo ) {
    this.log = clashTreeMojo.getLog();
    this.clashSeverity = clashSeverity;
    printFullTree( clashCollectResultWrapper.getRoot(), 0 );
    printStatistic( clashCollectResultWrapper );

  }

  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashListMojo clashListMojo ) {
    this.log = clashListMojo.getLog();
    this.clashSeverity = clashSeverity;
    printList( clashCollectResultWrapper, clashSeverity );
    printStatistic( clashCollectResultWrapper );
  }

  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashPhaseMojo clashPhaseMojo ) {
   this.log = clashPhaseMojo.getLog();    /*
    this.clashSeverity = clashSeverity;
    printList( clashCollectResultWrapper, clashSeverity );
    printStatistic( clashCollectResultWrapper );    */

    log.info(String.valueOf( clashCollectResultWrapper.getProjectList().size()) );
    for(Project project : clashCollectResultWrapper.getProjectList()  )
    {
      log.info( project.toString() + " " + project.hasVersionClashForClashSeverityLevel( clashSeverity ) );
    }
           log.info("");
    log.info(String.valueOf( clashCollectResultWrapper.getClashList().size()) );
    for(VersionClash versionClash : clashCollectResultWrapper.getClashList() )
    {
      log.info( versionClash.getProject().toString()+ " worst: " + versionClash.getWorstClashSeverity() );
    }

  }


}
