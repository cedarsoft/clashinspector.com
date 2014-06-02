
 //Vllt doch besser objekte ohne rest einzuschreiben ??
function DependencyNodeObject(dependencyNodeWrapper)
{


    this.dependencyNodeWrapper = dependencyNodeWrapper;



}


function UserSettingsWrapper(includedScopes,excludedScopes,includeOptional,clashSeverity)
{

this.includedScopes = includedScopes;
this.excludedScopes = excludedScopes;
this.includeOptional = includeOptional;
this.clashSeverity = clashSeverity;
               this.convertToParameterString = convertToParameterString;

     function convertToParameterString(includeClashSeverity)
      {


        var parameterString="";

                  if(this.includeOptional!=undefined)
                  {
                         parameterString =parameterString+ "includeOptional="+this.includeOptional+"";
                  }

                 if(this.clashSeverity!=undefined)
                                  {
                                        if(includeClashSeverity==true)
                                                                  {
                                                                      parameterString = parameterString +"&clashSeverity="+this.clashSeverity;
                                                                  }
                                  }

                                   if(this.includedScopes!=undefined)
                                                    {
                                                            for(var i=0;i<this.includedScopes.length;i++){
                                                                                       parameterString = parameterString +"&includedScope="+this.includedScopes[i];
                                                                                 }
                                                    }

                                                     if(this.excludedScopes!=undefined)
                                                                      {
                                                                                for(var i=0;i<this.excludedScopes.length;i++){
                                                                                                         parameterString = parameterString +"&excludedScope="+this.excludedScopes[i];
                                                                                                   }

                                                                      }








                       console.log("Convertion completed with: " + parameterString)    ;

                      return parameterString;
      }


}




       var dependencyNodeObjectList = new Array();
       var outerVersionClashList= new Array();
        var userSettingsWrapper = new UserSettingsWrapper();






$( document ).ready(function() {





$(".javascriptWarning").hide();

$("#main").addClass("loading");

 getTree();



});


function getTree()
{
console.log('[' + new Date().toUTCString() + '] ' +"getTree started");
     doGet("http://localhost:8090/dependencies",drawTree,"",getList);
     console.log('[' + new Date().toUTCString() + '] ' +"getTree finished");
}

