package com.cedarsoft.hsrt.zkb.ourplugin.model;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 29.11.13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
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
          if ( clashSeverity.equals( ClashSeverity.SAFE ) ) {
            safeList.add( innerVersionClash );
          }
          break;
        case UNSAFE:
          if ( clashSeverity.equals( ClashSeverity.SAFE ) | clashSeverity.equals( ClashSeverity.UNSAFE ) ) {
            unsafeList.add( innerVersionClash );
          }
          break;
        case CRITICAL:
          if ( clashSeverity.equals( ClashSeverity.SAFE ) | clashSeverity.equals( ClashSeverity.CRITICAL ) | clashSeverity.equals( ClashSeverity.CRITICAL ) ) {
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

      if ( worstClashSeverity.ordinal() < innerVersionClash.getClashSeverity().ordinal() )
        worstClashSeverity = innerVersionClash.getClashSeverity();


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
  public boolean equals( Object object ) {
    boolean result = false;

    if ( object instanceof OuterVersionClash ) {
      OuterVersionClash vC = ( OuterVersionClash ) object;

      if ( vC.getProject().getGroupId().equals( this.getProject().getGroupId() ) && vC.getProject().getArtifactId().equals( this.getProject().getArtifactId() ) ) {
        result = true;
      }
    }

    return result;
  }
}
