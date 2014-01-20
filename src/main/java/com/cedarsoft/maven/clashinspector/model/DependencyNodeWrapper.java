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
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.version.Version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public class DependencyNodeWrapper {
  //DependencyNodeVersionDetails

  private final DependencyNode dependencyNode;
  private final List<DependencyNodeWrapper> children = new ArrayList<DependencyNodeWrapper>();


  //private final ArrayList<DependencyNodeWrapper> ancestors = new ArrayList<DependencyNodeWrapper>()  ;
  //Tiefe innerhalb des baumes
  private final int graphDepth;
  //  Rangfolge innerhalb einer ebene(Tiefe)
  private final int graphLevelOrder;
  //important for usedversion
  private final int addCounter;
  @Nullable
  private final DependencyNodeWrapper parent;
  @Nullable
  private final Project project;

  //relationship to used version


  public DependencyNodeWrapper( DependencyNode dependencyNode, DependencyNodeWrapper parent, Project project, int graphDepth, int graphLevelOrder, int addCounter ) {
    this.dependencyNode = dependencyNode;
    this.graphDepth = graphDepth;
    this.graphLevelOrder = graphLevelOrder;
    this.parent = parent;
    this.addCounter = addCounter;
    this.project = project;
  }

  public DependencyNodeWrapper( DependencyNode dependencyNode ) {
    this.dependencyNode = dependencyNode;
    this.graphDepth = 0;
    this.graphLevelOrder = 0;
    this.parent = null;
    this.addCounter = 0;
    this.project = null;
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
      assert parent != null;
      this.parent.collectAncestors( list );
      list.add( this );
    }

    return list;
  }


  public RelationshipToUsedVersion getRelationShipToUsedVersion() {
    if ( this.project == null ) {
      throw new UnsupportedOperationException( "Not allowed on root node" );
    }

    //compare nodeVersion with inMavenUsedVersion
    int clashResult = this.getVersion().compareTo( this.project.getUsedVersion() );


    if ( clashResult < 0 ) {
      return RelationshipToUsedVersion.USED_VERSION_HIGHER;
    } else if ( clashResult > 0 ) {
      return RelationshipToUsedVersion.USED_VERSION_LOWER;
    } else {
      return RelationshipToUsedVersion.EQUAL;
    }
  }


  public String toString() {
    return this.dependencyNode.toString();
  }


  public void addChildren( @Nonnull DependencyNodeWrapper dependencyNodeWrapper ) {
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


  @Nullable
  public Project getProject() {
    return project;
  }


  public enum RelationshipToUsedVersion {
    EQUAL( ClashSeverity.SAFE ), USED_VERSION_HIGHER( ClashSeverity.UNSAFE ), USED_VERSION_LOWER( ClashSeverity.CRITICAL );

    private final ClashSeverity clashSeverity;

    RelationshipToUsedVersion( ClashSeverity clashSeverity ) {
      this.clashSeverity = clashSeverity;
    }

    public ClashSeverity getClashSeverity() {
      return clashSeverity;
    }
  }


}