function getList()
{
console.log('[' + new Date().toUTCString() + '] ' +"getList started");
    doGet("http://localhost:8090/dependencies/outerVersionClashes",drawList);
    console.log('[' + new Date().toUTCString() + '] ' +"getList finished");

}



   function drawList(result)
   {                         console.log('[' + new Date().toUTCString() + '] ' +"drawList started");
                   var listHtml = '<ul>';

             for(var i=0;i<result.length;i++){

                             var id = result[i].project.groupId+":"+result[i].project.artifactId;

                                      outerVersionClashList[id]=result[i];

                            listHtml = listHtml + buildClashListEntry(outerVersionClashList[id]);
                            }

                            $(".clashListContainer").html(listHtml+"</ul>") ;




   }

   function buildClashListEntry(outerVersionClash)
   {
   var idList = "";
   for(var i=0;i<outerVersionClash.innerVersionClashes.length;i++){
       idList = idList + "'"+outerVersionClash.innerVersionClashes[i].referredDependencyNodeWrapperId+"'";

       if(i<outerVersionClash.innerVersionClashes.length-1)
       {
         idList = idList + ",";
       }
   }


             console.log(idList);
          var versionsLink = '<span  onclick="highlightDependencyByIds(['+idList+'],&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true);"><span>'+outerVersionClash.project.groupId+"</span>:<span>"+outerVersionClash.project.artifactId+'</span></span>';



           var html = "<li>"+versionsLink+"<ul>";
                                 console.log("outerVersionClash.innerVersionClashes.length " + outerVersionClash.innerVersionClashes.length);
                              for(var i=0;i<outerVersionClash.innerVersionClashes.length;i++){
                              var innerVersionClash =  outerVersionClash.innerVersionClashes[i];
                               var dependencyNodeWrapper = dependencyNodeObjectList[innerVersionClash.referredDependencyNodeWrapperId].dependencyNodeWrapper;

                                var clashSeverityClass="";
                                if(innerVersionClash.clashSeverity=="UNSAFE")
                                                                         {
                                                                          clashSeverityClass = "clashSeverityUnsafe" ;
                                                                         highlightDependency(dependencyNodeWrapper,"clashSeverityUnsafe",false,false);
                                                                         addArrowClassToParentWrappers(dependencyNodeWrapper,"clashSeverityUnsafe");
                                                                         }
                                                                         if(innerVersionClash.clashSeverity=="CRITICAL")
                                                                                                                  {
                                                                                                                  highlightDependency(dependencyNodeWrapper,"clashSeverityCritical",false,false);
                                                                                                                    clashSeverityClass = "clashSeverityCritical" ;
                                                                                                                    addArrowClassToParentWrappers(dependencyNodeWrapper,"clashSeverityCritical");
                                                                                                                  }


                                                var versionLink = '<span  onclick="highlightDependencyById(&quot;'+dependencyNodeWrapper.id+'&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true,true);">'+dependencyNodeObjectList[innerVersionClash.referredDependencyNodeWrapperId].dependencyNodeWrapper.version+'</span>';



                                               html=html + "<li class='"+clashSeverityClass+"'>" + versionLink + "</li>";

                              console.log("innerVersionClash " + innerVersionClash.referredDependencyNodeWrapperId);

                              }

                           html = html +"</ul></li>";

   return html;


   }


   function buildList(result)
   {

   }

 function drawTree(result)
 {




 $("#analyzedDep").html("<h2>"+ result.groupId+":"+result.artifactId+":"+result.version + "</h2>");

 var html = "<ul id='dependencyTree'>" +   buildTree(result);

 html = html + "</ul>";




 $("#leftMain").html( html);
        console.log('[' + new Date().toUTCString() + '] ' +"drawTree finished");

        $("#main").removeClass("loading");
 }

function buildTree(data)
{


             var dep = new DependencyNodeObject(data);
                      dependencyNodeObjectList[dep.dependencyNodeWrapper.id] = dep;


                       var html=buildGuiDependency(dep);

      if(data.children.length >0)
      {
      var display = "display:block;";
          if(dep.dependencyNodeWrapper.graphDepth>0)
          {
                display="display:none;"
          }

         html = html +    '<ul class="depNodeUl" style="'+display+'">' ;

               for(var i=0;i<data.children.length;i++){



                 html = html + buildTree(data.children[i]);
               }


            html = html + '</ul>' ;
            html = html + ' <div class="clearing"></div></li>';
       }
       else
       {
       html = html +' <div class="clearing"></div></li>' ;
       }


         return html;


}

