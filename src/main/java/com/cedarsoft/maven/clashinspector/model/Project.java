/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cedarsoft.maven.clashinspector.model;

import com.cedarsoft.maven.clashinspector.mojos.ClashSeverity;
import org.eclipse.aether.version.Version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright 2014 Behr Michael, Kampa Martin, Schneider Johannes, Zhu Huina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//dachdependency   ein projekt wird identifiziert durch groupid nd artifact id
public class Project {

  private final String groupId;
  private final String artifactId;
  private Version usedVersion;
  private Version highestVersion;
  private Version lowestVersion;
  private DependencyNodeWrapper dependencyNodeWrapperWithUsedVersion;

  private final List<DependencyNodeWrapper> projectInstances = new ArrayList<DependencyNodeWrapper>();
  private OuterVersionClash outerVersionClash;

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
      List<InnerVersionClash> innerVersionClashes = new ArrayList<InnerVersionClash>();

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


  public List<Version> getAllVersions() {
    List<Version> list = new ArrayList<Version>();
    for ( DependencyNodeWrapper dependencyNodeWrapper : this.projectInstances ) {
      list.add( dependencyNodeWrapper.getVersion() );
    }
    return list;
  }

  public List<Version> getAllDifferentVersions() {
    List<Version> differentVersions = new ArrayList<Version>();

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

  public List<DependencyNodeWrapper> getProjectInstances() {
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
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Project ) ) return false;

    Project project = ( Project ) o;

    if ( artifactId != null ? !artifactId.equals( project.artifactId ) : project.artifactId != null ) return false;
    if ( groupId != null ? !groupId.equals( project.groupId ) : project.groupId != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = groupId != null ? groupId.hashCode() : 0;
    result = 31 * result + ( artifactId != null ? artifactId.hashCode() : 0 );
    return result;
  }
}
