function DependencyNodeObject( dependencyNodeWrapper ) {


    this.dependencyNodeWrapper = dependencyNodeWrapper;


}


function UserSettingsWrapper( includedScopes, excludedScopes, includeOptional, clashSeverity ) {

    this.includedScopes = includedScopes;
    this.excludedScopes = excludedScopes;
    this.includeOptional = includeOptional;
    this.clashSeverity = clashSeverity;

    this.convertToParameterString = convertToParameterString;
    this.applyValuesToView = applyValuesToView;

    this.applyViewValues = function () {
        this.includedScopes = [];
        this.excludedScopes = [];
        var that = this;


        $( "#includedScopeList" ).find( ".selected" ).each( function () {


            that.includedScopes.push( $( this ).text() );
        } );


        $( "#excludedScopeList" ).find( ".selected" ).each( function () {


            that.excludedScopes.push( $( this ).text() );
        } );

        if ( $( "#includeOptional" ).hasClass( "selected" ) ) {
            that.includeOptional = true;
        }
        else {
            that.includeOptional = false;
        }

        this.clashSeverity = $( "#clashSeverity .selected" ).text();


    }


    function applyValuesToView() {
        //Delete old selections in view
        $( ".settingsContainer" ).find( ".selected" ).removeClass( "selected" );

        for ( var i = 0; i < this.includedScopes.length; i++ ) {

            $( "#includedScopeList li:contains('" + this.includedScopes[i] + "')" ).addClass( "selected" );

        }
        for ( var i = 0; i < this.excludedScopes.length; i++ ) {

            $( "#excludedScopeList li:contains('" + this.excludedScopes[i] + "')" ).addClass( "selected" );

        }

        $( "#clashSeverity li:contains('" + this.clashSeverity + "')" ).addClass( "selected" );

        if ( this.includeOptional == true ) {
            $( "#includeOptional" ).addClass( "selected" );
        }


    }

    function convertToParameterString() {


        var parameterString = "";

        if ( this.includeOptional != undefined ) {
            parameterString = parameterString + "includeOptional=" + this.includeOptional + "";
        }

        if ( this.clashSeverity != undefined ) {

            parameterString = parameterString + "&clashSeverity=" + this.clashSeverity;

        }

        if ( this.includedScopes != undefined ) {
            for ( var i = 0; i < this.includedScopes.length; i++ ) {
                parameterString = parameterString + "&includedScope=" + this.includedScopes[i];
            }
        }

        if ( this.excludedScopes != undefined ) {
            for ( var i = 0; i < this.excludedScopes.length; i++ ) {
                parameterString = parameterString + "&excludedScope=" + this.excludedScopes[i];
            }

        }


        logConsole( "Convertion completed with: " + parameterString );

        return parameterString;
    }


}


var dependencyNodeObjectList = new Array();
var searchResult = new Array();
var activeSearchDependencyId;
var outerVersionClashList = new Array();
var userSettingsWrapper = new UserSettingsWrapper();
var deepestGraphDepth = 0;
var port = {replacePort};

function emptyAllInputs() {
    $( "input" ).val( "" );

}


$( document ).ready( function () {


    $( ".javascriptWarning" ).hide();
    emptyAllInputs();


    initializeOnTopIfScrolled();
    addMainPaddingToContentContainer();

    getTree();


} );


function backOffValues()
{
         dependencyNodeObjectList = new Array();
         searchResult = new Array();
         outerVersionClashList = new Array();
         deepestGraphDepth = 0;
         activeSearchDependencyId = undefined;

  $( "#treeContainer" ).html( "" );
    $( "#clashListContainer" ).html( "" );

}

function getTree() {

       backOffValues();


    $( "#contentContainer" ).addClass( "loading" );
    logConsole(  "getTree started" );
    doGet( "http://localhost:"+port+"/dependencies", drawTree, "", getList );


}

function getList() {
    logConsole("getList started" );
    doGet( "http://localhost:"+port+"/dependencies/outerVersionClashes", drawList );
    logConsole( "getList finished" );
    $("#treeContainer").width(deepestGraphDepth*300);
     applyTreeViewMode();

}

 function applyTreeViewMode()
 {
    var selectedValue = $("#treeViewMode .selected").text();

           if ( selectedValue == "Shortened" ) {
            $( "#dependencyTree" ).removeClass( "viewModeFull" );
               $( "#dependencyTree" ).addClass( "viewModeShortened" );
           }
           else if ( selectedValue == "Full" ) {
              $( "#dependencyTree" ).removeClass( "viewModeShortened" );
               $( "#dependencyTree" ).addClass( "viewModeFull" );
           }
 }


