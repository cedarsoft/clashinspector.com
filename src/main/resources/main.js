
 //Vllt doch besser objekte ohne rest einzuschreiben ??
function DependencyNodeObject(dependencyNodeWrapper,guiElementId)
{


    this.dependencyNodeWrapper = dependencyNodeWrapper;
    this.guiElementId =  guiElementId;


}
          //Eigenes objekt erstellen mit verweis auf guielement damit ausblenden etc.


       var dependencyNodeObjectList = {};







$( document ).ready(function() {


$(".javascriptWarning").hide();

doGet("http://localhost:8080/dependencies",drawTree);




});




function doGet(url,callbackFunction)
{

     $.ajax({
                         type: "GET",
                         url: url,
                         data: {},
                         async:true,
                         contentType: "application/json; charset=utf-8",
                         dataType: "jsonp",
                         success: function(data) {

                                     callbackFunction.call( this, data );
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

 function drawTree(data)
 {




 $("#analyzedDep").html("analyzed dependency: "+ data.groupId+":"+data.artifactId+":"+data.version);

 var html = "<ul id='dependencyTree'>" +   buildTree(data);

 html = html + "</ul>";




 $("#leftMain").html( html);

 }

function buildTree(data)
{        //console.log("jo2" + html)


              var id =   "d"+data.graphDepth+"r"+data.graphLevelOrderRelative+"a"+data.graphLevelOrderAbsolute;




             var dep = new DependencyNodeObject(data,id);
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


    return '<li class="depNodeLi"  ><div id="'+dependencyNodeObject.guiElementId+'" class="depNode">\
                                             <span class="groupId" title="groupId">'+dependencyNodeObject.dependencyNodeWrapper.groupId+'</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">'+dependencyNodeObject.dependencyNodeWrapper.version+'</span> <div class="depMenu"><a>details</a> | '+mavenCentralLink+' </div> </div>    ' ;
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