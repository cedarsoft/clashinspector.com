package com.cedarsoft.hsrt.zkb.ourplugin;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 20.11.13
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */

import org.eclipse.aether.version.Version;

public class VersionWrapper implements Comparable<VersionWrapper> {


  private final Version version;
  private final int graphDepth;

  public VersionWrapper( Version version, int grapDepth ) {
    this.version = version;
    this.graphDepth = grapDepth;
  }

  public int getGraphDepth() {
    return graphDepth;
  }


  public Version getVersion() {
    return version;
  }


  @Override
  public int compareTo( VersionWrapper o ) {

    if ( this.getGraphDepth() < o.getGraphDepth() ) {
      return -1;
    } else if ( this.getGraphDepth() > o.getGraphDepth() ) {
      return 1;
    } else {
      return 0;
    }

  }
}