function drawList( result ) {
    logConsole("drawList started" );
    var listHtml = '<ul>';

    for ( var i = 0; i < result.length; i++ ) {

        var id = result[i].project.groupId + ":" + result[i].project.artifactId;

        outerVersionClashList[id] = result[i];

        listHtml = listHtml + buildClashListEntry( outerVersionClashList[id] );
    }

    $( "#clashListContainer" ).html( listHtml + "</ul>" );


}

function buildClashListEntry( outerVersionClash ) //and highlight clashes in tree
{
    var idList = "";
    for ( var i = 0; i < outerVersionClash.innerVersionClashes.length; i++ ) {
        idList = idList + "'" + outerVersionClash.innerVersionClashes[i].referredDependencyNodeWrapperId + "'";

        if ( i < outerVersionClash.innerVersionClashes.length - 1 ) {
            idList = idList + ",";
        }
    }



    var versionsLink = '<span  onclick="highlightDependencyByIds([' + idList + '],&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true,true);"><span>' + outerVersionClash.project.groupId + "</span>:<span>" + outerVersionClash.project.artifactId + '</span></span>';


    var html = "<li>" + versionsLink + "<ul>";
   logConsole( "outerVersionClash.innerVersionClashes.length " + outerVersionClash.innerVersionClashes.length );
    for ( var i = 0; i < outerVersionClash.innerVersionClashes.length; i++ ) {
        var innerVersionClash = outerVersionClash.innerVersionClashes[i];
        var dependencyNodeWrapper = dependencyNodeObjectList[innerVersionClash.referredDependencyNodeWrapperId].dependencyNodeWrapper;

        var clashSeverityClass = "";
        if ( innerVersionClash.clashSeverity == "UNSAFE" ) {
            clashSeverityClass = "clashSeverityUnsafe";
            highlightDependency( dependencyNodeWrapper, "clashSeverityUnsafe", false, false );
            addArrowClassToParentWrappers( dependencyNodeWrapper, "clashSeverityUnsafe" );
        }
        if ( innerVersionClash.clashSeverity == "CRITICAL" ) {
            highlightDependency( dependencyNodeWrapper, "clashSeverityCritical", false, false );
            clashSeverityClass = "clashSeverityCritical";
            addArrowClassToParentWrappers( dependencyNodeWrapper, "clashSeverityCritical" );
        }


        var versionLink = '<span  onclick="highlightDependencyById(&quot;' + dependencyNodeWrapper.id + '&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true,true, true);">' + dependencyNodeObjectList[innerVersionClash.referredDependencyNodeWrapperId].dependencyNodeWrapper.version + '</span>';


        html = html + "<li class='" + clashSeverityClass + "'>" + versionLink + "</li>";

        logConsole( "innerVersionClash " + innerVersionClash.referredDependencyNodeWrapperId );

    }

    html = html + "</ul></li>";

    return html;


}


function buildList( result ) {

}

function drawTree( result ) {


    $( "#analyzedDep" ).html( "<h2>" + result.groupId + ":" + result.artifactId + ":" + result.version + "</h2>" );

    var html = "<ul id='dependencyTree'>" + buildTree( result );

    html = html + "</ul>";


    $( "#treeContainer" ).html( html );
    logConsole( "drawTree finished" );

    $( "#contentContainer" ).removeClass( "loading" );
}

function buildTree( data ) {


    var dep = new DependencyNodeObject( data );
    dependencyNodeObjectList[dep.dependencyNodeWrapper.id] = dep;


    var html = buildGuiDependency( dep );

    if ( data.children.length > 0 ) {
        var display = "display:block;";
        if ( dep.dependencyNodeWrapper.graphDepth > 0 ) {
            display = "display:none;"
        }

        html = html + '<ul class="depNodeUl" style="' + display + '">';

        for ( var i = 0; i < data.children.length; i++ ) {


            html = html + buildTree( data.children[i] );
        }


        html = html + '</ul>';
        html = html + ' <div class="clearing"></div></li>';
    }
    else {
        html = html + ' <div class="clearing"></div></li>';
    }


    return html;


}

