
 //Vllt doch besser objekte ohne rest einzuschreiben ??
 var dependencyNodeWrapperRoot;
          //Eigenes objekt erstellen mit verweis auf guielement damit ausblenden etc.

$( document ).ready(function() {

doGet("http://localhost:8080/dependencies",buildTree);






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

function buildTree(data)
{                alert("jo");
       	alert(data.artifactId);


    var html =    '<ul>' ;

               for(var i=0;i<data.children.length;i++){
                 html = html + drawDependency(data.children[i].groupId,data.children[i].artifactId,data.children[i].version);
               }

            html = html + '</ul>' ;

     $("#leftMain").html( html);




}

function drawDependency(groupId,artifactId,version)
    {

    return '<li class="depNode">\
                                             <span class="groupId" title="groupId">'+groupId+'</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">'+artifactId+'</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">'+version+'</span>  \
                                         </li>         ' ;
    }

    function drawMainDependency(data)
        {
        }