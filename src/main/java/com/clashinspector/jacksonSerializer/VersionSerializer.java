package com.clashinspector.jacksonSerializer;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.eclipse.aether.version.Version;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 09.05.14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */


public class VersionSerializer extends JsonSerializer<Version> {


  @Override
  public void serialize( Version value, JsonGenerator jgen, SerializerProvider provider )
    throws IOException, JsonProcessingException {


    jgen.writeString( value.toString() );


  }


}
