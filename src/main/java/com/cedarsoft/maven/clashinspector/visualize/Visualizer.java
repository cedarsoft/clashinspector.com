package com.cedarsoft.maven.clashinspector.visualize;

import com.cedarsoft.maven.clashinspector.model.ClashCollectResultWrapper;
import com.cedarsoft.maven.clashinspector.mojos.ClashSeverity;
import com.cedarsoft.maven.clashinspector.mojos.ClashTreeMojo;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 04.11.13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public interface Visualizer {

  void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity, ClashTreeMojo clashTreeMojo );

}
