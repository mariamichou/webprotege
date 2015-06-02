package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.Label;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.AbstractValidatableTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;


public class VOWLVisualizationPortlet extends AbstractOWLEntityPortlet {

	private VOWLVisualizationJso visualizationJso;
	protected static final String vowlTitle = "VOWL";

	public VOWLVisualizationPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override public void initialize() {
		setTitle(vowlTitle);
		Widget graphContainer = new HTML();
		graphContainer.getElement().setId(getContainerId());
		add(graphContainer);

		OntologyServiceManager.getInstance()
				.convertForVowlVisualization(getProjectId(), new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO
					}

					@Override
					public void onSuccess(String convertedOntology) {
						initializeVisualizationWhenElementExists(convertedOntology);
					}
				});
	}

	/**
	 * Temporary solution until we can find out when the graph container element is created.
	 */
	private void initializeVisualizationWhenElementExists(final String convertedOntology) {
		Timer timer = new Timer() {
			@Override
			public void run() {
				if (Document.get().getElementById(getContainerId()) != null) {
					cancel();
					if (VOWLVisualizationJso.isBrowserCompatible(getContainerId())) {
						visualizationJso = VOWLVisualizationJso.initialize(getContainerId(), convertedOntology);
					}
				}
			}
		};

		timer.scheduleRepeating(100);
	}

	@Override
	protected void onRefresh() {
		OntologyServiceManager.getInstance()
				.convertForVowlVisualization(getProjectId(), new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO
					}

					@Override
					public void onSuccess(String convertedOntology) {
						visualizationJso.setData(convertedOntology);
					}
				});
	}

	private String getContainerId() {
		/* The value has to begin with a letter to be valid for selecting the element.
		 * It is also important to use not only the project id, because this class is instantiated
		 * sometimes multiple times for the same project.
		 */
		return "project-id-" + getProjectId().getId() + "-hash-code-" + hashCode();
	}

	@Override
	protected TabPanel getConfigurationPanel() {
		TabPanel configurationPanel = super.getConfigurationPanel();
		// Add new tab to gear window.
		configurationPanel.add(createVOWlPanel());
		return configurationPanel;
	}

	/**
	 * @return New panel for the information tab in the gear window.
	 */
	protected Panel createVOWlPanel() {

		// TODO think of correct class
		Panel generalPanel = new AbstractValidatableTab() {
			@Override
			public void onSave() {
			}

			@Override
			public boolean isValid() {
				return true;
			}
		};

		generalPanel.setTitle("Info");
		generalPanel.setPaddings(10);
		generalPanel.add(new Label("WebVOWL Version 0.4.0"));
		generalPanel.add(new HTML("<a target=\"_blank\" href=\"http://vowl.visualdataweb.org/\">Check visualdataweb</a>"));

		return generalPanel;
	}
}
