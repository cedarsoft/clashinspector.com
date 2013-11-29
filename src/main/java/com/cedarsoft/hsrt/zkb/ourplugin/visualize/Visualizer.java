package com.cedarsoft.hsrt.zkb.ourplugin.visualize;

import com.cedarsoft.hsrt.zkb.ourplugin.model.ClashCollectResultWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.AbstractClashMojo;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashTreeMojo;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 04.11.13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public interface Visualizer {

  void visualize( ClashCollectResultWrapper clashCollectResultWrapper, AbstractClashMojo.ClashDetectionLevel clashDetectionLevel, ClashTreeMojo clashTreeMojo );

}
