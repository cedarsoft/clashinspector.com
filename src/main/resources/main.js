
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


function buildTree(data, html)
{
   html = html + '<ul>';

               for(var i=0;i<data.children.length;i++){
                 html = html + drawDependency(data.children[i].groupId,data.children[i].artifactId,data.children[i].version);
                 if(data.children[i].children.length==0)  {
                    html = html + '</li>' ;
                 }
                 else{
                    buildTree(data.children[i], html);
                 }
                 if(i == data.children.length-1) {
                    html = html + '</ul>' ;
                 }
               }
   return html;
}

function drawDependency(groupId,artifactId,version)
    {

    return '<li class="depNode">\
                                             <span class="groupId" title="groupId">'+groupId+'</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">'+artifactId+'</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">'+version+'</span>  \
                                                  ' ;
    }

    function drawMainDependency(data)
        {
        }