function buildGuiDependency(dependencyNodeObject)
    {


        if(dependencyNodeObject.dependencyNodeWrapper.repository == "repo.maven.apache.org" )
                                {
                                      var mavenCentralHref =  "http://search.maven.org/#artifactdetails|"+dependencyNodeObject.dependencyNodeWrapper.groupId+"|"+dependencyNodeObject.dependencyNodeWrapper.artifactId+"|"+dependencyNodeObject.dependencyNodeWrapper.version+"|"+dependencyNodeObject.dependencyNodeWrapper.extension;
                                      var  mavenCentralLink   = '<a href="'+mavenCentralHref+'" target="_blank"> maven-central </a>';

                                }

                                  var usedVersionLink = '<span class="usedVersionLink" onclick="highlightDependencyById(&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.dependencyNodeWrapperWithUsedVersionId+'&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true,true);">'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'</span>';
                                  var highestVersionLink = '<span class="highestVersionLink" onclick="searchAndHighlightDependencyByCoordinates(&quot;'+dependencyNodeObject.dependencyNodeWrapper.groupId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.highestVersion+'&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true);">'+dependencyNodeObject.dependencyNodeWrapper.project.highestVersion+'</span>';
                                  var lowestVersionLink = '<span class="lowestVersionLink" onclick="searchAndHighlightDependencyByCoordinates(&quot;'+dependencyNodeObject.dependencyNodeWrapper.groupId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.lowestVersion+'&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true);">'+dependencyNodeObject.dependencyNodeWrapper.project.lowestVersion+'</span>';




                               //searchviaID   var usedVersionLink = '<span class="usedVersionLink" onclick="searchAndHighlightDependency(&quot;'+dependencyNodeObject.dependencyNodeWrapper.groupId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'&quot;,&quot;highlightSearch&quot;,&quot;true&quot;);">'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'</span>';
         var arrowClass= "";
         if(dependencyNodeObject.dependencyNodeWrapper.children.length >0)
         {
             arrowClass="clashSeveritySafe";
         }



    return '<li class="depNodeLi"  ><div class="depNodeWrapper '+arrowClass+'" id="dNW'+dependencyNodeObject.dependencyNodeWrapper.id+'"><div id="'+dependencyNodeObject.dependencyNodeWrapper.id+'" class="depNode">\
                                             <span class="groupId" title="groupId">'+dependencyNodeObject.dependencyNodeWrapper.groupId+'</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">'+dependencyNodeObject.dependencyNodeWrapper.version+'</span> \
                                              <div class="details"><div title="from maven used version of this project"><span >used version:  </span>'+usedVersionLink+'</div> <hr><div title="highest version of this project included in the analyzed dependency"><span >highest version:  </span>'+highestVersionLink+'</div><hr><div title="lowest version of this project included in the analyzed dependency"><span >lowest version: </span>'+lowestVersionLink+'</div><hr><div title="number of direct dependencies"><span >number of dep: '+dependencyNodeObject.dependencyNodeWrapper.children.length+'</span></div></div> <div class="depMenu"><a class="detailsButton">details</a> | '+mavenCentralLink+' </div> </div>   </div>   ' ;
    }




        var delayTime = 200, clickNumber = 0, timer = null;

        $(document).on('click', '.depNodeWrapper', function(){

    var thisVar = $(this);

          clickNumber++;

        if(clickNumber === 1) {

            timer = setTimeout(function() {

               thisVar.next("ul").toggle();

                clickNumber = 0;

            }, delayTime);

        } else {

            clearTimeout(timer);
            thisVar.next("ul").show();
            thisVar.next("ul").find("ul").show();
            clickNumber = 0;
        }



        });



$(document).on('click', '#searchButton', function(){

                          var result =  searchAndHighlightDependencyByCoordinates($('#groupIdInput').val(),$('#artifactIdInput').val(),$('#versionInput').val(),'highlightSearch','highlightSearch',true);
                               if(Object.keys(result).length>0)
                                                                                         {
                                                                                             $("#searchButton").html("results <span>("+Object.keys(result).length+")</span>");
                                                                                         }
                                                                                          else
                                                                                                                                                    {
                                                                                                                                                        $("#searchButton").html("no results");

                                                                                                                                                    }

                });




