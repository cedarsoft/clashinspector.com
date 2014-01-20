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

import javax.xml.transform.sax.SAXSource;
import java.util.ArrayList;
import java.util.LinkedList;
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
public class OuterVersionClash {


  private final Project project;
  //Only unsafe oder CRITICAL possible
  private final ClashSeverity clashSeverity;
  private final List<InnerVersionClash> innerVersionClashes;

  public OuterVersionClash( Project project, List<InnerVersionClash> innerVersionClashes ) {
    this.project = project;
    this.innerVersionClashes = innerVersionClashes;
    this.clashSeverity = this.detectWorstClashSeverity();
  }

  /*public ArrayList<DependencyNodeWrapper> getDependencyNodeWrapperold( ClashSeverity clashSeverity ) {
    ArrayList<DependencyNodeWrapper> list = new ArrayList<DependencyNodeWrapper>();

    for ( DependencyNodeWrapper dependencyNodeWrapper : this.project.getProjectInstances() ) {
      switch ( clashSeverity ) {
        case SAFE:
          if ( dependencyNodeWrapper.getRelationShipToUsedVersion().equals( DependencyNodeWrapper.RelationshipToUsedVersion.EQUAL ) ) {
            list.add( dependencyNodeWrapper );
          }
          break;
        case UNSAFE:
          if ( dependencyNodeWrapper.getRelationShipToUsedVersion().equals( DependencyNodeWrapper.RelationshipToUsedVersion.USED_VERSION_HIGHER ) ) {
            list.add( dependencyNodeWrapper );
          }

          break;

        case CRITICAL:
          if ( dependencyNodeWrapper.getRelationShipToUsedVersion().equals( DependencyNodeWrapper.RelationshipToUsedVersion.USED_VERSION_LOWER ) ) {
            list.add( dependencyNodeWrapper );
          }
          break;
      }
    }
    return list;
  }   */

  public LinkedList<InnerVersionClash> getInnerVersionClashForClashSeverityLevel( ClashSeverity clashSeverity ) {
    List<InnerVersionClash> safeList = new ArrayList<InnerVersionClash>();
    List<InnerVersionClash> unsafeList = new ArrayList<InnerVersionClash>();
    List<InnerVersionClash> criticalList = new ArrayList<InnerVersionClash>();
    for ( InnerVersionClash innerVersionClash : this.innerVersionClashes ) {

      switch ( innerVersionClash.getClashSeverity() ) {
        case SAFE:
          if ( clashSeverity == ClashSeverity.SAFE ) {
            safeList.add( innerVersionClash );
          }
          break;
        case UNSAFE:
          if ( clashSeverity == ClashSeverity.SAFE | clashSeverity == ClashSeverity.UNSAFE ) {
            unsafeList.add( innerVersionClash );
          }
          break;
        case CRITICAL:
          if ( clashSeverity == ClashSeverity.SAFE | clashSeverity == ClashSeverity.UNSAFE | clashSeverity == ClashSeverity.CRITICAL  ) {
            criticalList.add( innerVersionClash );
          }
          break;
      }


    }
    LinkedList<InnerVersionClash> linkedList = new LinkedList<InnerVersionClash>();

    linkedList.addAll( safeList );
    linkedList.addAll( unsafeList );
    linkedList.addAll( criticalList );
    return linkedList;
  }

  private ClashSeverity detectWorstClashSeverity() {
    ClashSeverity worstClashSeverity = ClashSeverity.SAFE;
    for ( InnerVersionClash innerVersionClash : this.innerVersionClashes ) {

      if ( worstClashSeverity.ordinal() < innerVersionClash.getClashSeverity().ordinal() ) {
        worstClashSeverity = innerVersionClash.getClashSeverity();
      }


    }
    return worstClashSeverity;
  }


  public boolean hasInnerClashesWithSeverity( ClashSeverity clashSeverity ) {
    for ( InnerVersionClash innerVersionClash : this.innerVersionClashes ) {
      if ( innerVersionClash.getClashSeverity().toString().equals( clashSeverity.toString() ) ) {
        return true;
      }
    }
    return false;
  }

  public boolean isClashForSeverityLevel( ClashSeverity clashSeverity ) {
    if ( this.clashSeverity.ordinal() >= clashSeverity.ordinal() ) {
      return true;
    } else {
      return false;
    }
  }


  public ClashSeverity getClashSeverity() {
    return clashSeverity;
  }

  public List<InnerVersionClash> getInnerVersionClashes() {
    return innerVersionClashes;
  }

  public Project getProject() {
    return project;
  }


  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    OuterVersionClash that = ( OuterVersionClash ) o;

    if ( project != null ? !project.equals( that.project ) : that.project != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return project != null ? project.hashCode() : 0;
  }
}
