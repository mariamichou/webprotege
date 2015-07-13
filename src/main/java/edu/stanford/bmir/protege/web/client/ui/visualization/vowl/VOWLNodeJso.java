package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.client.JsArray;

public class VOWLNodeJso extends VOWLElementJso {
	protected VOWLNodeJso() {
	}

	public final native JsArray<VOWLNodeJso> getIndividuals() /*-{
	  return this.individuals();
	}-*/;
	

}
