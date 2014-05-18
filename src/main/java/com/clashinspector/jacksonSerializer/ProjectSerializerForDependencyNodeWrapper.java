package com.clashinspector.jacksonSerializer;

import com.clashinspector.model.DependencyNodeWrapper;
import com.clashinspector.model.OuterVersionClash;
import com.clashinspector.model.Project;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.eclipse.aether.version.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 09.05.14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */


public class ProjectSerializerForDependencyNodeWrapper extends JsonSerializer<Project> {



    @Override
    public void serialize(Project value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {

      jgen.writeStartObject();
      System.out.println("Serializer1");
                            jgen.writeStringField( "groupId", value.getGroupId() );
      System.out.println("Serializer2");
      jgen.writeStringField( "artifactId", value.getArtifactId() );
      System.out.println("Serializer3");
      jgen.writeStringField( "usedVersion", value.getUsedVersion().toString() );
      System.out.println("Serializer4");
      jgen.writeStringField( "highestVersion", value.getHighestVersion().toString()  );
      System.out.println("Serializer5");
      jgen.writeStringField( "lowestVersion", value.getLowestVersion().toString()  );
      System.out.println("Serializer6");

      jgen.writeStringField( "dependencyNodeWrapperWithUsedVersionId", value.getDependencyNodeWrapperWithUsedVersion().getId() );





      jgen.writeEndObject();



    }


}
