package com.cedarsoft.maven.clashinspector.mojos;

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

  @Component
  private RepositorySystem repoSystem;

  @Parameter( defaultValue = "${repositorySystemSession}", readonly = true )
  private RepositorySystemSession repoSession;

  @Parameter( defaultValue = "${project.remoteProjectRepositories}", readonly = true )
  private List<RemoteRepository> remoteRepos;

  @Parameter( defaultValue = "${project}", readonly = true, required = true )
  private MavenProject project;

  //TODO parameterübergabe per console geht nicht (beispiel severity)
  //TODO (how) is it possible to add multiple scopes via console?
  /**
   * Defines the included dependency scopes.
   *
   * @since 0.3
   */
  @Parameter( alias = "includedScopes", defaultValue = "compile")
  private String[] includedScopes;

  /**
   * Defines the excluded dependency scopes.
   *
   * @since 0.3
   */
  @Parameter( alias = "excludedScopes", defaultValue = "null" )
  private String[] excludedScopes;

  /**
   * Defines if optional dependencies will be included.
   *
   * @since 0.3
   */
  @Parameter( alias = "includeOptional", defaultValue = "false", property = "includeOptional")
  private String includeOptional;

  /**
   * <p>Defines which version clashes will be displayed depending on their severity.</p>
   * <p/>
   * <li><b>SAFE</b>: Displays <code>SAFE</code>, <code>UNSAFE</code> and <code>CRITICAL</code> version clashes.</li>
   * <li><b>UNSAFE</b>: Displays <code>UNSAFE</code> and <code>CRITICAL</code> version clashes.</li>
   * <li><b>CRITICAL</b>: Displays only <code>CRITICAL</code> version clashes.</li>
   *
   * @since 0.3
   */
  @Parameter( alias = "severity", defaultValue = "UNSAFE", property = "severity")
  private String severity;

  private final List<String> includedScopesList = new ArrayList<String>();
  private final List<String> excludedScopesList = new ArrayList<String>();

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
    return Boolean.valueOf(includeOptional);
  }

  public ClashSeverity getSeverity() {
    return ClashSeverity.valueOf(severity);
  }

  public List<String> getIncludedScopesList() {
    return includedScopesList;
  }

  public List<String> getExcludedScopesList() {
    return excludedScopesList;
  }


  public String getStartParameter()
  {
    String includedOptionalStr = "includedOptional = " + this.isIncludeOptional();
    String severityStr = "severity= " + this.getSeverity();
    String includedScopesStr="includedScopes = ";

    for(String s: this.getIncludedScopesList())
    {
      includedScopesStr = includedScopesStr + " "+ s;
    }

    String excludedScopesStr="excludedScopes=";

    for(String s: this.getExcludedScopesList())
    {
      excludedScopesStr = excludedScopesStr + " "+ s;
    }

    return severityStr + includedScopesStr+ excludedScopesStr+ includedOptionalStr;

  }


}


