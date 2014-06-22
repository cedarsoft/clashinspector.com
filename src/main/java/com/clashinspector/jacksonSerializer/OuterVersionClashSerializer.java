package com.clashinspector.jacksonSerializer;

import com.clashinspector.model.OuterVersionClash;
import com.clashinspector.mojos.ClashSeverity;
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


public class OuterVersionClashSerializer extends JsonSerializer<OuterVersionClash> {

  private ClashSeverity forClashSeverity;

  public OuterVersionClashSerializer( ClashSeverity forClashSeverity ) {
    this.forClashSeverity = forClashSeverity;

  }

  @Override
  public void serialize( OuterVersionClash value, JsonGenerator jgen, SerializerProvider provider )
    throws IOException, JsonProcessingException {


    jgen.writeStartObject();

    jgen.writeStringField( "clashSeverity", value.getClashSeverity().toString() );


    jgen.writeObjectField( "project", value.getProject() );
    jgen.writeObjectField( "innerVersionClashes", value.getInnerVersionClashForClashSeverityLevel( forClashSeverity ) );


    jgen.writeEndObject();


  }


}
