package com.clashinspector.exceptions;

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
