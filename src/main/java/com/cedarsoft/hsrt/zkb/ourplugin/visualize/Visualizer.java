package com.cedarsoft.hsrt.zkb.ourplugin.visualize;

import com.cedarsoft.hsrt.zkb.ourplugin.model.ClashCollectResultWrapper;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashSeverity;
import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashTreeMojo;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 04.11.13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public interface Visualizer {

       public void visualize(ClashCollectResultWrapper clashCollectResultWrapper,ClashSeverity clashSeverity, ClashTreeMojo clashTreeMojo )  ;

}
