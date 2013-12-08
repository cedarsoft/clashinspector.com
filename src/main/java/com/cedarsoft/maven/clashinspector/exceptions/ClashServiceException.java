package com.cedarsoft.maven.clashinspector.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 18.11.13
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class ClashServiceException extends Exception {

  public ClashServiceException() {
  }

  public ClashServiceException( String message ) {
    super( message );
  }

  public ClashServiceException( String message, Throwable cause ) {
    super( message, cause );
  }

  public ClashServiceException( Throwable cause ) {
    super( cause );
  }

  public ClashServiceException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
    super( message, cause, enableSuppression, writableStackTrace );
  }
}
