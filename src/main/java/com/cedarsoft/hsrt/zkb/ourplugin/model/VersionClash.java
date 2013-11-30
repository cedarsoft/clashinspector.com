package com.cedarsoft.hsrt.zkb.ourplugin.model;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 29.11.13
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class VersionClash {

  //Severity
  private final Project project;

  public VersionClash( Project project ) {
    this.project = project;
  }

  public ArrayList<DependencyNodeWrapper> getDependencyNodeWrapper( ClashSeverity clashSeverity ) {
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
  }

  public LinkedList<DependencyNodeWrapper> getDependencyNodeWrapperForClashSeverityLevel( ClashSeverity clashSeverity ) {
    ArrayList<DependencyNodeWrapper> safeList = new ArrayList<DependencyNodeWrapper>();
    ArrayList<DependencyNodeWrapper> unsafeList = new ArrayList<DependencyNodeWrapper>();
    ArrayList<DependencyNodeWrapper> criticalList = new ArrayList<DependencyNodeWrapper>();
    for ( DependencyNodeWrapper dependencyNodeWrapper : this.project.getProjectInstances() ) {

      switch ( dependencyNodeWrapper.getRelationShipToUsedVersion() ) {
        case EQUAL:
                if(clashSeverity.equals( ClashSeverity.SAFE )  )
                {
                  safeList.add( dependencyNodeWrapper );
                }
          break;
        case USED_VERSION_HIGHER:
          if(clashSeverity.equals( ClashSeverity.SAFE ) | clashSeverity.equals( ClashSeverity.UNSAFE )  )
          {
           unsafeList.add( dependencyNodeWrapper );
          }
          break;
        case USED_VERSION_LOWER:
          if(clashSeverity.equals( ClashSeverity.SAFE )| clashSeverity.equals( ClashSeverity.CRITICAL )| clashSeverity.equals( ClashSeverity.CRITICAL )  )
          {
            criticalList.add( dependencyNodeWrapper );
          }
          break;
      }


    }
   LinkedList<DependencyNodeWrapper> linkedList = new LinkedList<DependencyNodeWrapper>(  );

    linkedList.addAll( safeList );
    linkedList.addAll( unsafeList );
    linkedList.addAll( criticalList );
    return linkedList;
  }

  public ClashSeverity getWorstClashSeverity() {
    ClashSeverity worstClashSeverity = ClashSeverity.SAFE;
    for ( DependencyNodeWrapper dNW : this.project.getProjectInstances() ) {
      if ( worstClashSeverity.ordinal() < dNW.getRelationShipToUsedVersion().ordinal() )
        worstClashSeverity = dNW.getRelationShipToUsedVersion().getClashSeverity();


    }
    return worstClashSeverity;
  }


  public boolean hasClashSeverity( ClashSeverity clashSeverity ) {
    for ( DependencyNodeWrapper dependencyNodeWrapper : this.project.getProjectInstances() ) {
      if ( dependencyNodeWrapper.getRelationShipToUsedVersion().getClashSeverity().toString().equals( clashSeverity.toString() ) ) {
        return true;
      }
    }
    return false;
  }

  public boolean isClashForSeverityLevel( ClashSeverity clashSeverity ) {
    if ( this.getWorstClashSeverity().ordinal() >= clashSeverity.ordinal() ) {
      return true;
    } else {
      return false;
    }
  }


  public boolean hasClashSeverities( ClashSeverity... clashSeverity ) {


    for ( ClashSeverity clashSeverity1 : clashSeverity ) {


      for ( DependencyNodeWrapper dependencyNodeWrapper : this.project.getProjectInstances() ) {
        if ( dependencyNodeWrapper.getRelationShipToUsedVersion().getClashSeverity().toString().equals( clashSeverity.toString() ) ) {
          return true;
        }
      }


    }


    return false;
  }

  public Project getProject() {
    return project;
  }


  @Override
  public boolean equals( Object object ) {
    boolean result = false;


    if ( object instanceof VersionClash ) {

      VersionClash vC = ( VersionClash ) object;


      if ( vC.getProject().getGroupId().equals( this.getProject().getGroupId() ) && vC.getProject().getArtifactId().equals( this.getProject().getArtifactId() ) ) {
        result = true;
      }


    }

    return result;
  }
}