function calculateDeepestGraphDepth(depth)
{

  if(deepestGraphDepth < depth)
  {
    deepestGraphDepth = depth;
  }
}

function buildGuiDependency( dependencyNodeObject ) {

             calculateDeepestGraphDepth( dependencyNodeObject.dependencyNodeWrapper.graphDepth);


    if ( dependencyNodeObject.dependencyNodeWrapper.repository == "repo.maven.apache.org" ) {
        var mavenCentralHref = "http://search.maven.org/#artifactdetails|" + dependencyNodeObject.dependencyNodeWrapper.groupId + "|" + dependencyNodeObject.dependencyNodeWrapper.artifactId + "|" + dependencyNodeObject.dependencyNodeWrapper.version + "|" + dependencyNodeObject.dependencyNodeWrapper.extension;
        var mavenCentralLink = '<a href="' + mavenCentralHref + '" target="_blank"> maven-central </a>';

    }

    var usedVersionLink = '<span class="usedVersionLink" onclick="highlightDependencyById(&quot;' + dependencyNodeObject.dependencyNodeWrapper.project.dependencyNodeWrapperWithUsedVersionId + '&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true,true,true);">' + dependencyNodeObject.dependencyNodeWrapper.project.usedVersion + '</span>';
    var highestVersionLink = '<span class="highestVersionLink" onclick="searchAndHighlightDependencyByCoordinates(&quot;' + dependencyNodeObject.dependencyNodeWrapper.groupId + '&quot;,&quot;' + dependencyNodeObject.dependencyNodeWrapper.artifactId + '&quot;,&quot;' + dependencyNodeObject.dependencyNodeWrapper.project.highestVersion + '&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true, true);">' + dependencyNodeObject.dependencyNodeWrapper.project.highestVersion + '</span>';
    var lowestVersionLink = '<span class="lowestVersionLink" onclick="searchAndHighlightDependencyByCoordinates(&quot;' + dependencyNodeObject.dependencyNodeWrapper.groupId + '&quot;,&quot;' + dependencyNodeObject.dependencyNodeWrapper.artifactId + '&quot;,&quot;' + dependencyNodeObject.dependencyNodeWrapper.project.lowestVersion + '&quot;,&quot;highlightSearch&quot;,&quot;highlightSearch&quot;,true,true);">' + dependencyNodeObject.dependencyNodeWrapper.project.lowestVersion + '</span>';

    var optionalHtml = "";

    if ( dependencyNodeObject.dependencyNodeWrapper.optional == true ) {
        optionalHtml = " <span class='optional' title='This dependency is optional'>(o)</span>";
    }


    var scopeHtml = " <span class='scope' title='The scope of this dependency is " + dependencyNodeObject.dependencyNodeWrapper.scope + ".'>(" + dependencyNodeObject.dependencyNodeWrapper.scope.charAt( 0 ) + ")</span>";
    var usedHtml = "";
    if ( dependencyNodeObject.dependencyNodeWrapper.project.dependencyNodeWrapperWithUsedVersionId == dependencyNodeObject.dependencyNodeWrapper.id ) {
        usedHtml = "<span class='used' title='This dependency is used by maven.'>(u)</span>";

    }

    //searchviaID   var usedVersionLink = '<span class="usedVersionLink" onclick="searchAndHighlightDependency(&quot;'+dependencyNodeObject.dependencyNodeWrapper.groupId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.artifactId+'&quot;,&quot;'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'&quot;,&quot;highlightSearch&quot;,&quot;true&quot;);">'+dependencyNodeObject.dependencyNodeWrapper.project.usedVersion+'</span>';
    var arrowClass = "";
    if ( dependencyNodeObject.dependencyNodeWrapper.children.length > 0 ) {
        arrowClass = "clashSeveritySafe";
    }

    var wrapperTitle = "";
    if ( dependencyNodeObject.dependencyNodeWrapper.hasConcurrentDependencyWinner ) {
        arrowClass = arrowClass + " hasConcurrentDependencyWinner";
        wrapperTitle = "This dependency is not resolved at this graph-position. Check used version."
    }

    var idHtml = "";
    idHtml = idHtml + dependencyNodeObject.dependencyNodeWrapper.id;

    return '<li class="depNodeLi"  ><div class="depNodeWrapper ' + arrowClass + '" title="' + wrapperTitle + '" id="dNW' + idHtml + '"><div id="' + idHtml + '" class="depNode">\
                                             <span class="groupId" title="groupId">' + dependencyNodeObject.dependencyNodeWrapper.groupId + '</span>     \
                                             <hr>                                         \
                                             <span class="artifactId" title="artifactId">' + dependencyNodeObject.dependencyNodeWrapper.artifactId + '</span>   \
                                             <hr>                                      \
                                             <span class="version" title="version">' + dependencyNodeObject.dependencyNodeWrapper.version + '</span>' + scopeHtml + ' ' + optionalHtml + ' ' + usedHtml + '  \
                                              <div class="details"><div title="from maven used version of this project"><span >used version:  </span>' + usedVersionLink + '</div> <hr><div title="highest version of this project included in the analyzed dependency"><span >highest version:  </span>' + highestVersionLink + '</div><hr><div title="lowest version of this project included in the analyzed dependency"><span >lowest version: </span>' + lowestVersionLink + '</div></div> <div class="depMenu"><a class="detailsButton">details</a> | ' + mavenCentralLink + ' </div> </div>   </div>   ';
}

