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
	 * @return True if not IE.
	 */
	public static native boolean isBrowserCompatible(String containerId) /*-{
		var supported = $wnd.webvowlApp.browserWarning("#" + containerId).isSupported();
		if (!supported) {
			$wnd.webvowlApp.browserWarning("#" + containerId).showError();
		}
		return supported;
	}-*/;
	
	public static native int statistics(String containerId, String data)  /*-{
		var count = $wnd.webvowlApp.app("#" + containerId, data).getClassCount();
		
		return count || 0;
	}-*/;
}
