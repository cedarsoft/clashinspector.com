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

import com.cedarsoft.maven.clashinspector.mojos.ClashSeverity;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClashCollectResultWrapper {

  private final CollectResult collectResult;


  //This list cotains all projectVersionClashes ... im gegesatz steht dependencyVersionClash
  private final List<OuterVersionClash> outerVersionClashList = new ArrayList<OuterVersionClash>();
  private final List<Project> projectList = new ArrayList<Project>();


  private final DependencyNodeWrapper root;
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

      if ( dNW.getProject().hasOuterVersionClash() ) {

        if ( !this.outerVersionClashList.contains( dNW.getProject().getOuterVersionClash() ) ) {
          this.outerVersionClashList.add( dNW.getProject().getOuterVersionClash() );
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


        project = new Project( dN.getArtifact().getGroupId(), dN.getArtifact().getArtifactId() );

        this.projectList.add( project );
        projectMap.put( key, project );

      }

      dependencyCounter += 1;

      DependencyNodeWrapper dependencyNodeWrapper = new DependencyNodeWrapper( dN, dependencyNodeWrapperOld, project, graphDepth, graphLevelOrder, dependencyCounter );
      project.addInstance( dependencyNodeWrapper );

      graphLevelOrder += 1;
      dependencyNodeWrapperOld.addChildren( dependencyNodeWrapper );
      this.buildDependencyNodeWrapperGraph( dependencyNodeWrapper, projectMap, graphDepth + 1 );
    }


  }


  public int getNumberOfOuterClashes() {
    return this.outerVersionClashList.size();
  }

  public int getNumberOfOuterClashes( ClashSeverity clashSeverity ) {
    int number = 0;
    for ( OuterVersionClash outerVersionClash : this.outerVersionClashList ) {
      if ( outerVersionClash.getClashSeverity() == clashSeverity ) {
        number += 1;
      }
    }
    return number;
  }

  public int getNumberOfOuterClashesForSeverityLevel( ClashSeverity clashSeverity ) {
    int number = 0;
    for ( OuterVersionClash outerVersionClash : this.outerVersionClashList ) {
      if ( outerVersionClash.getClashSeverity().ordinal() >= clashSeverity.ordinal() ) {
        number += 1;
      }
    }
    return number;
  }





 /* public int getNumberOfOuterClashes(ClashSeverity clashSeverity)
    {
      int number =0;
         for(OuterVersionClash outerVersionClash : this.outerVersionClashList )
         {
          if( outerVersionClash.hasClashSeverity( clashSeverity ))
          {
            number = number +1;
          }
         }
      return number;
    }public boolean hasOuterVersionClash( com.cedarsoft.maven.clashinspector.mojos.ClashSeverity clashSeverity ) {

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

  public List<OuterVersionClash> getOuterVersionClashList() {
    return outerVersionClashList;
  }

  public List<Project> getProjectList() {
    return projectList;
  }


  public int getNumberOfTotalDependencies() {
    return this.dependencyCounter;
  }

  //Projects are defined by their group and artifactid (not by their version)
  public int getNumberOfTotalProjects() {
    return this.projectList.size();
  }

  public List<Exception> getExceptions() {
    return this.collectResult.getExceptions();

  }


}