//TODO add count of the same dependencies <hr><div title="number of direct dependencies"><span >number of dep: '+dependencyNodeObject.dependencyNodeWrapper.children.length+'</span></div>


var delayTime = 200, clickNumber = 0, timer = null;

$( document ).on( 'click', '.depNodeWrapper', function () {

    var thisVar = $( this );

    clickNumber++;

    if ( clickNumber === 1 ) {

        timer = setTimeout( function () {

            thisVar.next( "ul" ).toggle();

            clickNumber = 0;

        }, delayTime );

    } else {

        clearTimeout( timer );
        thisVar.next( "ul" ).show();
        thisVar.next( "ul" ).find( "ul" ).show();
        clickNumber = 0;
    }


} );


$( document ).on( 'click', '#searchJumpBack', function () {

    var idBefore;
    var jumpToLast = false;
    for ( var index in searchResult ) {

        if ( activeSearchDependencyId == undefined ) {
            activeSearchDependencyId = searchResult[index].dependencyNodeWrapper.id;
            break;
        }

        var idNow = searchResult[index].dependencyNodeWrapper.id;
        if ( idNow == activeSearchDependencyId ) {
            if ( idBefore != undefined ) {
                jumpToLocation( idBefore );
                activeSearchDependencyId = idBefore;
                break;
            }
            else {
                jumpToLast = true;
            }

        }
        idBefore = idNow;
    }

    if ( jumpToLast == true ) {
        jumpToLocation( idBefore );
        activeSearchDependencyId = idBefore;
    }


} );

$( document ).on( 'click', '#searchJumpNext', function () {

    var jumpToNext = false;

    for ( var index in searchResult ) {

        if ( activeSearchDependencyId == undefined ) {
            activeSearchDependencyId = searchResult[index].dependencyNodeWrapper.id;
            jumpToNext = true;
            break;
        }
        var idNow = searchResult[index].dependencyNodeWrapper.id;
        if ( jumpToNext == true ) {
            jumpToLocation( idNow );
            activeSearchDependencyId = idNow;
            jumpToNext = false;
            break;
        }


        if ( idNow == activeSearchDependencyId ) {
            jumpToNext = true;

        }

    }


    if ( jumpToNext == true ) {
        for ( var index in searchResult ) {
            var idNow = searchResult[index].dependencyNodeWrapper.id;
            jumpToLocation( idNow );
            activeSearchDependencyId = idNow;
            break;
        }
    }

} );



$( document ).on( 'input', '.searchInput', function () {
    var result = searchAndHighlightDependencyByCoordinates( $( '#groupIdInput' ).val(), $( '#artifactIdInput' ).val(), $( '#versionInput' ).val(), 'highlightSearch', 'highlightSearch', true, false );

    if ( Object.keys( result ).length > 0 ) {
        $( "#searchResultOutput" ).html( "results <span>(" + Object.keys( result ).length + ")</span>" );
    }
    else {
        $( "#searchResultOutput" ).html( "no results" );

    }


} );

$( document ).on( 'click', '#applyResolutionSettings', function () {


    userSettingsWrapper.applyViewValues();
    clearSearchResults();
    getTree();

} );

$( document ).on( 'click', '#clearSearchButton', function () {
    clearSearchResults();

} );


function calculateMainPadding() {
    var result = 0;

    if ( $( "#headerContainer" ).css( "top" ) != 0 ) {
        result = result + $( "#logoContainer" ).outerHeight( true );
    }

    result = result + $( "#headerContainer" ).outerHeight( true );


    return result;
}

