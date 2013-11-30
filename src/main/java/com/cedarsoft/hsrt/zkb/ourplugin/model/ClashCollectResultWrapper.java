package com.cedarsoft.hsrt.zkb.ourplugin.model;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 20.11.13
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class ClashCollectResultWrapper {

  private final CollectResult collectResult;

  /**
   *   Map to store all clash dependencyNodes for one group- and artifact-id.
   */


  /**
   * Map to store a list with the clash dependencies with the key of groupd and artifact id
   */

  private final ArrayList<VersionClash> clashList = new ArrayList <VersionClash>();
  private final ArrayList<Project> projectList = new ArrayList<Project>();


  private DependencyNodeWrapper root;
  private int dependencyCounter = 0;

  public ClashCollectResultWrapper( CollectResult collectResult ) {

    this.collectResult = collectResult;

    Map<String, Project> projectMap = new LinkedHashMap<String, Project>();

    this.root = new DependencyNodeWrapper( this.collectResult.getRoot() );

    this.buildDependencyNodeWrapperGraph( this.root, projectMap, 1 );


    this.initializeClashCollectResultWrapper( this.root, 1 );

  }


  public DependencyNodeWrapper getRoot() {
    return this.root;
  }


  private void initializeClashCollectResultWrapper( DependencyNodeWrapper dependencyNodeWrapper, int depth ) {
    for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {




      dNW.getProject().init();

      if ( dNW.getProject().hasVersionClash()) {

        if(this.clashList.contains(dNW.getProject().getVersionClash()  )==false)
        {
          this.clashList.add( dNW.getProject().getVersionClash() )  ;
        }



      }


      this.initializeClashCollectResultWrapper( dNW, depth + 1 );
    }
  }



  //Enrich Dependency Node with versiondetails and parent pathMap   buildWrapperGraph
  private void buildDependencyNodeWrapperGraph( DependencyNodeWrapper dependencyNodeWrapperOld, Map<String, Project> projectMap, int graphDepth ) {

    int graphLevelOrder = 0;


    for ( DependencyNode dN : dependencyNodeWrapperOld.getDependencyNode().getChildren() ) {


      String key = dN.getArtifact().getGroupId() + ":" + dN.getArtifact().getArtifactId();
      Project project = projectMap.get( key );

      if ( project == null ) {


        project = new Project(dN.getArtifact().getGroupId(),dN.getArtifact().getArtifactId());

        this.projectList.add( project );
        projectMap.put( key, project );

      }

      dependencyCounter = dependencyCounter + 1;
      DependencyNodeWrapper dependencyNodeWrapper = new DependencyNodeWrapper( dN, dependencyNodeWrapperOld, project, graphDepth, graphLevelOrder, dependencyCounter );
      project.addInstance( dependencyNodeWrapper );

      graphLevelOrder = graphLevelOrder + 1;
      dependencyNodeWrapperOld.addChildren( dependencyNodeWrapper );
      this.buildDependencyNodeWrapperGraph( dependencyNodeWrapper, projectMap, graphDepth + 1 );
    }


  }


  public int getNumberOfClashes() {
    return this.clashList.size();
  }

    public int getNumberOfClashes(ClashSeverity clashSeverity)
    {
      int number =0;
         for(VersionClash versionClash : this.clashList)
         {
          if(versionClash.hasClashSeverity( clashSeverity ))
          {
            number = number +1;
          }
         }
      return number;
    }







 /* public boolean hasVersionClash( com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity clashSeverity ) {

    //Simple Clash means two different versions
    boolean result = false;

    switch ( clashSeverity ) {
      case SAFE:
        if ( this.clashListUsedVersionEqual.size() > 0 | this.clashListUsedVersionHigher.size() > 0 | this.clashListUsedVersionLower.size() > 0 ) {
          result = true;
        }
        break;
      case UNSAFE:
        if ( this.clashListUsedVersionHigher.size() > 0 | this.clashListUsedVersionLower.size() > 0 ) {
          result = true;
        }
        break;
      case CRITICAL:
        if ( this.clashListUsedVersionLower.size() > 0 ) {
          result = true;
        }
        break;
    }


    return result;
  }    */

  public ArrayList<VersionClash> getClashList() {
    return clashList;
  }

  public ArrayList<Project> getProjectList() {
    return projectList;
  }
}
