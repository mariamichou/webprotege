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
	  return (this.attributes() == null) ? "" : this.attributes();
	}-*/;
	
	public final native String getId() /*-{
	  return this.id();
	}-*/;
	
	public final native String getComment() /*-{
	  return (this.commentForCurrentLanguage() == null) ? "" : this.commentForCurrentLanguage();
	}-*/;
	
	// returns object
	public final native String getTermStatus() /*-{
	  var termAr = this.annotations();
	  if(termAr == null)
	  	return "";
	  return termAr.term_status;
	}-*/;
	
	public final native VOWLAnnotationJso getAnnotations(String property) /*-{
		return (this.annotations() == null) ? null : this.annotations()[property];
	}-*/;
	
}
