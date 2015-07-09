package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.client.JavaScriptObject;

public class VOWLElementJso extends JavaScriptObject {

	protected VOWLElementJso() {
	}

	public final native String getIri() /*-{
	  return this.iri();
  	}-*/;

	public final native String getLabel() /*-{
	  return this.labelForCurrentLanguage();
  	}-*/;

	public final native String getType() /*-{
	  return this.type();
  	}-*/;
	
	public final native String getCharacteristics() /*-{
	  return "";
	}-*/;
	
	public final native String getComment() /*-{
	  return (this.commentForCurrentLanguage() == null) ? "" : this.commentForCurrentLanguage();
	}-*/;
	
	public final native String getTermStatus() /*-{
	  return "";
	}-*/;

}
