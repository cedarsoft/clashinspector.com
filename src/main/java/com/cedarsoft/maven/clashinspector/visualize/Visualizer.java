
package com.cedarsoft.maven.clashinspector.visualize;

import com.cedarsoft.maven.clashinspector.model.ClashCollectResultWrapper;
import com.cedarsoft.maven.clashinspector.mojos.ClashSeverity;
import com.cedarsoft.maven.clashinspector.mojos.ClashTreeMojo;

public interface Visualizer {

  void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashTreeMojo clashTreeMojo );

}
