package com.cedarsoft.hsrt.zkb.ourplugin.model;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.version.Version;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 18.11.13
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
public class DependencyNodeWrapper {
  //DependencyNodeVersionDetails

  private final DependencyNode dependencyNode;
  private final List<DependencyNodeWrapper> children = new ArrayList<DependencyNodeWrapper>();
  /**
   * Contains all DependencyWrapper with the same group and artifact id, even the actual one.
   */
  @Nullable
  private final List<DependencyNodeWrapper> dependencySiblings;

  private final List<DependencyNodeWrapper> ancestors = new ArrayList<DependencyNodeWrapper>();
  //Tiefe innerhalb des baumes
  private final int graphDepth;
  //  Rangfolge innerhalb einer ebene(Tiefe)
  private final int graphLevelOrder;
  //important for usedversion
  private final int addCounter;

  @Nullable
  private final DependencyNodeWrapper parent;


  public DependencyNodeWrapper( DependencyNode dependencyNode, @Nullable DependencyNodeWrapper parent, @Nullable List<DependencyNodeWrapper> dependencySiblings, int graphDepth, int graphLevelOrder, int addCounter ) {
    this.dependencyNode = dependencyNode;
    this.graphDepth = graphDepth;
    this.dependencySiblings = dependencySiblings;
    this.graphLevelOrder = graphLevelOrder;
    this.parent = parent;
    this.addCounter = addCounter;

  }

  public DependencyNodeWrapper( DependencyNode dependencyNode ) {
    this.dependencyNode = dependencyNode;
    this.graphDepth = 0;
    this.dependencySiblings = null;
    this.graphLevelOrder = 0;
    this.parent = null;
    this.addCounter = 0;
  }

  protected DependencyNode getDependencyNode() {
    return dependencyNode;
  }

  public String getGroupId() {
    return this.dependencyNode.getArtifact().getGroupId();
  }

  public String getArtifactId() {
    return this.dependencyNode.getArtifact().getArtifactId();
  }

  public Version getVersion() {
    return this.dependencyNode.getVersion();
  }

  public List<DependencyNodeWrapper> getChildren() {
    return Collections.unmodifiableList( this.children );
  }

  public List<DependencyNodeWrapper> getDependencySiblings() {
    //TODO what happens if this.dependencySiblings is null?
    return Collections.unmodifiableList( this.dependencySiblings );
  }

  @Nullable
  public DependencyNodeWrapper getParent() {
    return this.parent;
  }

  public List<DependencyNodeWrapper> getAllAncestors() {
    List<DependencyNodeWrapper> list = new ArrayList<DependencyNodeWrapper>();
    return this.collectAncestors( list );
  }


  private List<DependencyNodeWrapper> collectAncestors( List<DependencyNodeWrapper> list ) {
    if ( this.graphDepth != 0 ) {

      //TODO parent might be null!
      this.parent.collectAncestors( list );
      list.add( this );
    }

    return list;
  }

  public List<Version> getAllVersions() {
    List<Version> list = new ArrayList<Version>();
    //TODO dependencySiblings might be null
    for ( DependencyNodeWrapper dependencyNodeWrapper : this.dependencySiblings ) {
      list.add( dependencyNodeWrapper.getVersion() );
    }

    return list;
  }

  public Version getHighestVersion() {
    List<Version> list = this.getAllVersions();
    return Collections.max( list );
  }

  public Version getLowestVersion() {
    List<Version> list = this.getAllVersions();
    return Collections.min( list );
  }

  /**
   * Returns true or false if there is a version clash for the specified clashDetectionLevel
   *
   * @param clashDetectionLevel
   * @return
   */
  public boolean hasVersionClash( AbstractClashMojo.ClashDetectionLevel clashDetectionLevel ) {
    //Simple Clash means two different versions
    switch ( clashDetectionLevel ) {
      case ALL:
        return this.hasVersionClash();
      case CRITICAL:
        return this.getRelationShipToUsedVersion() == RelationShipToUsedVersion.USED_VERSION_LOWER || this.getRelationShipToUsedVersion() == RelationShipToUsedVersion.USED_VERSION_HIGHER;
      case FATAL:
        return this.getRelationShipToUsedVersion() == RelationShipToUsedVersion.USED_VERSION_LOWER;
    }

    throw new IllegalArgumentException( "Unsupported clash detection level " + clashDetectionLevel );
  }

