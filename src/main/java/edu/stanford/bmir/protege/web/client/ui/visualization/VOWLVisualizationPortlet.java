package edu.stanford.bmir.protege.web.client.ui.visualization;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;


public class VOWLVisualizationPortlet extends AbstractOWLEntityPortlet {

	private VOWLVisualizationJso visualizationJso;

	public VOWLVisualizationPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override public void initialize() {
		Widget graphContainer = new HTML();
		graphContainer.getElement().setId(getContainerId());
		add(graphContainer);

		OntologyServiceManager.getInstance()
				.convertForVowlVisualization(getProjectId(), new AsyncCallback<String>() {
					@Override public void onFailure(Throwable caught) {
						// TODO
					}

					@Override public void onSuccess(String convertedOntology) {
						initializeVisualizationWhenElementExists(convertedOntology);
					}
				});
	}

	/**
	 * Temporary solution until we can find out when the graph container element is created.
	 */
	private void initializeVisualizationWhenElementExists(final String convertedOntology) {
		Timer timer = new Timer() {
			@Override public void run() {
				if (Document.get().getElementById(getContainerId()) != null) {
					cancel();
					visualizationJso = VOWLVisualizationJso.initialize(getContainerId(), convertedOntology);
				}
			}
		};

		timer.scheduleRepeating(100);
	}

	private String getContainerId() {
		/* The value has to begin with a letter to be valid for selecting the element.
		 * It is also important to use not only the project id, because this class is instantiated
		 * sometimes multiple times for the same project.
		 */
		return "project-id-" + getProjectId().getId() + "-hash-code-" + hashCode();
	}

}
