package com.cedarsoft.hsrt.zkb.ourplugin;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import org.eclipse.aether.version.Version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 18.11.13
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
public class DependencyNodeVersionDetails {
  //DependencyNodeVersionDetails

  private ArrayList<VersionWrapper> versionWrapperList;
  private Version nodeVersion;


  public Version getHighestVersion() {
    // for(Version version : versionWrapperList)


    ArrayList<Version> list = new ArrayList<Version>();
    for ( VersionWrapper versionWrapper : this.versionWrapperList ) {
      list.add( versionWrapper.getVersion() );
    }


    return Collections.max( list );
  }

  public Version getLowestVersion() {


    ArrayList<Version> list = new ArrayList<Version>();
    for ( VersionWrapper versionWrapper : this.versionWrapperList ) {
      list.add( versionWrapper.getVersion() );
    }


    return Collections.min( list );
  }

  /**
   * Returns true or false if there is a version clash for the specified clashDetectionLevel
   * @param clashDetectionLevel
   * @return
   */
  public boolean hasVersionClash(AbstractClashMojo.ClashDetectionLevel clashDetectionLevel) {

    //Simple Clash means two different versions
         boolean result = false;

    switch ( clashDetectionLevel ) {
      case ALL:
        if ( this.hasVersionClash() == true ) {
          result = true;
        }
        break;
      case CRITICAL:
          if(this.getRelationShipToUsedVersion().equals( RelationShipToUsedVersion.USED_VERSION_LOWER )||this.getRelationShipToUsedVersion().equals( RelationShipToUsedVersion.USED_VERSION_HIGHER ))
          {
            result = true;
          }
        break;
      case FATAL:
        if(this.getRelationShipToUsedVersion().equals( RelationShipToUsedVersion.USED_VERSION_LOWER ))
        {
          result = true;
        }
        break;
    }



    return result;
  }

  /**
   * Detects if a version clash exists (Version Clash = two equal artifacts with different versions)
   * @return
   */
  public boolean hasVersionClash() {


        if ( this.getAllDifferentVersions().size() > 1 ) {
          return true;
        }
    else
        {
          return false;
        }


  }

  public Version getInMavenUsedVersion() {
    //TODO Baum chekcen ob wirklihc erste version von maven verwendet wird


    return Collections.min( this.versionWrapperList ).getVersion();
  }


  public List<VersionWrapper> getVersionWrapperList() {
    return Collections.unmodifiableList( versionWrapperList );
  }

  public List<Version> getAllDifferentVersions() {

    ArrayList<Version> differentVersions = new ArrayList<Version>();

    for ( VersionWrapper versionWrapper : versionWrapperList ) {
      if ( !differentVersions.contains( versionWrapper.getVersion() ) ) {
        differentVersions.add( versionWrapper.getVersion() );
      }
    }


    return Collections.unmodifiableList( differentVersions );
  }

  public void setVersionWrapperList( ArrayList<VersionWrapper> versionWrapperList ) {
    this.versionWrapperList = versionWrapperList;
  }

  public RelationShipToUsedVersion getRelationShipToUsedVersion() {
    //compare nodeVersion with inMavenUsedVersion
    int clashResult = this.nodeVersion.compareTo( this.getInMavenUsedVersion() );


    if ( clashResult < 0 ) {
      return RelationShipToUsedVersion.USED_VERSION_HIGHER;
    } else if ( clashResult > 0 ) {
      return RelationShipToUsedVersion.USED_VERSION_LOWER;
    } else {
      return RelationShipToUsedVersion.EQUAL;
    }


  }


  public enum RelationShipToUsedVersion {
    EQUAL, USED_VERSION_HIGHER, USED_VERSION_LOWER
  }


  public Version getNodeVersion() {
    return nodeVersion;
  }

  public void setNodeVersion( Version nodeVersion ) {
    this.nodeVersion = nodeVersion;
  }
}
