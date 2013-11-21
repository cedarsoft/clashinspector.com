package com.cedarsoft.hsrt.zkb.ourplugin;

import com.cedarsoft.hsrt.zkb.ourplugin.mojos.ClashTreeMojo;
import org.eclipse.aether.collection.CollectResult;

/**
 * Created with IntelliJ IDEA.
 * User: m
 * Date: 04.11.13
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public interface Visualizer {

       public void visualize(CollectResult collectResult, ClashTreeMojo clashTreeMojo )  ;

}
