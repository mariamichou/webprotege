package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.form.Label;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.*;


@SuppressWarnings("unchecked")
public class VOWLVisualizationPortlet extends AbstractOWLEntityPortlet {

	private static final String VOWL_TITLE = "WebVOWL 0.4.0";
	private VOWLVisualizationJso visualizationJso;

	public VOWLVisualizationPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override 
	public void initialize() {
		setTitle(VOWL_TITLE);
		Widget graphContainer = new HTML();
		graphContainer.getElement().setId(getContainerId());
		add(graphContainer);
		
		initializeView();
	}
	
	public void initializeView() {
		DispatchServiceManager.get().execute(new ConvertOntologyAction(getProjectId()), new DispatchServiceCallback<ConvertOntologyResult>() {
			@Override
			public void handleSuccess(ConvertOntologyResult result) {
				String ontologyAsJSONStr = result.getOntologyasJSONStr();
				initializeVisualizationWhenElementExists(ontologyAsJSONStr);
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
		
		
		DispatchServiceManager.get().execute(new ConvertOntologyAction(getProjectId()), new DispatchServiceCallback<ConvertOntologyResult>() {
			@Override
			public void handleSuccess(ConvertOntologyResult result) {
				String ontologyAsJSONStr = result.getOntologyasJSONStr();
				visualizationJso.setData(ontologyAsJSONStr);
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
		configurationPanel.add(createVOWLPanel());
		return configurationPanel;
	}

	/**
	 * @return New panel for the information tab in the gear window.
	 */
	protected Panel createVOWLPanel() {
		Panel infoPanel = new Panel();
		infoPanel.setTitle("Info");
		infoPanel.setPaddings(10);
		infoPanel.add(new Label("Visualized with WebVOWL 0.4.0 ("));
		infoPanel.add(new Anchor("VOWL homepage", "http://vowl.visualdataweb.org/", "_blank"));
		infoPanel.add(new Label(")"));

		return infoPanel;
	}
}
