package com.clashinspector.visualize.util;

import com.google.common.base.Strings;


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
