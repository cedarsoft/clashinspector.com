package com.cedarsoft.hsrt.zkb.ourplugin.mojos;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 25.10.13
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractClashMojo extends AbstractMojo {

  @Component()
  private RepositorySystem repoSystem;

  @Parameter(defaultValue = "${repositorySystemSession}")
  private RepositorySystemSession repoSession;

  @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true)
  private List<RemoteRepository> remoteRepos;


  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  @Parameter(alias = "includedScopes")
  private String[] includedScopes;

  @Parameter(alias = "excludedScopes")
  private String[] excludedScopes;

  @Parameter(alias = "includeOptional", defaultValue = "false")
  private boolean includeOptional;

  @Parameter(alias = "clashDetectionLevel", defaultValue = "ALL")
  private ClashDetectionLevel detectionLevel;

  private List<String> includedScopesList = new ArrayList<String>();
  private List<String> excludedScopesList = new ArrayList<String>();

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if ( this.includedScopes == null || this.includedScopes.length == 0 ) {
      includedScopesList.add( "compile" );
    } else {

      includedScopesList.addAll( Arrays.asList( this.includedScopes ) );
    }

    if ( this.excludedScopes == null ) {

    } else {
      excludedScopesList.addAll( Arrays.asList( this.excludedScopes ) );
    }


  }


  public RepositorySystem getRepoSystem() {
    return repoSystem;
  }

  public RepositorySystemSession getRepoSession() {
    return repoSession;
  }

  public List<RemoteRepository> getRemoteRepos() {
    return remoteRepos;
  }

  public MavenProject getProject() {
    return project;
  }

  public boolean isIncludeOptional() {
    return includeOptional;
  }

  public ClashDetectionLevel getClashDetectionLevel() {
    return detectionLevel;
  }

  public List<String> getIncludedScopesList() {
    return includedScopesList;
  }

  public List<String> getExcludedScopesList() {
    return excludedScopesList;
  }

  /**
   * The Clash Detection level is responsible for the detection of Version clashes. If the level is all then
   * there will be a clash if there are two different versions for the same dependency. If the level
   * is crtitical version clashes with higher and lower versions as the used version will be reported.
   * If the level is Fatal only the dependencies with higher version than the used one will be reported.
   */
  public enum ClashDetectionLevel {
    ALL, CRITICAL, FATAL

  }


}


