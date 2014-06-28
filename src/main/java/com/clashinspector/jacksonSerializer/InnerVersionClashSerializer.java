package com.clashinspector.jacksonSerializer;

import com.clashinspector.model.InnerVersionClash;
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


public class InnerVersionClashSerializer extends JsonSerializer<InnerVersionClash> {


  @Override
  public void serialize( InnerVersionClash value, JsonGenerator jgen, SerializerProvider provider )
    throws IOException, JsonProcessingException {


    jgen.writeStartObject();

    jgen.writeStringField( "clashSeverity", value.getClashSeverity().toString() );
    jgen.writeStringField( "referredDependencyNodeWrapperId", value.getReferredDependencyNodeWrapper().getId() );
    jgen.writeStringField( "usedDependencyNodeWrapperId", value.getUsedDependencyNodeWrapper().getId() );


    jgen.writeEndObject();


  }


}
