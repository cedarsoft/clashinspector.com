
 //Vllt doch besser objekte ohne rest einzuschreiben ??
function DependencyNodeObject(dependencyNodeWrapper)
{


    this.dependencyNodeWrapper = dependencyNodeWrapper;



}
          //Eigenes objekt erstellen mit verweis auf guielement damit ausblenden etc.


       var dependencyNodeObjectList = {};
        var viewId =0;






$( document ).ready(function() {


$(".javascriptWarning").hide();

doGet("http://localhost:8080/dependencies",drawTree);




});






 function drawTree(result)
 {




 $("#analyzedDep").html("analyzed dependency: <b>"+ result.groupId+":"+result.artifactId+":"+result.version + "</b>");

 var html = "<ul id='dependencyTree'>" +   buildTree(result);

 html = html + "</ul>";




 $("#leftMain").html( html);

 }

function buildTree(data)
{        //console.log("jo2" + html)


              //var id =   "d"+data.graphDepth+"r"+data.graphLevelOrderRelative+"a"+data.graphLevelOrderAbsolute;




             var dep = new DependencyNodeObject(data);
                      dependencyNodeObjectList[data.id] = dep;
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


    return '<li class="depNodeLi"  ><div id="'+dependencyNodeObject.dependencyNodeWrapper.id+'" class="depNode">\
                                             <span class="groupId" title="groupId">'+dependencyNodeObject.dependencyNodeWrapper.groupId+'</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">'+dependencyNodeObject.dependencyNodeWrapper.version+'</span> \
                                              <div class="details"><span title="from maven used version of this project">used version:  </span><hr><span title="highest version of this project included in the analyzed dependency">highest version: </span><hr><span title="lowest version of this project included in the analyzed dependency">lowest version: '+dependencyNodeObject.dependencyNodeWrapper.project.lowestVersion+'</span><hr><span>number of dep: '+dependencyNodeObject.dependencyNodeWrapper.children.length+'</span></div> <div class="depMenu"><a id="detailsButton">details</a> | '+mavenCentralLink+' </div> </div>    ' ;
    }

    function drawMainDependency(groupId,artifactId,version)
        {


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

        $(document).on('dbclick', '.depNode', function(){

                     e.preventDefault();


                });

         $(document).on('mouseenter', '.depNode', function(){


                               $(this).children(".depMenu").show();


                });

                  $(document).on('mouseleave', '.depNode', function(){

                                          $(this).children(".depMenu").hide();


                                });



                                function markClashes()
                                {

                                }


                            $(document).on('click', '#detailsButton', function(){

                                                 $(this).parent(".depMenu").prev(".details").toggle();

                                                e.preventDefault();



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