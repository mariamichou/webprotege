package edu.stanford.bmir.protege.web.client.ui.visualization;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

public class VisualizationPortlet extends AbstractOWLEntityPortlet {

	public VisualizationPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override
	public void initialize() {
		Widget graphContainer = new HTML();
		graphContainer.getElement().setId(getGraphContainerId());
		add(graphContainer);

		OntologyServiceManager.getInstance()
				.convertForVowlVisualization(getProjectId(), new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO
					}

					@Override
					public void onSuccess(String convertedOntology) {
						initializeWebVowlApp(getGraphContainerSelector(), convertedOntology);
					}
				});
	}

	private String getGraphContainerSelector() {
		return "#" + getGraphContainerId();
	}

	private String getGraphContainerId() {
		/* The value has to begin with a letter to be valid for selecting the element.
		 * It is also important to use not only the project id, because this class is instantiated
		 * sometimes multiple times for the same project.
		 */
		return "project-id-" + getProjectId().getId() + "-hash-code-" + hashCode();
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
