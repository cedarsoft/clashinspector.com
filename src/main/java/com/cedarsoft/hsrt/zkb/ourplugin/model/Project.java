package com.cedarsoft.hsrt.zkb.ourplugin.model;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;
import org.eclipse.aether.version.Version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 29.11.13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */

//dachdependency   ein projekt wird identifiziert durch groupid nd artifact id
public class Project {

  private final String groupId;
  private final String artifactId;
  private Version usedVersion;
  private Version highestVersion;
  private Version lowestVersion;
  private DependencyNodeWrapper dependencyNodeWrapperWithUsedVersion;

  private final ArrayList<DependencyNodeWrapper> projectInstances = new ArrayList<DependencyNodeWrapper>();
  OuterVersionClash outerVersionClash;

  public Project( String groupId, String artifactId ) {


    this.groupId = groupId;
    this.artifactId = artifactId;

  }

  public void init() {
    this.dependencyNodeWrapperWithUsedVersion = this.identifyUsedDependencyNodeWrapper();
    this.usedVersion = this.identifyUsedDependencyNodeWrapper().getVersion();
    this.highestVersion = this.identifyHighestVersion();
    this.lowestVersion = this.identifyLowestVersion();

    //Detect Clashes for this project first inner than outer
    if ( this.getAllDifferentVersions().size() > 1 ) {
      ArrayList<InnerVersionClash> innerVersionClashes = new ArrayList<InnerVersionClash>();

      for ( DependencyNodeWrapper dependencyNodeWrapper : this.projectInstances ) {
        InnerVersionClash innerVersionClash = new InnerVersionClash( this.dependencyNodeWrapperWithUsedVersion, dependencyNodeWrapper );
        innerVersionClashes.add( innerVersionClash );
      }


      this.outerVersionClash = new OuterVersionClash( this, innerVersionClashes );
    }


  }


  public boolean hasOuterVersionClash() {

    if ( this.outerVersionClash == null ) {
      return false;
    } else {
      return true;
    }

  }

  /**
   * Attention, this method is considering the sequence of ClashSeverityLevel. It returns true if the clashSeverity is higher then the parameter severity
   * Alle methoden mit forClashSeverity berücksichtigen für ihr ergebnis die reihenfolge
   *
   * @param clashSeverity
   * @return
   */

  public boolean hasOuterVersionClashForClashSeverityLevel( ClashSeverity clashSeverity ) {

    if ( this.outerVersionClash == null ) {
      return false;
    } else {
      return this.outerVersionClash.isClashForSeverityLevel( clashSeverity );


    }

  }


  public ArrayList<Version> getAllVersions() {
    ArrayList<Version> list = new ArrayList<Version>();
    for ( DependencyNodeWrapper dependencyNodeWrapper : this.projectInstances ) {
      list.add( dependencyNodeWrapper.getVersion() );
    }
    return list;
  }

  public List<Version> getAllDifferentVersions() {

    ArrayList<Version> differentVersions = new ArrayList<Version>();

    for ( Version version : this.getAllVersions() ) {
      if ( !differentVersions.contains( version ) ) {
        differentVersions.add( version );
      }

    }
    return differentVersions;
  }


  private DependencyNodeWrapper identifyUsedDependencyNodeWrapper() {
    DependencyNodeWrapper dependencyNodeWrapperWithUsedVersion = this.projectInstances.get( 0 );

    for ( DependencyNodeWrapper dependencyNodeWrapper : this.projectInstances ) {
      if ( dependencyNodeWrapperWithUsedVersion.getGraphDepth() > dependencyNodeWrapper.getGraphDepth() ) {
        dependencyNodeWrapperWithUsedVersion = dependencyNodeWrapper;
      } else if ( dependencyNodeWrapperWithUsedVersion.getGraphDepth() == dependencyNodeWrapper.getGraphDepth() ) {
        if ( dependencyNodeWrapperWithUsedVersion.getAddCounter() > dependencyNodeWrapper.getAddCounter() ) {
          dependencyNodeWrapperWithUsedVersion = dependencyNodeWrapper;
        }

      }
    }
    return dependencyNodeWrapperWithUsedVersion;
  }

  private Version identifyHighestVersion() {
    return Collections.max( this.getAllVersions() );
  }

  private Version identifyLowestVersion() {
    return Collections.min( this.getAllVersions() );
  }


  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public Version getUsedVersion() {
    return usedVersion;
  }

  public Version getHighestVersion() {
    return highestVersion;
  }

  public Version getLowestVersion() {
    return lowestVersion;
  }

  public ArrayList<DependencyNodeWrapper> getProjectInstances() {
    return projectInstances;
  }

  public OuterVersionClash getOuterVersionClash() {
    return outerVersionClash;
  }

  public String toString() {
    return this.getGroupId() + ":" + this.artifactId;
  }

  public void addInstance( DependencyNodeWrapper dependencyNodeWrapper ) {
    this.projectInstances.add( dependencyNodeWrapper );
  }


  @Override
  public boolean equals( Object object ) {
    boolean result = false;

    if ( object instanceof Project ) {
      if ( ( ( Project ) object ).toString().equals( this.toString() ) ) {
        result = true;
      }
    }
    if ( object instanceof String ) {
      String s = ( String ) object;
      if ( this.toString().equals( object.toString() ) ) {
        result = true;
      }


    }

    return result;
  }
}