$(document).on('input', '.searchInput', function(){
                                  var result =  searchAndHighlightDependencyByCoordinates($('#groupIdInput').val(),$('#artifactIdInput').val(),$('#versionInput').val(),'highlightSearch','highlightSearch',true);

                                                           if(Object.keys(result).length>0 )
                                                           {
                                                               $("#searchButton").html("results <span>("+Object.keys(result).length+")</span>");
                                                           }
                                                           else
                                                           {
                                                               $("#searchButton").html("no results");

                                                           }


                });

 $(document).on('click', '.openSearchButton', function(){
                            $("#searchContainer").toggle();

                });

 $(document).on('click', '.openSettingsButton', function(){
                            $("#settingsFilterContainer").toggle();

                });

                 $(document).on('click', '.openClashListButton', function(){
                                            $(".clashListContainer").toggle();

                                });

        $(document).on('dbclick', '.depNode', function(){

                     e.preventDefault();


                });

         $(document).on('mouseenter', '.depNode', function(){


                               $(this).children(".depMenu").show();


                });

                  $(document).on('mouseleave', '.depNode', function(){

                                          $(this).children(".depMenu").hide();


                                });




                                 $(document).on('click', '.easySelectBox li', function(){

                                                                        $(this).toggleClass("selected");


                                                              });

                                                               $(document).on('click', '.easyCheckBox', function(){

                                                                        $(this).toggleClass("selected");


                                                              });

                            $(document).on('click', '.detailsButton', function(){

                                                 $(this).parent(".depMenu").prev(".details").toggle();

                                                e.preventDefault();



                                           });

                                        $(document).on('click', '.details', function(){


                                                                                        e.stopPropagation();



                                                                                   });



                                function doGet(url,callbackFunction,parameters,syncCallFunction)
                                {

                                            var parameter = userSettingsWrapper.convertToParameterString();
                                                 alert (parameter);

                                     $.ajax({
                                                         type: "GET",
                                                         url: url,
                                                         data:  parameter,
                                                         async:true,
                                                         contentType: "application/json; charset=utf-8",
                                                         dataType: "jsonp",
                                                         success: function(responseObject) {

                                                                     processResponseObject(responseObject, callbackFunction,syncCallFunction);
                                                         },
                                                         error: function (XMLHttpRequest, textStatus, errorThrown) {
                                                                 alert('error');
                                                         },
                                                         beforeSend: function (XMLHttpRequest) {
                                             						//show loading
                                                         },
                                                         complete: function (XMLHttpRequest, textStatus) {
                                             					//hide loading
                                                         }
                                             		});

                                }


                                //Bei jedem Get ausführen und viewId setzen etc.

                                 function processResponseObject(responseObject, callbackFunction,syncCallFunction)
                                        {

                                                             userSettingsWrapper.includedScopes = responseObject.userParameterWrapper.includedScopes;
                                                             userSettingsWrapper.excludedScopes = responseObject.userParameterWrapper.excludedScopes;
                                                             userSettingsWrapper.includeOptional = responseObject.userParameterWrapper.includeOptional;
                                                             userSettingsWrapper.clashSeverity = responseObject.userParameterWrapper.clashSeverity;



                                                           console.log("userSettingsWrapper " + userSettingsWrapper.includedScopes);

                                                        callbackFunction.call( this, responseObject.result );

                                                        if(syncCallFunction!=undefined && syncCallFunction !="")
                                                        {
                                                            syncCallFunction.call();
                                                        }


                                        }







                               function searchForDependencyById(id)
                                                                       {
                                                  return   dependencyNodeObjectList[id].dependencyNodeWrapper;
                                                                       }



                               function searchForDependenciesByCoordinates(groupId,artifactId,version)
                                        {
                                                          //  alert("dependencyNodeObjectList length: " + Object.keys(dependencyNodeObjectList).length)
                                                          var result =  jQuery.extend({}, dependencyNodeObjectList);

                                            if(groupId != undefined && groupId!="" )
                                            {


                                                            for(var index in result) {

                                                                    var compValue1;
                                                                     var compValue2 = groupId;
                                                                 if(groupId.slice(-1)=="*")
                                                                 {
                                                                      compValue2 = groupId.substring(0,groupId.length-1);
                                                                      compValue1 =result[index].dependencyNodeWrapper.groupId.substring(0,compValue2.length);

                                                                 }
                                                                 else
                                                                 {
                                                                    compValue1 =result[index].dependencyNodeWrapper.groupId;
                                                                 }


                                                              if(compValue1!=compValue2)
                                                               {

                                                                  delete result[index];
                                                               }


                                                                                                                                    }






                                            }
                                            if(artifactId != undefined  && artifactId!="")
                                            {

                                                   for(var index in result) {



                                                       var compValue1;
                                                                                                                        var compValue2 = artifactId;
                                                                                                                    if(artifactId.slice(-1)=="*")
                                                                                                                    {
                                                                                                                         compValue2 = artifactId.substring(0,artifactId.length-1);
                                                                                                                         compValue1 =result[index].dependencyNodeWrapper.artifactId.substring(0,compValue2.length);

                                                                                                                    }
                                                                                                                    else
                                                                                                                    {
                                                                                                                       compValue1 =result[index].dependencyNodeWrapper.artifactId;
                                                                                                                    }


                                           if(compValue1!=compValue2)
                                                                                                        {

                                                                                                           delete result[index];
                                                                                                        }

                                                                                                                                                                                      }


                                            }
                                             if(version != undefined && version!="")
                                            {

                                                 for(var index in result) {

     var compValue1;
                                                                     var compValue2 = version;
                                                                 if(version.slice(-1)=="*")
                                                                 {
                                                                      compValue2 = version.substring(0,version.length-1);
                                                                      compValue1 =result[index].dependencyNodeWrapper.version.substring(0,compValue2.length);

                                                                 }
                                                                 else
                                                                 {
                                                                    compValue1 =result[index].dependencyNodeWrapper.version;
                                                                 }

                                                          if(compValue1!=compValue2)
                                                                                                                       {

                                                                                                                          delete result[index];
                                                                                                                       }
                                                                                                                                                                                    }

                                            }
                                                if((groupId == undefined || groupId=="") && (artifactId == undefined || artifactId=="") && (version == undefined || version==""))
                                                {

                                                      result = new Array();
                                                }




                                          return result;


                                        }




 function highlightDependency(dependencyNodeWrapper,highlightClazz,openPath,jumpTo)
                                        {



                                            $("#"+dependencyNodeWrapper.id).addClass(highlightClazz);



                                           if(openPath == true)
                                                                                       {
                                                                                                       $("#"+dependencyNodeWrapper.id).parents("ul").show();

                                                                                       }

                                                                                         if(jumpTo == true)
                                                                                                                                  {
                                                                                                                                                  location.href = "#"+dependencyNodeWrapper.id;

                                                                                                                                  }

                                        }


