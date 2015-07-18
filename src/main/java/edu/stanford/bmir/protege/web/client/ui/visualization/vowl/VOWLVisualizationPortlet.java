package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Optional;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphListener;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphLoadedEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.Loadable;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ConvertOntologyAction;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ConvertOntologyResult;


@SuppressWarnings("unchecked")
public class VOWLVisualizationPortlet extends AbstractOWLEntityPortlet implements Selectable, Loadable {

	private static final String VOWL_TITLE = "WebVOWL 0.4.0";
	private VOWLVisualizationJso visualizationJso;
	//private VOWLDetailsJso detailsJso;
	//private VOWLOntologyInfoJso ontologyInfoJso;
	public static String ontologyAsJSONStr;
	public static JSONValue jsonValue;
	private static Optional<String> selectedElement;
	private static Optional<String> elementType;

	// Listeners to selection events in this portlet
	private Collection<SelectionListener> listeners;
	
	// Listeners to graph loaded events in this portlet
	private Collection<GraphListener> graphListeners;

	private Widget graphContainer;
	private VerticalPanel detailsDynamicPanel;
	private Widget selectionDetailsContainer;

	public VOWLVisualizationPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
		this.listeners = new ArrayList<SelectionListener>();
		this.graphListeners = new ArrayList<GraphListener>();
	}

	@Override 
	public void initialize() {
		setTitle(VOWL_TITLE);
		//Widget graphContainer = new HTML();
		graphContainer = new HTML();

		graphContainer.getElement().setId(getContainerId());
		graphContainer.getElement().getStyle().setBackgroundColor("#ecf0f1");

		add(graphContainer);

	}

	@Override
	public void handleActivated() {
		super.handleActivated();
		GWT.log("[VOWL] I'm finally activated! Hooray!");
		initializeView();
	}

	public void initializeView() {
		// THIS runs when the project is loaded, i.e. long before a tab (portlet) is selected,
		// meaning that the graphContainer has not loaded the graph yet!
		DispatchServiceManager.get().execute(new ConvertOntologyAction(getProjectId()), new DispatchServiceCallback<ConvertOntologyResult>() {
			@Override
			public void handleSuccess(ConvertOntologyResult result) {
				ontologyAsJSONStr = result.getOntologyasJSONStr();
				initializeVisualizationWhenElementExists(ontologyAsJSONStr);
				setOntologyAsJSONString(ontologyAsJSONStr);
				jsonValue = JSONParser.parseStrict(ontologyAsJSONStr);
				//GWT.log("[VOWL] json: "+jsonValue);
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

		if (Document.get().getElementById(getContainerId()) != null) {

			if (VOWLVisualizationJso.isBrowserCompatible(getContainerId())) {
				visualizationJso = VOWLVisualizationJso.initialize(getContainerId(), convertedOntology);
				// graphContainer.getElement().getInnerHTML().isEmpty() = false
				// here we have to load static Details panel only once!
				// obviously we have to fire some kind of event for the Details portlet to take over.
				//detailsJso = visualizationJso.getStatistics();
				//ontologyInfoJso = visualizationJso.getOntologyInfo();
			}

			graphContainer.addDomHandler(new MyClickHandler(), ClickEvent.getType());
			
			notifyGraphListeners(new GraphLoadedEvent(VOWLVisualizationPortlet.this));
			
		}
	}


	@Override
	protected void onRefresh() {


		DispatchServiceManager.get().execute(new ConvertOntologyAction(getProjectId()), new DispatchServiceCallback<ConvertOntologyResult>() {
			@Override
			public void handleSuccess(ConvertOntologyResult result) {
				ontologyAsJSONStr = result.getOntologyasJSONStr();
				visualizationJso.setData(ontologyAsJSONStr);
				setOntologyAsJSONString(ontologyAsJSONStr);
				jsonValue = JSONParser.parseStrict(ontologyAsJSONStr);
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
		//infoPanel.setTitle("Info");
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
		if (selectedElement.isPresent())
			return Arrays.asList(selectedElement.get());
		else return Collections.emptyList();
	}

	@Override
	public void addGraphListener(GraphListener listener) {
		graphListeners.add(listener);
	}

	@Override
	public void notifyGraphListeners(GraphLoadedEvent event) {
		for (GraphListener listener: graphListeners) {
			listener.graphLoaded(new GraphLoadedEvent(this));
		}
		
	}

	@Override
	public void removeGraphListener(GraphListener listener) {
		graphListeners.remove(listener);
		
	}

	@Override
	public VOWLVisualizationJso getVisualizationObject() {
		return visualizationJso;
	}
	
	private void setDetailsContent() {
		detailsDynamicPanel = new VerticalPanel();
		detailsDynamicPanel.setSpacing(4);
		/*
		detailsDynamicPanel.add(new Label(visualizationJso.getOntologyInfo().getTitle()));
		
		detailsDynamicPanel.add(new HTML("<a href=\"" + visualizationJso.getOntologyInfo().getIRI() + "\">"+visualizationJso.getOntologyInfo().getIRI()+"</a>"));
		detailsDynamicPanel.add(new Label("Version: "+ visualizationJso.getOntologyInfo().getVersion()));
		
		GWT.log("[VOWL] authors: "+visualizationJso.getOntologyInfo().getAuthors().toString());
		detailsDynamicPanel.add(new Label("Author(s): "+ visualizationJso.getOntologyInfo().getAuthors().join()));
			
		
		String langStr="";
		for(int i=0; i< visualizationJso.getOntologyInfo().getLanguages().length(); i++) {
			langStr += "<option value=\""+visualizationJso.getOntologyInfo().getLanguages().get(i)+"\">"+visualizationJso.getOntologyInfo().getLanguages().get(i)+"</option>";
		}
		if(!langStr.isEmpty())
			detailsDynamicPanel.add(new HTML("Language: <select>"+ langStr + "</select>"));
		
		detailsDynamicPanel.add(new HTML("<b>Description</b>"));
		detailsDynamicPanel.add(new Label(visualizationJso.getOntologyInfo().getDescription()));
		
		detailsDynamicPanel.add(new HTML("<b>Metadata</b>"));
		detailsDynamicPanel.add(new Label(visualizationJso.getOntologyInfo().getOther()));
		
		
		detailsDynamicPanel.add(new HTML("<b>Statistics</b>"));
		
		detailsDynamicPanel.add(new HTML("Classes: <i>"+visualizationJso.getStatistics().getClassCount()+"</i>"));
		detailsDynamicPanel.add(new HTML("Object prop.: <i>"+visualizationJso.getStatistics().getObjectPropertyCount()+"</i>"));
		detailsDynamicPanel.add(new HTML("Datatype prop.: <i>"+visualizationJso.getStatistics().getDatatypePropertyCount()+"</i>"));
		detailsDynamicPanel.add(new HTML("Individuals: <i>"+visualizationJso.getStatistics().getIndividualCount()+"</i>"));
		detailsDynamicPanel.add(new HTML("Nodes: <i>"+visualizationJso.getStatistics().getNodeCount()+"</i>"));
		detailsDynamicPanel.add(new HTML("Edges: <i>"+visualizationJso.getStatistics().getAxiomCount()+"</i>"));
		*/
		
		detailsDynamicPanel.add(new HTML("<b>Selection Details</b>"));
		
		//TODO: add a loop
		if(elementType.get().equals("node")) {
			//GWT.log("[VOWL] selected node: "+ visualizationJso.getSelectedNode().getLabel());
			detailsDynamicPanel.add(new HTML("Name: <a href=\""+visualizationJso.getSelectedNode().getIri()+"\">"+visualizationJso.getSelectedNode().getLabel()+"</a>"));
			detailsDynamicPanel.add(new Label("Type: "+visualizationJso.getSelectedNode().getType()));
			//GWT.log("[VOWL] individuals: "+ visualizationJso.getSelectedNode().getIndividual().getAnnotation());
			
			if(visualizationJso.getSelectedNode().getIndividuals() != null) {
				String indStr = "";
				
				for(int i=0; i<visualizationJso.getSelectedNode().getIndividuals().length(); i++)  {
					
					indStr += "<a href=\""+visualizationJso.getSelectedNode().getIndividuals().get(i).getIri()+"\">"+visualizationJso.getSelectedNode().getIndividuals().get(i).getLabel()+"</a> ";
				}
				if(!indStr.isEmpty())
				detailsDynamicPanel.add(new HTML("Individuals: "+ indStr));
			}
			String charStr = visualizationJso.getSelectedNode().getCharacteristics();
			if(!charStr.isEmpty())
				detailsDynamicPanel.add(new Label("Char.: "+ charStr));
			String comment = visualizationJso.getSelectedNode().getComment();
			if(!comment.isEmpty())
				detailsDynamicPanel.add(new Label("Comment: "+comment));
			//String termStr = visualizationJso.getSelectedNode().getTermStatus();
			if(visualizationJso.getSelectedNode().getAnnotations("term_status") != null) {
				String termStr = visualizationJso.getSelectedNode().getAnnotations("term_status").getAnnotationProperty("value");
				if(!termStr.isEmpty())
					detailsDynamicPanel.add(new Label("term_status: "+ termStr));
			}
			
		}
		else {
			//GWT.log("[VOWL] selected label: "+ visualizationJso.getSelectedLabel().getDomain().getLabel());
			detailsDynamicPanel.add(new HTML("Name: <a href=\""+visualizationJso.getSelectedLabel().getIri()+"\">"+visualizationJso.getSelectedLabel().getLabel()+"</a>"));
			detailsDynamicPanel.add(new Label("Type: "+visualizationJso.getSelectedLabel().getType()));
			
			if(visualizationJso.getSelectedLabel().getInverse() != null)
				detailsDynamicPanel.add(new HTML("Inverse: <a href=\""+visualizationJso.getSelectedLabel().getInverse().getIri() + "\">" + visualizationJso.getSelectedLabel().getInverse().getLabel()+"</a>"));
			
			detailsDynamicPanel.add(new HTML("Domain: <a href=\""+visualizationJso.getSelectedLabel().getDomain().getIri()+"\">"+visualizationJso.getSelectedLabel().getDomain().getLabel()+"</a>"));
			detailsDynamicPanel.add(new HTML("Range: <a href=\""+visualizationJso.getSelectedLabel().getRange().getIri()+"\">"+visualizationJso.getSelectedLabel().getRange().getLabel()+"</a>"));
			
			if(visualizationJso.getSelectedLabel().getCardinality() != null)
				detailsDynamicPanel.add(new Label("Cardinality: "+visualizationJso.getSelectedLabel().getCardinality()));
			
			
			if(visualizationJso.getSelectedLabel().getSubproperties() != null) {
				String subs="";
				for(int i=0; i<visualizationJso.getSelectedLabel().getSubproperties().length(); i++) {
					subs += "<a href=\""+visualizationJso.getSelectedLabel().getSubproperties().get(i).getIri() + "\">" + visualizationJso.getSelectedLabel().getSubproperties().get(i).getLabel()+"</a> ";
				}
				if(!subs.isEmpty())
					detailsDynamicPanel.add(new HTML("Subprop.:"+subs));
			}
			if(visualizationJso.getSelectedLabel().getSuperproperties() != null) {
				String sups="";
				for(int i=0; i<visualizationJso.getSelectedLabel().getSuperproperties().length(); i++) {
					sups += "<a href=\""+visualizationJso.getSelectedLabel().getSuperproperties().get(i).getIri() + "\">" + visualizationJso.getSelectedLabel().getSuperproperties().get(i).getLabel()+"</a> ";
				}
				if(!sups.isEmpty())
					detailsDynamicPanel.add(new HTML("Superprop.:"+sups));
			}
			
			String charStr = visualizationJso.getSelectedLabel().getCharacteristics();
			if(!charStr.isEmpty())
				detailsDynamicPanel.add(new Label("Char.: "+ charStr));
			String comment = visualizationJso.getSelectedLabel().getComment();
			if(!comment.isEmpty())
				detailsDynamicPanel.add(new Label("Comment: "+comment));
			//String termStr = visualizationJso.getSelectedLabel().getTermStatus();
			if(visualizationJso.getSelectedLabel().getAnnotations("term_status") != null) {
				String termStr = visualizationJso.getSelectedLabel().getAnnotations("term_status").getAnnotationProperty("value");
				if(!termStr.isEmpty())
					detailsDynamicPanel.add(new Label("term_status: "+ termStr));
			}
		}
	}

	@Override
	public VerticalPanel getPanel() {
		return detailsDynamicPanel;
	}

	@Override
	public Widget getWidget() {
		return selectionDetailsContainer;
	}

	class MyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			{

				Element element=  event.getNativeEvent().getEventTarget().cast();
				if(element.getTagName().equals("circle") || element.getTagName().equals("rect")) {
					com.google.gwt.user.client.Element gElement = (com.google.gwt.user.client.Element)element.cast();
					selectedElement = Optional.of(gElement.getParentElement().getId());
					// classes have value 'node', while properties have value 'property' 
					elementType = Optional.of(gElement.getParentElement().getAttribute("class"));
					//setDetailsContent(selectedElement.get());
					//setDetailsContent(gElement.getParentElement());
					setDetailsContent();
					//Window.alert("<circle> or <rect> element with parent g id " + gElement.getParentElement().getId() + ", and class " + elementType.get() + " was clicked");
					notifySelectionListeners(new SelectionEvent(VOWLVisualizationPortlet.this));
				}
				//event.stopPropagation();
			}

		}

	}

}
