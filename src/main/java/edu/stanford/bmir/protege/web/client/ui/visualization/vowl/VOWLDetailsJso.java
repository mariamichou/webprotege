package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.client.JavaScriptObject;

public class VOWLDetailsJso extends JavaScriptObject {

	protected VOWLDetailsJso() {
	}

	public final native int getClassCount()  /*-{
	  return this.classCount;
  }-*/;

	public final native int getDatatypeCount() /*-{
	  return this.datatypeCount;
  }-*/;

	public final native int getPropertyCount() /*-{
	  return this.propertyCount;
  }-*/;

	public final native int getObjectPropertyCount()  /*-{
	  return this.objectPropertyCount;
  }-*/;

	public final native int getDatatypePropertyCount()  /*-{
	  return this.datatypePropertyCount;
  }-*/;

	public final native int getNodeCount()  /*-{
	  return this.nodeCount;
  }-*/;

	public final native int getAxiomCount()  /*-{
	  return this.axiomCount;
  }-*/;

	public final native int getIndividualCount() /*-{
	  return this.individualCount;
  }-*/;
}
