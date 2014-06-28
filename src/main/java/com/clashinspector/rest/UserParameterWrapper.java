package com.clashinspector.rest;

import com.clashinspector.mojos.ClashSeverity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 16.05.14
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class UserParameterWrapper {

  private java.util.List<String> includedScopes;
  private List<String> excludedScopes;
  private Boolean includeOptional;
  private ClashSeverity clashSeverity;

  public UserParameterWrapper( List<String> includedScopes, List<String> excludedScopes, Boolean includeOptional, ClashSeverity clashSeverity ) {
    this.includedScopes = includedScopes;
    this.excludedScopes = excludedScopes;
    this.includeOptional = includeOptional;
    this.clashSeverity = clashSeverity;
  }

  public ClashSeverity getClashSeverity() {
    return clashSeverity;
  }

  public List<String> getIncludedScopes() {
    return includedScopes;
  }

  public List<String> getExcludedScopes() {
    return excludedScopes;
  }

  public Boolean getIncludeOptional() {
    return includeOptional;
  }
}
