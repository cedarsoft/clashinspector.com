package com.clashinspector.jacksonSerializer;

import com.clashinspector.model.Project;
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


public class ProjectSerializerForDependencyNodeWrapper extends JsonSerializer<Project> {


  @Override
  public void serialize( Project value, JsonGenerator jgen, SerializerProvider provider )
    throws IOException, JsonProcessingException {

    jgen.writeStartObject();

    jgen.writeStringField( "groupId", value.getGroupId() );

    jgen.writeStringField( "artifactId", value.getArtifactId() );

    jgen.writeStringField( "usedVersion", value.getUsedVersion().toString() );

    jgen.writeStringField( "highestVersion", value.getHighestVersion().toString() );

    jgen.writeStringField( "lowestVersion", value.getLowestVersion().toString() );


    jgen.writeStringField( "dependencyNodeWrapperWithUsedVersionId", value.getDependencyNodeWrapperWithUsedVersion().getId() );


    jgen.writeEndObject();


  }


}
