package com.clashinspector.jacksonSerializer;

import com.clashinspector.model.DependencyNodeWrapper;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 09.05.14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */


public class DependencyNodeWrapperSerializer extends JsonSerializer<DependencyNodeWrapper> {


  @Override
  public void serialize( DependencyNodeWrapper value, JsonGenerator jgen, SerializerProvider provider )
    throws IOException, JsonProcessingException {


    jgen.writeStartObject();

    jgen.writeStringField( "id", value.getId() );
    jgen.writeObjectField( "project", value.getProject() );
    if ( value.getParent() != null ) {
      jgen.writeStringField( "parentId", value.getParent().getId() );
    } else {
      jgen.writeStringField( "parentId", "" );
    }

    jgen.writeStringField( "artifactId", value.getArtifactId() );
    jgen.writeStringField( "groupId", value.getGroupId() );
    jgen.writeStringField( "version", value.getVersion().toString() );
    jgen.writeStringField( "scope", value.getScope() );
    jgen.writeBooleanField( "optional", value.getOptional() );
    jgen.writeBooleanField( "hasConcurrentDependencyWinner", value.hasConcurrentDependencyWinner() );

    jgen.writeStringField( "extension", value.getExtension() );
    jgen.writeStringField( "repository", value.getRepository() );
    jgen.writeNumberField( "graphDepth", value.getGraphDepth() );
    jgen.writeNumberField( "graphLevelOrderAbsolute", value.getGraphLevelOrderAbsolute() );

    jgen.writeObjectField( "children", value.getChildren() );


    jgen.writeEndObject();


  }


}
