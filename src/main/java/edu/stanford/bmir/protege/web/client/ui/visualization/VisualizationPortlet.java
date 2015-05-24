package edu.stanford.bmir.protege.web.client.ui.visualization;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

public class VisualizationPortlet extends AbstractOWLEntityPortlet {

	private static final String VOWL_GRAPH_ATTRIBUTE = "data-vowl-graph";

	public VisualizationPortlet(Project project) {
		super(project);
	}

	@Override public void initialize() {
		Widget graphContainer = new HTML();
		// Setting the id strangely doesn't work with multiple ontologies, so we select by an attribute
		graphContainer.getElement().setAttribute(VOWL_GRAPH_ATTRIBUTE, getVowlGraphAttributeValue());
		add(graphContainer);

		injectD3JsAndWebVowl();
	}

	private String getGraphContainerSelector() {
		return "[" + VOWL_GRAPH_ATTRIBUTE + "=" + getVowlGraphAttributeValue() + "]";
	}

	private String getVowlGraphAttributeValue() {
		// the value has to begin with a letter to be valid for selecting the element
		return "project-id-" + getProjectId().getId() + "-" + hashCode();
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
						initializeWebVowlApp(getGraphContainerSelector());
					}
				}).inject();
	}

	/**
	 * Temporary solution until we can find out when the graph container element is created.
	 */
	native void initializeWebVowlApp(String graphContainerSelector) /*-{
	  var intervalId = $wnd.setInterval(function () {
		  if ($doc.querySelector(graphContainerSelector)) {
			  $wnd.webvowlApp.app(graphContainerSelector).initialize();
			  $wnd.clearInterval(intervalId);
		  }
	  }, 100);
  }-*/;

}
