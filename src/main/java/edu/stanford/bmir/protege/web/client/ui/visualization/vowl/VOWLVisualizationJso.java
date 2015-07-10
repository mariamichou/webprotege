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

	/**
	 * If not compatible shows an error message in given container.
	 *
	 * @return True if not IE.
	 */
	public static native boolean isBrowserCompatible(String containerId) /*-{
	  var supported = $wnd.webvowlApp.browserWarning("#" + containerId).isSupported();
	  if (!supported) {
		  $wnd.webvowlApp.browserWarning("#" + containerId).showError();
	  }
	  return supported;
  }-*/;

	public final native VOWLDetailsJso getStatistics()  /*-{
	  return this.statistics();
  }-*/;
	
	public final native String getVersion()  /*-{
	  return this.version();
}-*/;
	
	public final native VOWLStaticDataJso getDescription()  /*-{
	  return this.desription();
}-*/;
	

	public final native VOWLNodeJso getSelectedNode() /*-{
	  return this.selectedNode();
  }-*/;

	public final native VOWLLabelJso getSelectedLabel() /*-{
	  return this.selectedLabel();
  }-*/;

	public static native void alert(String msg) /*-{
	  $wnd.alert(msg);
  }-*/;
}