function addMainPaddingToContentContainer() {
    document.getElementById( "contentContainer" ).style.paddingTop = calculateMainPadding() + "px";
}

$( document ).on( 'click', '.openSearchButton', function () {

    $( "#searchContainer" ).toggle();
    $( this ).children( "span" ).toggleClass( "triangleDown" );
    $( this ).children( "span" ).toggleClass( "triangleUp" );

    addMainPaddingToContentContainer();


} );

$( document ).on( 'click', '.openSettingsButton', function () {
    userSettingsWrapper.applyValuesToView();

    $( "#settingsFilterContainer" ).toggle();
    $( this ).children( "span" ).toggleClass( "triangleDown" );
    $( this ).children( "span" ).toggleClass( "triangleUp" );
    addMainPaddingToContentContainer();
} );

$( document ).on( 'click', '.openClashListButton', function () {
    $( "#clashListContainer" ).toggle();
    $( this ).children( "span" ).toggleClass( "triangleDown" );
    $( this ).children( "span" ).toggleClass( "triangleUp" );
} );

$( document ).on( 'click', '.openLegendButton', function () {
    $( "#legendContainer" ).toggle();
    $( this ).children( "span" ).toggleClass( "triangleDown" );
    $( this ).children( "span" ).toggleClass( "triangleUp" );
    addMainPaddingToContentContainer();
} );

$( document ).on( 'dbclick', '.depNode', function () {

    e.preventDefault();


} );



$( document ).on( 'mouseenter', '.depNode', function () {


    if ( $( "#dependencyTree" ).hasClass( "viewModeShortened" ) ) {
        $( this ).children( "hr" ).addClass( "showBlock" );
        $( this ).children( ".version" ).addClass( "showInlineBlock" );
        $( this ).children( ".groupId" ).addClass( "showBlock" );
        $( this ).children( ".optional" ).addClass( "showInlineBlock" );
        $( this ).children( ".used" ).addClass( "showInlineBlock" );
        $( this ).children( ".scope" ).addClass( "showInlineBlock" );
    }

    $( this ).children( ".depMenu" ).show();


} );

$( document ).on( 'mouseleave', '.depNode', function () {
    if ( $( "#dependencyTree" ).hasClass( "viewModeShortened" ) ) {
        $( this ).children( "hr" ).removeClass( "showBlock" );
               $( this ).children( ".version" ).removeClass( "showInlineBlock" );
               $( this ).children( ".groupId" ).removeClass( "showBlock" );
               $( this ).children( ".optional" ).removeClass( "showInlineBlock" );
               $( this ).children( ".used" ).removeClass( "showInlineBlock" );
               $( this ).children( ".scope" ).removeClass( "showInlineBlock" );
    }
    $( this ).children( ".depMenu" ).hide();


} );


$( document ).on( 'click', '.easySelectBox.selectModeMultiple li', function () {

    $( this ).toggleClass( "selected" );


} );

$( document ).on( 'click', '.easySelectBox.selectModeSingle li', function () {

    $( this ).parent().children().removeClass( "selected" );
    $( this ).toggleClass( "selected" );


} );

$( document ).on( 'click', '#treeViewMode li', function () {
    applyTreeViewMode();

} );

$( document ).on( 'click', '.easyCheckBox', function () {

    $( this ).toggleClass( "selected" );


} );

$( document ).on( 'click', '.detailsButton', function () {

    $( this ).parent( ".depMenu" ).prev( ".details" ).toggle();

    e.preventDefault();


} );

$( document ).on( 'click', '.details', function () {


    e.stopPropagation();


} );


function doGet( url, callbackFunction, parameters, syncCallFunction ) {

    var parameter = userSettingsWrapper.convertToParameterString();


    $.ajax( {
                type: "GET",
                url: url,
                data: parameter,
                async: true,
                contentType: "application/json; charset=utf-8",
                dataType: "jsonp",
                success: function ( responseObject ) {

                    processResponseObject( responseObject, callbackFunction, syncCallFunction );
                },
                error: function ( XMLHttpRequest, textStatus, errorThrown ) {
                    alert( 'Error in ajax call. Please contact developer.' );
                },
                beforeSend: function ( XMLHttpRequest ) {
                    //show loading
                },
                complete: function ( XMLHttpRequest, textStatus ) {
                    //hide loading
                }
            } );

}




