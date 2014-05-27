package com.clashinspector.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 16.05.14
 * Time: 19:01
 * To change this template use File | Settings | File Templates.
 */
public class UserParameterWrapper {

  private java.util.List<String> includedScopes = new ArrayList<String>(  );
 private List<String> excludedScopes = new ArrayList<String>(  );
 private  boolean includeOptional ;

  public UserParameterWrapper( List<String> includedScopes, List<String> excludedScopes, boolean includeOptional ) {
    this.includedScopes.addAll( includedScopes ) ;
    this.excludedScopes.addAll(excludedScopes  );
    this.includeOptional = includeOptional;
  }

  public List<String> getIncludedScopes() {
    return includedScopes;
  }

  public List<String> getExcludedScopes() {
    return excludedScopes;
  }

  public boolean getIncludeOptional() {
    return includeOptional;
  }
}
