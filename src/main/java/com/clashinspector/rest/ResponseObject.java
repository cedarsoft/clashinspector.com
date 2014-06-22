package com.clashinspector.rest;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 18.05.14
 * Time: 00:17
 * To change this template use File | Settings | File Templates.
 */
public class ResponseObject {

  private Object result;
  private UserParameterWrapper userParameterWrapper;

  public UserParameterWrapper getUserParameterWrapper() {
    return userParameterWrapper;
  }

  public void setUserParameterWrapper( UserParameterWrapper userParameterWrapper ) {
    this.userParameterWrapper = userParameterWrapper;
  }

  public Object getResult() {
    return result;
  }

  public void setResult( Object result ) {
    this.result = result;
  }


}
