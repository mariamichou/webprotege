package edu.stanford.bmir.protege.web.client.ui.webvowl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
//import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface WebVowlResources extends ClientBundle {
	public static final WebVowlResources INSTANCE =  GWT.create(WebVowlResources.class);
	
	@Source("webvowl.html")
	public TextResource html();
}
