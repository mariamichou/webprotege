package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.client.JsArray;

public class VOWLNodeJso extends VOWLElementJso {
	protected VOWLNodeJso() {
	}

	public final native JsArray<VOWLElementJso> getIndividuals() /*-{
	  return this.individuals();
	}-*/;
	
	
	public final native String getAnnotation() /*-{
	  //$wnd.alert(JSON.stringify(this.annotations) + " "+JSON.stringify(this.annotations[p]));
	  //var an =  this["annotations"];
	  
	  $wnd.alert(JSON.stringify(this));
	  return "";
	 // var labelAr = an["label"];
	 // var label = labelAr[0];
	 // return label[0].value;
	}-*/;
}
