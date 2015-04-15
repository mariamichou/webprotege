package edu.stanford.bmir.protege.web.client.ui.webvowl;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

@SuppressWarnings("unchecked")
public class WebVowlPortlet extends AbstractOWLEntityPortlet {

	public WebVowlPortlet(Project project) {
		super(project);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize() {
		
		// Inject WebVOWL JavaScript
		ScriptInjector.fromUrl("js/webvowl/d3.min.js").setWindow(ScriptInjector.TOP_WINDOW).setCallback(
				new Callback<Void, Exception>() {
					public void onFailure(Exception reason) {
						Window.alert("Script load failed.");
					}
					public void onSuccess(Void result) {
						//Window.alert("Script load success.");
					}
				}).inject();

		ScriptInjector.fromUrl("js/webvowl/webvowl.js").setWindow(ScriptInjector.TOP_WINDOW).setCallback(
				new Callback<Void, Exception>() {
					public void onFailure(Exception reason) {
						Window.alert("Script load failed.");
					}
					public void onSuccess(Void result) {
						//Window.alert("Script load success.");
					}
				}).inject();
		
		ScriptInjector.fromUrl("js/webvowl/webvowl-app.js").setWindow(ScriptInjector.TOP_WINDOW).setCallback(
				new Callback<Void, Exception>() {
					public void onFailure(Exception reason) {
						Window.alert("Script load failed.");
					}
					public void onSuccess(Void result) {
						//Window.alert("Script load success.");
					}
				}).inject();
		
		
		
		Panel p = new Panel();
		p.add(new Button("Initialize WebVOWL",new ButtonListenerAdapter() {
			public void onClick(final Button button, final EventObject e) {
				reloadWebVowl();
			}
		}));
		p.add(new Button("Load FOAF",new ButtonListenerAdapter() {
			public void onClick(final Button button, final EventObject e) {
				loadFoaf();
			}
		}));
		
		String webVowlHtml = WebVowlResources.INSTANCE.html().getText();
		HTMLPanel html = new HTMLPanel(webVowlHtml);
		p.add(html);
		
		add(p);
	}
	
    // JSNI call to reload WebVowl interface
    native void reloadWebVowl() /*-{
    	$wnd.webvowlApp.version = "0.3.3";
    	$wnd.webvowlApp.app().initialize();
    }-*/;
    
    native void loadFoaf() /*-{
	$wnd.webvowlApp.loadOntology("/js/data/foaf.json");
}-*/;
}
