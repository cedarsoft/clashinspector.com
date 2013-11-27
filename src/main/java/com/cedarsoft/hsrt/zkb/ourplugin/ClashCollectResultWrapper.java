package com.cedarsoft.hsrt.zkb.ourplugin;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 20.11.13
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DependencyNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClashCollectResultWrapper {

 private final CollectResult collectResult;

  /**
   *   Map to store all clash dependencyNodes for one group- and artifact-id.
   */
  Map<String,List<DependencyNodeWrapper>> clashMap = new HashMap<String,List<DependencyNodeWrapper>>();

  /**
   *   Map to store a list with the clash dependencies with the key of groupd and artifact id
   */
 private final List<DependencyNodeWrapper>  clashListUsedVersionHigher = new ArrayList<DependencyNodeWrapper>(  )  ;
  private final List<DependencyNodeWrapper>  clashListUsedVersionLower = new ArrayList<DependencyNodeWrapper>(  )  ;
  private final List<DependencyNodeWrapper>  clashListUsedVersionEqual = new ArrayList<DependencyNodeWrapper>(  )   ;

  private DependencyNodeWrapper root;

  public ClashCollectResultWrapper(CollectResult collectResult) {

    this.collectResult = collectResult;

    Map<String, ArrayList<DependencyNodeWrapper>> dependencyMap = new LinkedHashMap<String, ArrayList<DependencyNodeWrapper>>();

     this.root =       new DependencyNodeWrapper(collectResult.getRoot());

    this.buildDependencyNodeWrapperGraph( this.root, dependencyMap, 1 );



   this.initializeClashCollectResultWrapper( this.root,1 );
  }


  public DependencyNodeWrapper getRoot()
  {
    return this.root;
  }



     private void initializeClashCollectResultWrapper(DependencyNodeWrapper dependencyNodeWrapper, int depth)
     {
       for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {



              if(dNW.hasVersionClash())
              {

                ArrayList<DependencyNodeWrapper> dependencyNodeList = (ArrayList) this.clashMap.get( dNW.getGroupId()+":"+dNW.getArtifactId());
                             if(dependencyNodeList==null)
                             {
                               dependencyNodeList = new ArrayList<DependencyNodeWrapper>(  );
                               dependencyNodeList.add( dNW );
                                this.clashMap.put(dNW.getGroupId()+":"+dNW.getArtifactId(),dependencyNodeList);
                             }
                else
                             {
                               dependencyNodeList.add( dNW );
                             }



                if(dNW.getRelationShipToUsedVersion().equals(  DependencyNodeWrapper.RelationShipToUsedVersion.EQUAL))
                {
                          this.clashListUsedVersionEqual.add( dNW );

                }
                else if (dNW.getRelationShipToUsedVersion().equals(  DependencyNodeWrapper.RelationShipToUsedVersion.USED_VERSION_HIGHER))
                {
                  this.clashListUsedVersionHigher.add( dNW );

                }
                else if(dNW.getRelationShipToUsedVersion().equals(  DependencyNodeWrapper.RelationShipToUsedVersion.USED_VERSION_LOWER))
                {
                  this.clashListUsedVersionLower.add( dNW );

                }




              }





         this.initializeClashCollectResultWrapper( dNW,depth+1 );
       }
     }

     private  int dependencyCounter=0;

  //Enrich Dependency Node with versiondetails and parent pathMap   buildWrapperGraph
  private void buildDependencyNodeWrapperGraph( DependencyNodeWrapper dependencyNodeWrapperOld, Map<String, ArrayList<DependencyNodeWrapper>> dependencyMap, int graphDepth ) {

    int graphLevelOrder=0;


    for ( DependencyNode dN : dependencyNodeWrapperOld.getDependencyNode().getChildren()) {



      String key = dN.getArtifact().getGroupId() + ":" + dN.getArtifact().getArtifactId();
      ArrayList<DependencyNodeWrapper> dependencySiblings = dependencyMap.get( key );
      if ( dependencySiblings == null ) {


        dependencySiblings = new ArrayList<DependencyNodeWrapper>(  ) ;


        dependencyMap.put( key, dependencySiblings );

      }

      dependencyCounter = dependencyCounter +1;
      DependencyNodeWrapper dependencyNodeWrapper = new DependencyNodeWrapper( dN,dependencyNodeWrapperOld, dependencySiblings,graphDepth,graphLevelOrder,dependencyCounter);
      dependencySiblings.add(dependencyNodeWrapper );

      graphLevelOrder = graphLevelOrder +1;
      dependencyNodeWrapperOld.addChildren(dependencyNodeWrapper) ;
      this.buildDependencyNodeWrapperGraph( dependencyNodeWrapper, dependencyMap, graphDepth + 1 );
    }


  }

  private ArrayList<VersionWrapper> createVersionList() {
    return new ArrayList<VersionWrapper>();
  }


   public int getNumberOfClashes()
   {
       return this.clashListUsedVersionLower.size()+ this.clashListUsedVersionHigher.size()+ this.clashListUsedVersionEqual.size();
   }

  public int getNumberOfClashes(AbstractClashMojo.ClashDetectionLevel clashDetectionLevel)
  {
    int number = 0;



           for(DependencyNodeWrapper dNW : this.getCompleteClashList())
           {
             if(dNW.hasVersionClash( clashDetectionLevel ))
             {
               number = number+1;
             }
           }

    return number;
  }

  //Critical
  public int getNumberOfClashesWithUsedVersionHigher()
  {
    return this.clashListUsedVersionHigher.size();
  }

  //Fatal
  public int getNumberOfClashesWithUsedVersionLower()
  {
    return this.clashListUsedVersionLower.size();

  }

  public int getNumberOfClashesWithUsedVersionEqual()
  {


    return this.clashListUsedVersionEqual.size();
  }

  public List<DependencyNodeWrapper> getClashListUsedVersionHigher() {
    return clashListUsedVersionHigher;
  }

  public List<DependencyNodeWrapper> getClashListUsedVersionLower() {
    return clashListUsedVersionLower;
  }

  public List<DependencyNodeWrapper> getClashListUsedVersionEqual() {
    return clashListUsedVersionEqual;
  }

  public List<DependencyNodeWrapper> getCompleteClashList() {

    ArrayList<DependencyNodeWrapper> clashList = new ArrayList<DependencyNodeWrapper>(  );
    clashList.addAll(  this.clashListUsedVersionLower)   ;
    clashList.addAll(  this.clashListUsedVersionHigher)   ;
    clashList.addAll(  this.clashListUsedVersionEqual)   ;

    return clashList;
  }

  public List<DependencyNodeWrapper> getClashes(AbstractClashMojo.ClashDetectionLevel clashDetectionLevel)
  {
         ArrayList <DependencyNodeWrapper>  list = new ArrayList <DependencyNodeWrapper>();

    switch ( clashDetectionLevel ) {
      case ALL:
        list.addAll( this.clashListUsedVersionEqual );
        list.addAll( this.clashListUsedVersionLower );
        list.addAll( this.clashListUsedVersionHigher );
        break;
      case CRITICAL:
        list.addAll( this.clashListUsedVersionLower );
        list.addAll( this.clashListUsedVersionHigher );
        break;
      case FATAL:
        list.addAll( this.clashListUsedVersionLower );
        break;
    }
        return list;
  }

  public Map<String, List<DependencyNodeWrapper>> getClashMap() {
    return clashMap;
  }

  public boolean hasVersionClash(AbstractClashMojo.ClashDetectionLevel clashDetectionLevel) {

    //Simple Clash means two different versions
    boolean result = false;

    switch ( clashDetectionLevel ) {
      case ALL:
        if ( this.clashMap.size()>0 ) {
          result = true;
        }
        break;
      case CRITICAL:
        if(this.clashListUsedVersionHigher.size()>0||this.clashListUsedVersionLower.size()>0 )
        {
          result = true;
        }
        break;
      case FATAL:
        if(this.clashListUsedVersionLower.size()>0)
        {
          result = true;
        }
        break;
    }



    return result;
  }


}