  /**
   * Detects if a version clash exists (Version Clash = two equal artifacts with different versions)
   *
   * @return
   */
  public boolean hasVersionClash() {
    if ( this.getAllDifferentVersions().size() > 1 ) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the version of this node(group), that is used in Maven
   * First the graphDepth is checked. Lowest graphDepth should win. and then the order is checked. Lowest order should win. (Maven is using the first dependency)
   *
   * @return
   */
  public Version getInMavenUsedVersion() {
    //TODO Baum chekcen ob wirklihc erste version von maven verwendet wird
    DependencyNodeWrapper dependencyNodeWrapperWithUsedVersion = this;

    //TODO dependencySiblings might be null
    for ( DependencyNodeWrapper dependencyNodeWrapper : this.dependencySiblings ) {
      if ( dependencyNodeWrapperWithUsedVersion.graphDepth > dependencyNodeWrapper.graphDepth ) {
        dependencyNodeWrapperWithUsedVersion = dependencyNodeWrapper;
      } else if ( dependencyNodeWrapperWithUsedVersion.graphDepth == dependencyNodeWrapper.graphDepth ) {
        if ( dependencyNodeWrapperWithUsedVersion.addCounter > dependencyNodeWrapper.addCounter ) {
          dependencyNodeWrapperWithUsedVersion = dependencyNodeWrapper;
        }

         /* if(dependencyNodeWrapperWithUsedVersion.graphLevelOrder>dependencyNodeWrapper.graphLevelOrder)
          {
            dependencyNodeWrapperWithUsedVersion =  dependencyNodeWrapper;
          }
          else if (dependencyNodeWrapperWithUsedVersion.graphLevelOrder==dependencyNodeWrapper.graphLevelOrder)
          {
                   if(dependencyNodeWrapperWithUsedVersion.addCounter>dependencyNodeWrapper.addCounter)
                   {
                     dependencyNodeWrapperWithUsedVersion =  dependencyNodeWrapper;
                   }


          }    */

      }
    }


    return dependencyNodeWrapperWithUsedVersion.getVersion();
  }


  public List<Version> getAllDifferentVersions() {
    List<Version> differentVersions = new ArrayList<Version>();

    for ( Version version : this.getAllVersions() ) {
      if ( !differentVersions.contains( version ) ) {
        differentVersions.add( version );
      }
    }

    return Collections.unmodifiableList( differentVersions );
  }


  public RelationShipToUsedVersion getRelationShipToUsedVersion() {
    //compare nodeVersion with inMavenUsedVersion
    int clashResult = this.getVersion().compareTo( this.getInMavenUsedVersion() );

    if ( clashResult < 0 ) {
      return RelationShipToUsedVersion.USED_VERSION_HIGHER;
    } else if ( clashResult > 0 ) {
      return RelationShipToUsedVersion.USED_VERSION_LOWER;
    } else {
      return RelationShipToUsedVersion.EQUAL;
    }
  }

  public enum RelationShipToUsedVersion {
    EQUAL,
    USED_VERSION_HIGHER,
    USED_VERSION_LOWER
  }

  public String toString() {
    return this.dependencyNode.toString();
  }

  public void addChildren( DependencyNodeWrapper dependencyNodeWrapper ) {
    this.children.add( dependencyNodeWrapper );
  }

  public int getGraphDepth() {
    return graphDepth;
  }

  public int getGraphLevelOrder() {
    return graphLevelOrder;
  }

  public int getAddCounter() {
    return addCounter;
  }
}
