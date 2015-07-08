package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.client.JavaScriptObject;

public class VOWLDetailsJso extends JavaScriptObject {
	
	protected VOWLDetailsJso() {
	}
	
	public static native VOWLDetailsJso statistics(String containerId, String data) /*-{
	  return $wnd.webvowlApp.app("#" + containerId, data).stats();
	}-*/;
	
	public final native int getClassCount()  /*-{
		return this.classCount();
	}-*/;
	
	public final native int getObjectPropertyCount()  /*-{
		return this.objectPropertyCount();
	}-*/;
	
	public final native int getDatatypePropertyCount()  /*-{
		return this.datatypePropertyCount();
	}-*/;
	
	public final native int getIndividualCount()  /*-{
		return this.totalIndividualCount();
	}-*/;
	
	public final native int getNodeCount()  /*-{
		return this.nodeCount();
	}-*/;
	
	public final native int getEdgeCount()  /*-{
		return this.edgeCount();
	}-*/;
}
