package com.cedarsoft.hsrt.zkb.ourplugin.model;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 02.12.13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class InnerVersionClash {

  private final DependencyNodeWrapper usedDependencyNodeWrapper;
  private final DependencyNodeWrapper referredDependencyNodeWrapper;
  private final ClashSeverity clashSeverity;

  public InnerVersionClash( DependencyNodeWrapper usedDependencyNodeWrapper, DependencyNodeWrapper referredDependencyNodeWrapper ) {
    this.usedDependencyNodeWrapper = usedDependencyNodeWrapper;
    this.referredDependencyNodeWrapper = referredDependencyNodeWrapper;

    this.clashSeverity =  this.referredDependencyNodeWrapper.getRelationShipToUsedVersion().getClashSeverity();

  }

  public boolean isClashForSeverityLevel( ClashSeverity clashSeverity ) {
    if ( this.clashSeverity.ordinal() >= clashSeverity.ordinal() ) {
      return true;
    } else {
      return false;
    }
  }


  public DependencyNodeWrapper getUsedDependencyNodeWrapper() {
    return usedDependencyNodeWrapper;
  }

  public DependencyNodeWrapper getReferredDependencyNodeWrapper() {
    return referredDependencyNodeWrapper;
  }

  public ClashSeverity getClashSeverity() {
    return clashSeverity;
  }
}
