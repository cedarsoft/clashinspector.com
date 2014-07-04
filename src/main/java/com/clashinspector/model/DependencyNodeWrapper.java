package com.clashinspector.model;

import com.clashinspector.mojos.ClashSeverity;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.version.Version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class DependencyNodeWrapper {
  //DependencyNodeVersionDetails

  private final DependencyNode dependencyNode;
  private final List<DependencyNodeWrapper> children = new ArrayList<DependencyNodeWrapper>();


  //private final ArrayList<DependencyNodeWrapper> ancestors = new ArrayList<DependencyNodeWrapper>()  ;
  //Tiefe innerhalb des baumes
  private final int graphDepth;
  //  Rangfolge innerhalb einer ebene(Tiefe) und einer oberdependency
  private final int graphLevelOrderRelative;
  //  Rangfolge innerhalb einer gesamten ebene(Tiefe)
  private final int graphLevelOrderAbsolute;

  //important for usedversion
  private final int addCounter;
  @Nullable
  private final DependencyNodeWrapper parent;
  @Nullable
  private final Project project;

  //relationship to used version


  public DependencyNodeWrapper( DependencyNode dependencyNode, DependencyNodeWrapper parent, Project project, int graphDepth, int graphLevelOrderRelative, int graphLevelOrderAbsolute, int addCounter ) {
    this.dependencyNode = dependencyNode;
    this.graphDepth = graphDepth;
    this.graphLevelOrderRelative = graphLevelOrderRelative;
    this.graphLevelOrderAbsolute = graphLevelOrderAbsolute;
    this.parent = parent;
    this.addCounter = addCounter;
    this.project = project;

  }

  public DependencyNodeWrapper( DependencyNode dependencyNode, Project project ) {
    this.dependencyNode = dependencyNode;
    this.graphDepth = 0;
    this.graphLevelOrderRelative = 0;
    this.graphLevelOrderAbsolute = 0;
    this.parent = null;
    this.addCounter = 0;
    this.project = project;
  }


  protected DependencyNode getDependencyNode() {
    return dependencyNode;
  }


  public String getGroupId() {

    return this.dependencyNode.getArtifact().getGroupId();

  }

  public String getExtension() {
    return this.dependencyNode.getArtifact().getExtension();

  }

  public String getScope() {

    return this.dependencyNode.getDependency().getScope();

  }

  public Boolean getOptional() {
    return this.dependencyNode.getDependency().getOptional();

  }

  public Boolean hasConcurrentDependencyWinner() {
    Object object = this.dependencyNode.getData().get( ConflictResolver.NODE_DATA_WINNER );

    if ( object != null ) {
      return true;
    } else {
      return false;
    }

  }

  public String getRepository() {
    //TODO entsprechendes Repositoriy zurück geben und nicht nur erstes (wird benötigt für link in html seite)

    if ( this.dependencyNode.getRepositories().size() > 0 ) {
      return this.dependencyNode.getRepositories().get( 0 ).getHost();
    } else {
      return "";
    }


  }

  public String getArtifactId() {
    return this.dependencyNode.getArtifact().getArtifactId();
  }


  public Version getVersion() {
    return this.dependencyNode.getVersion();
  }


  public List<DependencyNodeWrapper> getChildren() {
    return this.children;
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

  public int getGraphLevelOrderRelative() {
    return graphLevelOrderRelative;
  }


  public int getGraphLevelOrderAbsolute() {
    return graphLevelOrderAbsolute;
  }


  public int getAddCounter() {
    return addCounter;
  }


  @Nullable
  public Project getProject() {
    return project;
  }

  public String getId() {
    return "d" + this.getGraphDepth() + "r" + this.getGraphLevelOrderRelative() + "a" + this.graphLevelOrderAbsolute;
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