function processResponseObject( responseObject, callbackFunction, syncCallFunction ) {

    userSettingsWrapper.includedScopes = responseObject.userParameterWrapper.includedScopes;
    userSettingsWrapper.excludedScopes = responseObject.userParameterWrapper.excludedScopes;
    userSettingsWrapper.includeOptional = responseObject.userParameterWrapper.includeOptional;
    userSettingsWrapper.clashSeverity = responseObject.userParameterWrapper.clashSeverity;


    userSettingsWrapper.applyValuesToView();
    logConsole( "userSettingsWrapper clashSeverity " + userSettingsWrapper.clashSeverity );

    callbackFunction.call( this, responseObject.result );

    if ( syncCallFunction != undefined && syncCallFunction != "" ) {
        syncCallFunction.call();
    }


}



function clearSearchResults() {
    searchResult = new Array();
    activeSearchDependencyId = undefined;
    $( ".highlightSearch" ).removeClass( "highlightSearch" );
    $( "#searchResultOutput" ).html( "no results" );
    $( ".searchInput" ).val( "" );

}

function searchForDependencyById( id ) {
    return   dependencyNodeObjectList[id].dependencyNodeWrapper;
}


function searchForDependenciesByCoordinates( groupId, artifactId, version ) {
    //  alert("dependencyNodeObjectList length: " + Object.keys(dependencyNodeObjectList).length)
    searchResult = jQuery.extend( {}, dependencyNodeObjectList );
    activeSearchDependencyId = undefined;

    if ( groupId != undefined && groupId != "" ) {


        for ( var index in searchResult ) {

            var compValue1;
            var compValue2 = groupId;
            if ( groupId.slice( -1 ) == "*" ) {
                compValue2 = groupId.substring( 0, groupId.length - 1 );
                compValue1 = searchResult[index].dependencyNodeWrapper.groupId.substring( 0, compValue2.length );

            }
            else {
                compValue1 = searchResult[index].dependencyNodeWrapper.groupId;
            }


            if ( compValue1 != compValue2 ) {

                delete searchResult[index];
            }


        }


    }
    if ( artifactId != undefined && artifactId != "" ) {

        for ( var index in searchResult ) {


            var compValue1;
            var compValue2 = artifactId;
            if ( artifactId.slice( -1 ) == "*" ) {
                compValue2 = artifactId.substring( 0, artifactId.length - 1 );
                compValue1 = searchResult[index].dependencyNodeWrapper.artifactId.substring( 0, compValue2.length );

            }
            else {
                compValue1 = searchResult[index].dependencyNodeWrapper.artifactId;
            }


            if ( compValue1 != compValue2 ) {

                delete searchResult[index];
            }

        }


    }
    if ( version != undefined && version != "" ) {

        for ( var index in searchResult ) {

            var compValue1;
            var compValue2 = version;
            if ( version.slice( -1 ) == "*" ) {
                compValue2 = version.substring( 0, version.length - 1 );
                compValue1 = searchResult[index].dependencyNodeWrapper.version.substring( 0, compValue2.length );

            }
            else {
                compValue1 = searchResult[index].dependencyNodeWrapper.version;
            }

            if ( compValue1 != compValue2 ) {

                delete searchResult[index];
            }
        }

    }
    if ( (groupId == undefined || groupId == "") && (artifactId == undefined || artifactId == "") && (version == undefined || version == "") ) {

        searchResult = new Array();
    }

    //Check if search result includes the active dependency d0r0a0

    delete searchResult["d0r0a0"];


    return searchResult;


}


function jumpToLocation( locationId ) {
    location.href = "#" + locationId;

    $( 'html, body' ).animate( {
                                   scrollTop: $( "#" + locationId ).offset().top - calculateMainPadding()
                               },
                               0 );
}

function highlightDependency( dependencyNodeWrapper, highlightClazz, openPath, jumpTo ) {


    $( "#" + dependencyNodeWrapper.id ).addClass( highlightClazz );


    if ( openPath == true ) {
        $( "#" + dependencyNodeWrapper.id ).parents( "ul" ).show();

    }

    if ( jumpTo == true ) {
        jumpToLocation( dependencyNodeWrapper.id );

    }

}


