package edu.stanford.bmir.protege.web.client.ui.visualization;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

public class VisualizationPortlet extends AbstractOWLEntityPortlet {

	public VisualizationPortlet(Project project) {
		super(project);
	}

	@Override public void initialize() {
		Widget graphContainer = new HTML();
		graphContainer.getElement().setId("graph-container");
		add(graphContainer);

		injectD3JsAndWebVowl();
	}

	/**
	 * Injects the d3.js code.
	 */
	private void injectD3JsAndWebVowl() {
		ScriptInjector.fromUrl("js/webvowl/d3.min.js").setWindow(ScriptInjector.TOP_WINDOW)
				.setCallback(new Callback<Void, Exception>() {
					public void onFailure(Exception reason) {
						Window.alert("Script load failed.");
					}
					public void onSuccess(Void result) {
						injectWebVowlGraphAndApp();
					}
				}).inject();
	}

	/**
	 * Injects the WebVOWL graph code.
	 * Requires d3.js code to be injected previously with {@link #injectD3JsAndWebVowl()}.
	 */
	private void injectWebVowlGraphAndApp() {
		ScriptInjector.fromUrl("js/webvowl/webvowl.js").setWindow(ScriptInjector.TOP_WINDOW)
				.setCallback(new Callback<Void, Exception>() {
					public void onFailure(Exception reason) {
						Window.alert("Script load failed.");
					}
					public void onSuccess(Void result) {
						injectWebVowlApp();
					}
				}).inject();
	}

	/**
	 * Injects the WebVOWL app code.
	 * Requires the WebVOWL graph to be injected previously with {@link #injectWebVowlGraphAndApp()}.
	 */
	private void injectWebVowlApp() {
		ScriptInjector.fromUrl("js/webvowl/webvowl-app.js").setWindow(ScriptInjector.TOP_WINDOW)
				.setCallback(new Callback<Void, Exception>() {
					public void onFailure(Exception reason) {
						Window.alert("Script load failed.");
					}
					public void onSuccess(Void result) {
						initializeWebVowlApp();
					}
				}).inject();
	}

	/**
	 * Temporary solution until we can find out when the graph container element is created.
	 */
	native void initializeWebVowlApp() /*-{
	  var intervalId = $wnd.setInterval(function () {
		  if ($doc.getElementById("graph-container")) {
			  $wnd.webvowlApp.app().initialize();
			  $wnd.clearInterval(intervalId);
		  }
	  }, 100);
  }-*/;

}
