package com.cedarsoft.maven.clashinspector.visualize.util;

import com.google.common.base.Strings;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 10.01.14
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleVisualizeHelper {

  private static final  int commandLineLength =    79   ;




  public enum LogLevel
  {
    INFO(7),ERROR(8),WARN(10)  ;

     private int logLength;

    private LogLevel (int logLength)
    {
      this.logLength = logLength;
    }

    private int getLogLength()
    {
      return this.logLength;
    }

  }

   public static String createSectionHeader(String text,LogLevel logLevel)
   {

      int startValue = commandLineLength - logLevel.getLogLength();
      int a = startValue - text.length();
      int b = a / 2;

      return Strings.repeat("-",b) + text + Strings.repeat("-",b);

   }

}
