package com.clashinspector.rest;

import com.clashinspector.model.ClashCollectResultWrapper;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 16.05.14
 * Time: 18:51
 * To change this template use File | Settings | File Templates.
 */
//Für jeden Tab eine neue Session
public class ViewScopeObject {

  private Date lastUsed;
  private int viewId;

  private ClashCollectResultWrapper clashCollectResultWrapper;
   private UserParameterWrapper userParameterWrapper;


  public ViewScopeObject( Date lastUsed, int viewId, ClashCollectResultWrapper clashCollectResultWrapper, UserParameterWrapper userParameterWrapper ) {
    this.lastUsed = lastUsed;
    this.viewId = viewId;
    this.clashCollectResultWrapper = clashCollectResultWrapper;
    this.userParameterWrapper = userParameterWrapper;
  }

  public UserParameterWrapper getUserParameterWrapper() {
    return userParameterWrapper;
  }

  public void setUserParameterWrapper( UserParameterWrapper userParameterWrapper ) {
    this.userParameterWrapper = userParameterWrapper;
  }

  public Date getLastUsed() {
    return lastUsed;
  }

  public void setLastUsed( Date lastUsed ) {
    this.lastUsed = lastUsed;
  }

  public int getViewId() {
    return viewId;
  }

  public void setViewId( int viewId ) {
    this.viewId = viewId;
  }

  public ClashCollectResultWrapper getClashCollectResultWrapper() {
    return clashCollectResultWrapper;
  }

  public void setClashCollectResultWrapper( ClashCollectResultWrapper clashCollectResultWrapper ) {
    this.clashCollectResultWrapper = clashCollectResultWrapper;
  }


}
