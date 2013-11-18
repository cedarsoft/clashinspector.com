package com.cedarsoft.hsrt.zkb.ourplugin;

import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.version.Version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 18.11.13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class VersionDetailService {

  private Map<String, ArrayList<Version>> map = new HashMap<String, ArrayList<Version>>();

  public DependencyNodeVersionDetails resolveVersionDetails( DependencyNode dN ) {
    DependencyNodeVersionDetails dependencyNodeVersionDetails = new DependencyNodeVersionDetails();

    ArrayList<Version> versions;
    String key = dN.getArtifact().getGroupId() + ":" + dN.getArtifact().getArtifactId();

    //Wenn der key in map noch nicht drin ist  dann liste erstellen

    if ( map.get( key ) == null ) {  // Kein Dependency Clash aufgetreten
      versions = this.createVersionList();
      versions.add( dN.getVersion() );
      map.put( key,versions );
    } else {   //Dependency Clash  aufgetreten

      versions = map.get( key );

      versions.add( dN.getVersion() );


    }
        dependencyNodeVersionDetails.setVersions( versions );



    //Check weather dependency clash appeared (two same dependencies)
    /*
    if ( ) {
      dependencyNodeVersionDetails.setPreviousDependencyVersion( version );
      //Check
      if ( !version.toString().equals( dN.getArtifact().getVersion().toString() ) ) {
        //Check if previous version is higher?
        if ( version.compareTo( dN.getVersion() ) > 0 ) {
          dependencyNodeVersionDetails.setPreviousVersionHigher( true );
        } else if ( version.compareTo( dN.getVersion() ) < 0 ) {
          dependencyNodeVersionDetails.setPreviousVersionHigher( false );
        } else {
          throw new ClashServiceException( "Versions had to be different, but are equal" );
        }

      }
    } else {
      map.put( key, versions );
      dependencyNodeVersionDetails.setVersionsState( DependencyNodeVersionDetails.VersionsState.NONE );
    }
              */

    return dependencyNodeVersionDetails;
  }

  private ArrayList<Version> createVersionList() {
    return new ArrayList<Version>();
  }

}
