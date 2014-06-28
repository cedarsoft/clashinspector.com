package com.clashinspector.model;


import com.clashinspector.mojos.ClashSeverity;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;

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
    Map<Integer, Integer> graphLevelOrderAbsoluteMap = new LinkedHashMap<Integer, Integer>();


    //Create Project for Root DependenciyWrapper ... this project is needed in html
    //TODO hier project vllt wieder entfernen
    Project project = new Project( this.collectResult.getRoot().getArtifact().getGroupId(), this.collectResult.getRoot().getArtifact().getArtifactId() );    //
    String key = this.collectResult.getRoot().getArtifact().getGroupId() + ":" + this.collectResult.getRoot().getArtifact().getArtifactId();              //
    projectMap.put( key, project ); //

    this.root = new DependencyNodeWrapper( this.collectResult.getRoot(), project );
    //Add Wrapper to Dependency Node
    this.root.getDependencyNode().setData( "RELATED_DEPENDENCY_NODE_WRAPPER", this.root );

    this.buildDependencyNodeWrapperGraph( this.root, projectMap, 1, graphLevelOrderAbsoluteMap );

    project.addInstance( this.root );
    this.root.getProject().init();      //

    this.initializeClashCollectResultWrapper( this.root, 1 );

    //completeTreeWithWinnerDependencies(this.root);
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
  private void buildDependencyNodeWrapperGraph( DependencyNodeWrapper dependencyNodeWrapperOld, Map<String, Project> projectMap, int graphDepth, Map<Integer, Integer> graphLevelOrderAbsoluteMap ) {

    int graphLevelOrderRelative = 0;


    for ( DependencyNode dN : dependencyNodeWrapperOld.getDependencyNode().getChildren() ) {


      String key = dN.getArtifact().getGroupId() + ":" + dN.getArtifact().getArtifactId();
      Project project = projectMap.get( key );

      if ( project == null ) {


        project = new Project( dN.getArtifact().getGroupId(), dN.getArtifact().getArtifactId() );

        this.projectList.add( project );
        projectMap.put( key, project );

      }

      dependencyCounter += 1;

      //Get amount of dependency on one depth level and add one more
      Integer graphLevelOrderAbsolute = graphLevelOrderAbsoluteMap.get( graphDepth );
      if ( graphLevelOrderAbsolute == null ) {
        graphLevelOrderAbsoluteMap.put( graphDepth, -1 );
        graphLevelOrderAbsolute = graphLevelOrderAbsoluteMap.get( graphDepth );

      }
      graphLevelOrderAbsolute = graphLevelOrderAbsolute + 1;
      graphLevelOrderAbsoluteMap.put( graphDepth, graphLevelOrderAbsolute );


      DependencyNodeWrapper dependencyNodeWrapper = new DependencyNodeWrapper( dN, dependencyNodeWrapperOld, project, graphDepth, graphLevelOrderRelative, graphLevelOrderAbsolute, dependencyCounter );
      dN.setData( "RELATED_DEPENDENCY_NODE_WRAPPER", dependencyNodeWrapper );

      project.addInstance( dependencyNodeWrapper );

      graphLevelOrderRelative += 1;
      dependencyNodeWrapperOld.addChildren( dependencyNodeWrapper );
      this.buildDependencyNodeWrapperGraph( dependencyNodeWrapper, projectMap, graphDepth + 1, graphLevelOrderAbsoluteMap );
    }


  }

  //TODO extend this method to build the complete tree even there are winner dependencies.
  private void completeTreeWithWinnerDependencies( DependencyNodeWrapper dependencyNodeWrapper ) {


    DependencyNode dependencyNodeWinner = ( DependencyNode ) dependencyNodeWrapper.getDependencyNode().getData().get( ConflictResolver.NODE_DATA_WINNER );

    if ( dependencyNodeWinner != null ) {

      //System.out.println( dependencyNodeWrapper.toString() + " || " +(dependencyNodeWrapper.getDependencyNode().getData().get( ConflictResolver.NODE_DATA_WINNER )));

      dependencyNodeWrapper.getChildren().addAll( ( ( DependencyNodeWrapper ) dependencyNodeWinner.getData().get( "RELATED_DEPENDENCY_NODE_WRAPPER" ) ).getChildren() );
    }


    for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {
      completeTreeWithWinnerDependencies( dNW );

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

  public List<OuterVersionClash> getOuterClashesForSeverityLevel( ClashSeverity clashSeverity ) {


    List<OuterVersionClash> outerVersionClashes = new ArrayList<OuterVersionClash>();
    for ( OuterVersionClash outerVersionClash : this.outerVersionClashList ) {
      if ( outerVersionClash.getClashSeverity().ordinal() >= clashSeverity.ordinal() ) {
        outerVersionClashes.add( outerVersionClash );
      }
    }
    return outerVersionClashes;
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
    }public boolean hasOuterVersionClash( com.clashinspector.mojos.ClashSeverity clashSeverity ) {

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
