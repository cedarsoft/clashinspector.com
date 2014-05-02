package com.clashinspector.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 02.05.14
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
@Path( "dependencies" )
public class rest {

  @GET
  @Produces( MediaType.TEXT_PLAIN )
  public String serverinfo()
  {
    return "alle Dependencies";
  }



}

