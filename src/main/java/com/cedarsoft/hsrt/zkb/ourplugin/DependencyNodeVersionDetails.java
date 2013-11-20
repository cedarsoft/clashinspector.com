package com.cedarsoft.hsrt.zkb.ourplugin;

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

  private ArrayList<VersionWrapper> versions;
  private Version nodeVersion;


  public Version getHighestVersion() {
    // for(Version version : versions)


    ArrayList<Version> list = new ArrayList<Version>();
    for ( VersionWrapper versionWrapper : this.versions ) {
      list.add( versionWrapper.getVersion() );
    }


    return Collections.max( list );
  }

  public Version getLowestVersion() {


    ArrayList<Version> list = new ArrayList<Version>();
    for ( VersionWrapper versionWrapper : this.versions ) {
      list.add( versionWrapper.getVersion() );
    }


    return Collections.min( list );
  }

  public boolean hasVersionClash() {

    if ( this.getAllDifferentVersions().size() > 1 ) {
      return true;
    } else {
      return false;
    }
  }

  public Version getInMavenUsedVersion() {
    //TODO Baum chekcen ob wirklihc erste version von maven verwendet wird


    return Collections.min( this.versions ).getVersion();
  }


  public List<VersionWrapper> getVersions() {
    return Collections.unmodifiableList( versions );
  }

  public List<Version> getAllDifferentVersions() {

    ArrayList<Version> differentVersions = new ArrayList<Version>();

    for ( VersionWrapper versionWrapper : versions ) {
      if ( !differentVersions.contains( versionWrapper.getVersion() ) ) {
        differentVersions.add( versionWrapper.getVersion() );
      }
    }


    return Collections.unmodifiableList( differentVersions );
  }

  public void setVersions( ArrayList<VersionWrapper> versions ) {
    this.versions = versions;
  }

  public ClashType getClashType() {
    //compare nodeVersion with inMavenUsedVersion
    int clashResult = this.nodeVersion.compareTo( this.getInMavenUsedVersion() );

    if ( !this.hasVersionClash() ) {
      return ClashType.NONE;
    }

    if ( clashResult < 0 ) {
      return ClashType.USED_VERSION_HIGHER;
    } else if ( clashResult > 0 ) {
      return ClashType.USED_VERSION_LOWER;
    } else {
      return ClashType.EQUAL;
    }


  }


  public enum ClashType {
    NONE, EQUAL, USED_VERSION_HIGHER, USED_VERSION_LOWER
  }


  public Version getNodeVersion() {
    return nodeVersion;
  }

  public void setNodeVersion( Version nodeVersion ) {
    this.nodeVersion = nodeVersion;
  }
}
