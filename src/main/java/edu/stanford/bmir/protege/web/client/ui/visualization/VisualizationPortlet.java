package edu.stanford.bmir.protege.web.client.ui.visualization;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
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

		OntologyServiceManager.getInstance()
				.convertOntology(getProjectId(), new AsyncCallback<String>() {
					@Override public void onFailure(Throwable caught) {
						// TODO
					}

					@Override public void onSuccess(String convertedOntology) {
						initializeWebVowlApp(getGraphContainerSelector(), convertedOntology);
					}
				});
	}

	private String getGraphContainerSelector() {
		return "[" + VOWL_GRAPH_ATTRIBUTE + "=" + getVowlGraphAttributeValue() + "]";
	}

	private String getVowlGraphAttributeValue() {
		// the value has to begin with a letter to be valid for selecting the element
		return "project-id-" + getProjectId().getId() + "-" + hashCode();
	}

	/**
	 * Temporary solution until we can find out when the graph container element is created.
	 */
	native void initializeWebVowlApp(String graphContainerSelector, String convertedOntology) /*-{
	  var intervalId = $wnd.setInterval(function () {
		  if ($doc.querySelector(graphContainerSelector)) {
			  $wnd.webvowlApp.app(graphContainerSelector, convertedOntology).initialize();
			  $wnd.clearInterval(intervalId);
		  }
	  }, 100);
  }-*/;

}