function highlightDependencyById(id,highlightClazz,highlightClazzToDelete,openPath,jumpTo)
                                        {

                                            $("."+highlightClazzToDelete).removeClass(highlightClazzToDelete);

                                            highlightDependency(dependencyNodeObjectList[id].dependencyNodeWrapper,highlightClazz,openPath,jumpTo);

                                        }
                                        function highlightDependencyByIds(ids,highlightClazz,highlightClazzToDelete,openPath)
                                        {

                                            $("."+highlightClazzToDelete).removeClass(highlightClazzToDelete);


                                                 for(var i=0;i<ids.length;i++){


                                                        highlightDependency(dependencyNodeObjectList[ids[i]].dependencyNodeWrapper,highlightClazz,openPath,false);
                                                    }


                                        }

                                         function highlightDependencies(dependencyNodeWrapperList,highlightClazz,highlightClazzToDelete,openPath)
                                              {
                                                 $("."+highlightClazzToDelete).removeClass(highlightClazzToDelete);
                                                 for(var index in dependencyNodeWrapperList) {

                                                      highlightDependency(dependencyNodeWrapperList[index].dependencyNodeWrapper,highlightClazz,openPath);


                                                 }
                                              }




                       function addArrowClassToParentWrappers(dependencyNodeWrapper,clazz)
                                        {
                                       // alert(dependencyNodeWrapper.groupId + " " +dependencyNodeWrapper.artifactId )   ;

                                            // alert("jof " +dependencyNodeObjectList[dependencyNodeWrapper.parentId].dependencyNodeWrapper.id )   ;
                                               $("#"+dependencyNodeWrapper.id).parents(".depNodeUl").prev(".depNodeWrapper").addClass(clazz);
                                               // addArrowClassToParents(dependencyNodeObjectList(dependencyNodeWrapper.parentId).dependencyNodeWrapper);
                                                ////    alert("jo" )   ;





                                        }


                                                 function searchAndHighlightDependencyByCoordinates(groupId,artifactId,version,highlightClazz,highlightClazzToDelete,openPath)
                                                                                {



                                                                                     var result =  searchForDependenciesByCoordinates(groupId,artifactId,version) ;

                                                                                     highlightDependencies(result,highlightClazz,highlightClazzToDelete,openPath);

                                                                                     return result;
                                                                                }