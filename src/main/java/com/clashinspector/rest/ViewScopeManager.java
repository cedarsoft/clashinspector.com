package com.clashinspector.rest;

import com.clashinspector.model.ClashCollectResultWrapper;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 16.05.14
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */
public class ViewScopeManager {

  private static Map<Integer,ViewScopeObject> viewScopeList = new HashMap<Integer,ViewScopeObject>();
  private static int viewCounter = 0;
  private static Artifact mainArtifact;
  private static RepositorySystem repositorySystem;
  private static RepositorySystemSession repositorySystemSession;
  private static UserParameterWrapper startParameter;

  public static void init(Artifact artifact, RepositorySystem repositorySystem1,RepositorySystemSession repositorySystemSession1, UserParameterWrapper userParameterWrapper)
  {
          startParameter = userParameterWrapper;
    mainArtifact = artifact;
    repositorySystem = repositorySystem1;
    repositorySystemSession = repositorySystemSession1;
  }


  public void removeUnusedViewScopeObjects()
  {
    for(Map.Entry<Integer,ViewScopeObject> entry : viewScopeList.entrySet() )
    {
      Date now= new Date();

      if (now.getTime() - entry.getValue().getLastUsed().getTime() >=60000 /*15*60*1000*/)
      {

        System.out.println("ViewScope with id " + entry.getKey()+ " deleted.");
        viewScopeList.remove( entry.getKey() ) ;

      }

    }
  }

  //Returns a viewScopedObject if it is not already build it will be build with the information of the startUserParameter
  public ViewScopeObject getViewScopeObject(int viewId, UserParameterWrapper userParameterWrapper)
  {
       System.out.println("jo1");
    removeUnusedViewScopeObjects();
    System.out.println("jo2");
    ViewScopeObject viewScopeObject =  viewScopeList.get( viewId );
    System.out.println("jo3");
    if(viewScopeObject==null)
    {
      System.out.println("jo3");
      com.clashinspector.DependencyService dependencyService = new com.clashinspector.DependencyService();
      System.out.println("jo3");
      if(userParameterWrapper==null|| (userParameterWrapper.getExcludedScopes().size()==0 && userParameterWrapper.getIncludedScopes().size()==0))
      {
        System.out.println("joooooooo");
        userParameterWrapper = startParameter;
      }
      System.out.println("neuer result wrapper erstellt");
      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( mainArtifact, repositorySystemSession, repositorySystem, userParameterWrapper.getIncludedScopes(), userParameterWrapper.getExcludedScopes(),userParameterWrapper.getIncludeOptional() ) );

      System.out.println("jo3");
      viewCounter = viewCounter +1;
      viewScopeObject = new ViewScopeObject(new Date(), viewCounter, clashCollectResultWrapper,userParameterWrapper );

      viewScopeObject.setViewId( viewCounter );

      System.out.println("jo3 added object with id: " + viewScopeObject.getViewId());
      viewScopeList.put( viewScopeObject.getViewId(),viewScopeObject )  ;
    }
    else
    {
    viewScopeObject.setLastUsed( new Date() );
    }

    return  viewScopeObject;

  }





}
