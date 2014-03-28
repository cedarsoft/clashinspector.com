
package com.clashinspector.visualize;

import com.clashinspector.model.ClashCollectResultWrapper;
import com.clashinspector.mojos.ClashSeverity;
import com.clashinspector.mojos.ClashTreeMojo;

public interface Visualizer {

  void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashTreeMojo clashTreeMojo );

}
