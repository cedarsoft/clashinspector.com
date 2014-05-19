
 //Vllt doch besser objekte ohne rest einzuschreiben ??
function DependencyNodeObject(dependencyNodeWrapper)
{


    this.dependencyNodeWrapper = dependencyNodeWrapper;



}
          //Eigenes objekt erstellen mit verweis auf guielement damit ausblenden etc.


       var dependencyNodeObjectList = new Array();
        var viewId =0;






$( document ).ready(function() {


$(".javascriptWarning").hide();

doGet("http://localhost:8080/dependencies",drawTree);




});






 function drawTree(result)
 {




 $("#analyzedDep").html("<b>"+ result.groupId+":"+result.artifactId+":"+result.version + "</b>");

 var html = "<ul id='dependencyTree'>" +   buildTree(result);

 html = html + "</ul>";




 $("#leftMain").html( html);

 }

function buildTree(data)
{        //console.log("jo2" + html)


              var id =   "d"+data.graphDepth+"r"+data.graphLevelOrderRelative+"a"+data.graphLevelOrderAbsolute;




             var dep = new DependencyNodeObject(data);
                      dependencyNodeObjectList[id] = dep;


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

                                  var usedVersionLink = '<span class="usedVersionLink" onclick="highlightDependencyById(&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.dependencyNodeWrapperWithUsedVersionId+'&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true);">'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'</span>';
                                  var highestVersionLink = '<span class="highestVersionLink" onclick="searchAndHighlightDependencyByCoordinates(&quot;'+dependencyNodeObject.dependencyNodeWrapper.groupId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.highestVersion+'&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true);">'+dependencyNodeObject.dependencyNodeWrapper.project.highestVersion+'</span>';
                                  var lowestVersionLink = '<span class="lowestVersionLink" onclick="searchAndHighlightDependencyByCoordinates(&quot;'+dependencyNodeObject.dependencyNodeWrapper.groupId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.lowestVersion+'&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true);">'+dependencyNodeObject.dependencyNodeWrapper.project.lowestVersion+'</span>';




                               //searchviaID   var usedVersionLink = '<span class="usedVersionLink" onclick="searchAndHighlightDependency(&quot;'+dependencyNodeObject.dependencyNodeWrapper.groupId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'&quot;,&quot;highlightSearch&quot;,&quot;true&quot;);">'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'</span>';


    return '<li class="depNodeLi"  ><div id="'+dependencyNodeObject.dependencyNodeWrapper.id+'" class="depNode">\
                                             <span class="groupId" title="groupId">'+dependencyNodeObject.dependencyNodeWrapper.groupId+'</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">'+dependencyNodeObject.dependencyNodeWrapper.version+'</span> \
                                              <div class="details"><div title="from maven used version of this project"><span >used version:  </span>'+usedVersionLink+'</div> <hr><div title="highest version of this project included in the analyzed dependency"><span >highest version:  </span>'+highestVersionLink+'</div><hr><div title="lowest version of this project included in the analyzed dependency"><span >lowest version: </span>'+lowestVersionLink+'</div><hr><div title="number of direct dependencies"><span >number of dep: '+dependencyNodeObject.dependencyNodeWrapper.children.length+'</span></div></div> <div class="depMenu"><a class="detailsButton">details</a> | '+mavenCentralLink+' </div> </div>    ' ;
    }




        var delayTime = 200, clickNumber = 0, timer = null;

        $(document).on('click', '.depNode', function(){

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

                                                           if(Object.keys(result).length>0)
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

        $(document).on('dbclick', '.depNode', function(){

                     e.preventDefault();


                });

         $(document).on('mouseenter', '.depNode', function(){


                               $(this).children(".depMenu").show();


                });

                  $(document).on('mouseleave', '.depNode', function(){

                                          $(this).children(".depMenu").hide();


                                });






                            $(document).on('click', '.detailsButton', function(){

                                                 $(this).parent(".depMenu").prev(".details").toggle();

                                                e.preventDefault();



                                           });

                                        $(document).on('click', '.details', function(){


                                                                                        e.stopPropagation();



                                                                                   });



                                function doGet(url,callbackFunction)
                                {

                                            var parameter = "viewId="+viewId;
                                            alert(parameter);

                                     $.ajax({
                                                         type: "GET",
                                                         url: url,
                                                         data:  parameter,
                                                         async:true,
                                                         contentType: "application/json; charset=utf-8",
                                                         dataType: "jsonp",
                                                         success: function(responseObject) {

                                                                     processResponseObject(responseObject, callbackFunction);
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

                                 function processResponseObject(responseObject, callbackFunction)
                                        {
                                        //set View Id
                                        viewId =   responseObject.viewId;


                                                        callbackFunction.call( this, responseObject.result );
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

                                                             //  alert(result[index].dependencyNodeWrapper.groupId) ;
                                                              if(result[index].dependencyNodeWrapper.groupId!=groupId)
                                                               {

                                                                  delete result[index];
                                                               }


                                                                                                                                    }






                                            }
                                            if(artifactId != undefined  && artifactId!="")
                                            {

                                                   for(var index in result) {
                                           if(result[index].dependencyNodeWrapper.artifactId!=artifactId)
                                                                                                        {

                                                                                                           delete result[index];
                                                                                                        }

                                                                                                                                                                                      }


                                            }
                                             if(version != undefined && version!="")
                                            {

                                                 for(var index in result) {

                                                          if(result[index].dependencyNodeWrapper.version!=version)
                                                                                                                       {

                                                                                                                          delete result[index];
                                                                                                                       }
                                                                                                                                                                                    }

                                            }





                                          return result;


                                        }




 function highlightDependency(dependencyNodeWrapper,highlightClazz,openPath)
                                        {



                                            $("#"+dependencyNodeWrapper.id).addClass(highlightClazz);



                                           if(openPath == true)
                                           {
                                                           $("#"+dependencyNodeWrapper.id).parents("ul").show();

                                           }

                                        }


function highlightDependencyById(id,highlightClazz,highlightClazzToDelete,openPath)
                                        {

                                            $("."+highlightClazzToDelete).removeClass(highlightClazzToDelete);
                                            highlightDependency(dependencyNodeObjectList[id].dependencyNodeWrapper,highlightClazz,openPath);

                                        }

                                         function highlightDependencies(dependencyNodeWrapperList,highlightClazz,highlightClazzToDelete,openPath)
                                              {
                                                 $("."+highlightClazzToDelete).removeClass(highlightClazzToDelete);
                                                 for(var index in dependencyNodeWrapperList) {

                                                      highlightDependency(dependencyNodeWrapperList[index].dependencyNodeWrapper,highlightClazz,openPath);


                                                 }
                                              }







                                                 function searchAndHighlightDependencyByCoordinates(groupId,artifactId,version,highlightClazz,highlightClazzToDelete,openPath)
                                                                                {



                                                                                     var result =  searchForDependenciesByCoordinates(groupId,artifactId,version) ;

                                                                                     highlightDependencies(result,highlightClazz,highlightClazzToDelete,openPath);

                                                                                     return result;
                                                                                }