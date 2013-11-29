package com.cedarsoft.hsrt.zkb.ourplugin.model;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 20.11.13
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
  private final List<DependencyNodeWrapper> clashListUsedVersionHigher = new ArrayList<DependencyNodeWrapper>();
  private final List<DependencyNodeWrapper> clashListUsedVersionLower = new ArrayList<DependencyNodeWrapper>();
  private final List<DependencyNodeWrapper> clashListUsedVersionEqual = new ArrayList<DependencyNodeWrapper>();

  private final DependencyNodeWrapper root;
  private int dependencyCounter;

  public ClashCollectResultWrapper( CollectResult collectResult ) {

    this.collectResult = collectResult;

    Map<String, ArrayList<DependencyNodeWrapper>> dependencyMap = new LinkedHashMap<String, ArrayList<DependencyNodeWrapper>>();

    this.root = new DependencyNodeWrapper( collectResult.getRoot() );

    this.buildDependencyNodeWrapperGraph( this.root, dependencyMap, 1 );


    this.initializeClashCollectResultWrapper( this.root, 1 );
  }


  public DependencyNodeWrapper getRoot() {
    return this.root;
  }


  private void initializeClashCollectResultWrapper( DependencyNodeWrapper dependencyNodeWrapper, int depth ) {
    for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {


      if ( dNW.hasVersionClash() ) {


        if ( dNW.getRelationShipToUsedVersion() == DependencyNodeWrapper.RelationShipToUsedVersion.EQUAL ) {
          this.clashListUsedVersionEqual.add( dNW );

        } else if ( dNW.getRelationShipToUsedVersion() == DependencyNodeWrapper.RelationShipToUsedVersion.USED_VERSION_HIGHER ) {
          this.clashListUsedVersionHigher.add( dNW );

        } else if ( dNW.getRelationShipToUsedVersion() == DependencyNodeWrapper.RelationShipToUsedVersion.USED_VERSION_LOWER ) {
          this.clashListUsedVersionLower.add( dNW );
        }
      }

      this.initializeClashCollectResultWrapper( dNW, depth + 1 );
    }
  }


  //Enrich Dependency Node with versiondetails and parent pathMap   buildWrapperGraph
  private void buildDependencyNodeWrapperGraph( DependencyNodeWrapper dependencyNodeWrapperOld, Map<String, ArrayList<DependencyNodeWrapper>> dependencyMap, int graphDepth ) {

    int graphLevelOrder = 0;


    for ( DependencyNode dN : dependencyNodeWrapperOld.getDependencyNode().getChildren() ) {


      String key = dN.getArtifact().getGroupId() + ":" + dN.getArtifact().getArtifactId();
      ArrayList<DependencyNodeWrapper> dependencySiblings = dependencyMap.get( key );
      if ( dependencySiblings == null ) {


        dependencySiblings = new ArrayList<DependencyNodeWrapper>();


        dependencyMap.put( key, dependencySiblings );

      }

      dependencyCounter += 1;
      DependencyNodeWrapper dependencyNodeWrapper = new DependencyNodeWrapper( dN, dependencyNodeWrapperOld, dependencySiblings, graphDepth, graphLevelOrder, dependencyCounter );
      dependencySiblings.add( dependencyNodeWrapper );

      graphLevelOrder += 1;
      dependencyNodeWrapperOld.addChildren( dependencyNodeWrapper );
      this.buildDependencyNodeWrapperGraph( dependencyNodeWrapper, dependencyMap, graphDepth + 1 );
    }
  }


  public int getNumberOfClashes() {
    return this.clashListUsedVersionLower.size() + this.clashListUsedVersionHigher.size() + this.clashListUsedVersionEqual.size();
  }

  public int getNumberOfClashes( AbstractClashMojo.ClashDetectionLevel clashDetectionLevel ) {
    int number = 0;


    for ( DependencyNodeWrapper dNW : this.getCompleteClashList() ) {
      if ( dNW.hasVersionClash( clashDetectionLevel ) ) {
        number += 1;
      }
    }

    return number;
  }

  //Critical
  public int getNumberOfClashesWithUsedVersionHigher() {
    return this.clashListUsedVersionHigher.size();
  }

  //Fatal
  public int getNumberOfClashesWithUsedVersionLower() {
    return this.clashListUsedVersionLower.size();

  }

  public int getNumberOfClashesWithUsedVersionEqual() {


    return this.clashListUsedVersionEqual.size();
  }

  public List<DependencyNodeWrapper> getClashListUsedVersionHigher() {
    return clashListUsedVersionHigher;
  }

  public List<DependencyNodeWrapper> getClashListUsedVersionLower() {
    return clashListUsedVersionLower;
  }

  public List<DependencyNodeWrapper> getClashListUsedVersionEqual() {
    return clashListUsedVersionEqual;
  }

  public List<DependencyNodeWrapper> getCompleteClashList() {

    ArrayList<DependencyNodeWrapper> clashList = new ArrayList<DependencyNodeWrapper>();
    clashList.addAll( this.clashListUsedVersionLower );
    clashList.addAll( this.clashListUsedVersionHigher );
    clashList.addAll( this.clashListUsedVersionEqual );

    return clashList;
  }

  public List<DependencyNodeWrapper> getCompleteClashList( AbstractClashMojo.ClashDetectionLevel clashDetectionLevel ) {
    ArrayList<DependencyNodeWrapper> list = new ArrayList<DependencyNodeWrapper>();

    switch ( clashDetectionLevel ) {
      case ALL:
        list.addAll( this.clashListUsedVersionEqual );
        list.addAll( this.clashListUsedVersionLower );
        list.addAll( this.clashListUsedVersionHigher );
        break;
      case CRITICAL:
        list.addAll( this.clashListUsedVersionLower );
        list.addAll( this.clashListUsedVersionHigher );
        break;
      case FATAL:
        list.addAll( this.clashListUsedVersionLower );
        break;
    }
    return list;
  }

  /**
   * Returns a map with key (group and artifact id)  and a belonging list wit all nodeWrapper
   *
   * @return
   */
  public Map<String, List<DependencyNodeWrapper>> getClashMap( AbstractClashMojo.ClashDetectionLevel clashDetectionLevel ) {

    Map<String, List<DependencyNodeWrapper>> clashMap = new HashMap<String, List<DependencyNodeWrapper>>();

    for ( DependencyNodeWrapper dNW : this.getCompleteClashList( clashDetectionLevel ) ) {
      List<DependencyNodeWrapper> dependencyNodeList = clashMap.get( dNW.getGroupId() + ":" + dNW.getArtifactId() );
      if ( dependencyNodeList == null ) {
        dependencyNodeList = new ArrayList<DependencyNodeWrapper>();
        dependencyNodeList.add( dNW );
        clashMap.put( dNW.getGroupId() + ":" + dNW.getArtifactId(), dependencyNodeList );
      } else {
        dependencyNodeList.add( dNW );
      }
    }


    return clashMap;
  }

  public boolean hasVersionClash( AbstractClashMojo.ClashDetectionLevel clashDetectionLevel ) {

    //Simple Clash means two different versions
    boolean result = false;

    switch ( clashDetectionLevel ) {
      case ALL:
        return !this.clashListUsedVersionEqual.isEmpty() || !this.clashListUsedVersionHigher.isEmpty() || !this.clashListUsedVersionLower.isEmpty();
      case CRITICAL:
        return !this.clashListUsedVersionHigher.isEmpty() || !this.clashListUsedVersionLower.isEmpty();
      case FATAL:
        return !this.clashListUsedVersionLower.isEmpty();
    }

    throw new IllegalStateException( "Invalid clash detection level " + clashDetectionLevel );
  }
}
