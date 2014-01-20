/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cedarsoft.maven.clashinspector.visualize.util;

import com.google.common.base.Strings;

/**
 * Copyright 2014 Behr Michael, Kampa Martin, Schneider Johannes, Zhu Huina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
