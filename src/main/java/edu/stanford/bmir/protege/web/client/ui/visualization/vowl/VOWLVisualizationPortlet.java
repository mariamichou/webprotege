package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.ArrayList;
import java.util.Collection;

import org.semanticweb.owlapi.model.OWLEntity;

import com.google.common.base.Optional;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.*;


@SuppressWarnings("unchecked")
public class VOWLVisualizationPortlet extends AbstractOWLEntityPortlet implements Selectable {

	private static final String VOWL_TITLE = "WebVOWL 0.4.0";
	private VOWLVisualizationJso visualizationJso;
	public static String ontologyAsJSONStr;

	// Listeners to selection events in this portlet
	private Collection<SelectionListener> listeners;

	private Widget graphContainer;

	public VOWLVisualizationPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
		this.listeners = new ArrayList<SelectionListener>();
	}

	@Override 
	public void initialize() {
		setTitle(VOWL_TITLE);
		//Widget graphContainer = new HTML();
		graphContainer = new HTML();

		graphContainer.getElement().setId(getContainerId());
		graphContainer.getElement().getStyle().setBackgroundColor("#ecf0f1");

		add(graphContainer);
		
		initializeView();
		
	}

	public void initializeView() {
		// THIS runs when the project is loaded, i.e. long before a tab (portlet) is selected,
		// meaning that the graphContainer has not loaded the graph yet!
		DispatchServiceManager.get().execute(new ConvertOntologyAction(getProjectId()), new DispatchServiceCallback<ConvertOntologyResult>() {
			@Override
			public void handleSuccess(ConvertOntologyResult result) {
				String ontologyAsJSONStr = result.getOntologyasJSONStr();
				initializeVisualizationWhenElementExists(ontologyAsJSONStr);
				setOntologyAsJSONString(ontologyAsJSONStr);
			}
		});

	}


	String getOntologyAsJSONString() {
		return VOWLVisualizationPortlet.ontologyAsJSONStr;
	}

	void setOntologyAsJSONString(String ontologyAsJSONStr) {
		VOWLVisualizationPortlet.ontologyAsJSONStr = ontologyAsJSONStr;
	}

	/**
	 * This is *actually* initialized when the Visualization tab is selected and not before!
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
					
					/*graphContainer.addHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							Element element=  event.getNativeEvent().getEventTarget().cast();
							if(element.getTagName().equals("circle") || element.getTagName().equals("rect")) {
								com.google.gwt.user.client.Element gElement = (com.google.gwt.user.client.Element)element.cast();
								Window.alert("<circle> or <rect> element with parent g id " + gElement.getParentElement().getId() + " was clicked");
							}
						}
					}, ClickEvent.getType());
					*/
					HandlerRegistration hr = graphContainer.addDomHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							Element element=  event.getNativeEvent().getEventTarget().cast();
							if(element.getTagName().equals("circle") || element.getTagName().equals("rect")) {
								com.google.gwt.user.client.Element gElement = (com.google.gwt.user.client.Element)element.cast();
								Window.alert("<circle> or <rect> element with parent g id " + gElement.getParentElement().getId() + " was clicked");
							}
						}
					}, ClickEvent.getType());
					 
					//hr.removeHandler();
					// edw exei fortw8ei o container kai to dom tree, alla de mporw na paw pio va8ia kai na kanw inject clickandler 
					// sta tags <g> pou perikleioun mesa tous ta <circle> kai <rect>
					// prepei na kanw capture ta events sta <g> tags a posteriori, dld. afou exei fortw8ei o graph container
					// de mporw na to 3erw a priori auto, giati einai html widget kai fortwnetai to periexomeno tou dynamically
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

	/* ---- Selectable implementation methods by Karl Hammar ----*/
	@Override
	public void notifySelectionListeners(final SelectionEvent selectionEvent) {
		for (SelectionListener listener: listeners) {
			listener.selectionChanged(new SelectionEvent(this));
		}
	}

	@Override
	public void addSelectionListener(SelectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeSelectionListener(SelectionListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setSelection(Collection<? extends Object> selection) {
		// We don't allow external sources to modify the selection of this portlet.
	}

	@Override
	public Collection<? extends Object> getSelection() {
		return null;
	}

}
