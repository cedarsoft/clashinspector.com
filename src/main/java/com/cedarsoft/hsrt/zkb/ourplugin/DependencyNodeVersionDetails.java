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

  private ArrayList<Version> versions;


  public Version getHighestVersion() {
    // for(Version version : versions)
    Version version = Collections.max( versions );


    return version;
  }

  public Version getLowestVersion() {
    Version version = Collections.min( versions );


    return version;
  }

  public boolean hasVersionClash() {
    if ( this.versions.size() > 1 ) {
      return true;
    } else {
      return false;
    }
  }

  public Version getInMavenUsedVersion() {
    //TODO Baum chekcen ob wirklihc erste version von maven verwendet wird
    return versions.get( 0 );
  }


  public List<Version> getVersions() {
    return Collections.unmodifiableList( versions );
  }

  public void setVersions( ArrayList<Version> versions ) {
    this.versions = versions;
  }

  public ClashType getClashType() {
    //compare last entry with used version benutzte version mit leztter version vergelcihen
    int clashResult = versions.get( 0 ).compareTo( versions.get( versions.size() - 1 ) );

    if ( !this.hasVersionClash() ) {
      return ClashType.NONE;
    }

    if ( clashResult < 0 ) {
      return ClashType.USED_VERSION_LOWER;
    } else if ( clashResult > 0 ) {
      return ClashType.USED_VERSION_HIGHER;
    } else {
      return ClashType.EQUAL;
    }


  }


  public enum ClashType {
    NONE, EQUAL, USED_VERSION_HIGHER, USED_VERSION_LOWER
  }


}