function highlightDependencyById( id, highlightClazz, highlightClazzToDelete, openPath, jumpTo, doClearSearchResults ) {
     if(doClearSearchResults == true)
              {
                 clearSearchResults();
              }
         $( "."+highlightClazzToDelete ).removeClass(highlightClazzToDelete);

    highlightDependency( dependencyNodeObjectList[id].dependencyNodeWrapper, highlightClazz, openPath, jumpTo );

}
function highlightDependencyByIds( ids, highlightClazz, highlightClazzToDelete, openPath, doClearSearchResults ) {


      if(doClearSearchResults == true)
                    {
                       clearSearchResults();
                    }
         $( "."+highlightClazzToDelete ).removeClass(highlightClazzToDelete);

    for ( var i = 0; i < ids.length; i++ ) {


        highlightDependency( dependencyNodeObjectList[ids[i]].dependencyNodeWrapper, highlightClazz, openPath, false );
    }


}

function highlightDependencies( dependencyNodeWrapperList, highlightClazz, highlightClazzToDelete, openPath,doClearSearchResults ) {

     if(doClearSearchResults == true)
                       {
                          clearSearchResults();
                       }
            $( "."+highlightClazzToDelete ).removeClass(highlightClazzToDelete);


    for ( var index in dependencyNodeWrapperList ) {

        highlightDependency( dependencyNodeWrapperList[index].dependencyNodeWrapper, highlightClazz, openPath );


    }
}


function addArrowClassToParentWrappers( dependencyNodeWrapper, clazz ) {



    $( "#" + dependencyNodeWrapper.id ).parents( ".depNodeUl" ).prev( ".depNodeWrapper" ).addClass( clazz );




}


function searchAndHighlightDependencyByCoordinates( groupId, artifactId, version, highlightClazz, highlightClazzToDelete, openPath, doClearSearchResults ) {


    var result = searchForDependenciesByCoordinates( groupId, artifactId, version );

    highlightDependencies( result, highlightClazz, highlightClazzToDelete, openPath,doClearSearchResults );

    return result;
}


function initializeOnTopIfScrolled() {

    var headerContainer = $( "#headerContainer" );

    var _defautlTop = headerContainer.offset().top - $( document ).scrollTop();

    var _defautlLeft = headerContainer.offset().left - $( document ).scrollLeft();


    var originalHeaderContainerPosition = headerContainer.css( 'position' );
    var originalHeaderContainerTop = headerContainer.css( 'top' );
    var originalHeaderContainerLeft = headerContainer.css( 'left' );
    var originalHeaderContainerZIndex = headerContainer.css( 'z-index' );
    var originalHeaderContainerWidth = headerContainer.css( 'width' );


    var originalLogoContainerPosition = $( "#logoContainer" ).css( "position" );
    var originalLogoContainerWidth = $( "#logoContainer" ).css( "width" );

    $( window ).scroll( function () {


        if ( $( this ).scrollTop() > _defautlTop && $( this ).scrollLeft() > _defautlLeft ) {
            $( "#headerContainer" ).css( {'position': 'fixed', 'top': 0 + 'px',
                                             'z-index': 99999} );
            $( "#logoContainer" ).css( {
                                           position: originalLogoContainerPosition

                                       } );


        }
        else if ( $( this ).scrollTop() > _defautlTop ) {
            $( "#headerContainer" ).css( {'position': 'fixed', 'top': 0 + 'px', 'z-index': 99999} );
            $( "#logoContainer" ).css( {position: originalLogoContainerPosition } );

        }
        else if ( $( this ).scrollLeft() > _defautlLeft ) {
            if ( $( "#headerContainer" ).css( "top" ).replace( "px", "" ) == "0" ) {
                $( "#headerContainer" ).css( {'position': 'fixed', 'top': '70px'} );
                $( "#logoContainer" ).css( {position: "fixed"} );

            }
            else {
                $( "#headerContainer" ).css( {'position': 'fixed'} );
                $( "#logoContainer" ).css( {position: "fixed"} );

            }


        }
        else {
            $( "#headerContainer" ).css( {'position': originalHeaderContainerPosition, 'top': originalHeaderContainerTop,
                                             'z-index': originalHeaderContainerZIndex} );

            $( "#logoContainer" ).css( {'position': originalLogoContainerPosition} );
        }

    } );
}

window.onresize = function ( event ) {
    addMainPaddingToContentContainer();
};


function logConsole( message ) {
  //  console.log( '[' + new Date().toUTCString() + '] ' + message );
}

function logAlert( message ) {
 //alert( message );
}