/*
 * VowlVisualization.java
 *
 */

package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.client.JavaScriptObject;

public class VOWLVisualizationJso extends JavaScriptObject {

	protected VOWLVisualizationJso() {
	}

	public static native VOWLVisualizationJso initialize(String containerId, String data) /*-{
	  return $wnd.webvowlApp.app("#" + containerId, data).initialize();
  }-*/;

	public final native void setData(String data) /*-{
	  this.data(data);
  }-*/;
}
