package com.clashinspector.mojos;


/*
 * The Clash Detection level is responsible for the detection of Version clashes. If the level is all then
 * there will be a clash if there are two different versions for the same dependency. If the level
 * is crtitical version clashes with higher and lower versions as the used version will be reported.
 * If the level is Fatal only the dependencies with higher version than the used one will be reported.
 */
public enum ClashSeverity {
  SAFE, UNSAFE, CRITICAL


}
