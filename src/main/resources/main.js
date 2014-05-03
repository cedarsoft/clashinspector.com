
 //Vllt doch besser objekte ohne rest einzuschreiben ??
 var dependencyNodeWrapperRoot;
          //Eigenes objekt erstellen mit verweis auf guielement damit ausblenden etc.



$( document ).ready(function() {

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

 var html = "<ul id='dependencyTree'>" +   buildTree(data);

 html = html + "</ul>";




 $("#leftMain").html( html);

 }

function buildTree(data)
{        //console.log("jo2" + html)
         var html=drawDependency(data.groupId,data.artifactId,data.version);


          // console.log("jo3" + html)

      if(data.children.length >0)
      {          // console.log("jo4" + html)
         html = html +    '<ul>' ;

               for(var i=0;i<data.children.length;i++){



                 html = html + buildTree(data.children[i]);
               }


            html = html + '</ul>' ;
            html = html + '</li>';
       }
       else
       {
       html = html +'</li>' ;
       }


         return html;



}

function drawDependency(groupId,artifactId,version)
    {

    return '<li class="depNodeLi"><div class="depNode">\
                                             <span class="groupId" title="groupId">'+groupId+'</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">'+artifactId+'</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">'+version+'</span>  \  </div>    ' ;
    }

    function drawMainDependency(data)
        {
        }