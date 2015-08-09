package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphListener;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphLoadedEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * This renders general details for the selected ontology and 
 * specific details for the selected node or label.
 * @author Maria Michou
 *
 */
public class VOWLDetailsPortlet extends AbstractOWLEntityPortlet implements SelectionListener, GraphListener {

	private static final String DETAILS_TITLE = "Details";
	private VerticalPanel mainPanel;
	private VerticalPanel descriptionPanel;
	private VerticalPanel metadataPanel;
	private VerticalPanel statisticsPanel;
	private Grid staticInfoPanel;
	private DecoratedStackPanel dynamicInfoPanel;
	public static String ontologyAsJSONStr;
	public VOWLVisualizationJso visualizationJso;
	private boolean loaded = false;

	public VOWLDetailsPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override
	public void initialize() {
		setTitle(DETAILS_TITLE);


		// Set up main panel
		mainPanel = new VerticalPanel(); 
		
		// Set up static info panel (contains name, IRI, version, author(s) and language)
		staticInfoPanel = new Grid(5, 1);
		staticInfoPanel.setCellSpacing(5);
		staticInfoPanel.setCellPadding(3);
		staticInfoPanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		descriptionPanel = new VerticalPanel();

		metadataPanel = new VerticalPanel();

		statisticsPanel = new VerticalPanel();

		dynamicInfoPanel = new DecoratedStackPanel();
		dynamicInfoPanel.setTitle("Dynamic panel");
		dynamicInfoPanel.add(descriptionPanel, "<h3>Description</h3>", true);
		dynamicInfoPanel.add(metadataPanel, "<h3>Metadata</h3>", true);
		dynamicInfoPanel.add(statisticsPanel, "<h3>Metrics</h3>", true);
		dynamicInfoPanel.showStack(2);

		add(mainPanel);
	}


	@Override
	public void handleActivated() {
		super.handleActivated();
		GWT.log("[VOWL] Details portlet status: Activated.");
	}

	@Override
	protected void onRefresh() {

	}

	private String getContainerId() {
		/* The value has to begin with a letter to be valid for selecting the element.
		 * It is also important to use not only the project id, because this class is instantiated
		 * sometimes multiple times for the same project.
		 */
		return "project-id-" + getProjectId().getId() + "-hash-code-" + hashCode();
	}

	/**
	 * SelectionListener implementation method. 
	 * Called when notified by some {@link edu.stanford.bmir.protege.web.client.ui.visualization.selection.Selectable} object 
	 * (e.g., a class, property, etc in the visualization portlet), that 
	 * their selections have been updated and that listeners should refresh content.
	 */
	@Override
	public void selectionChanged(SelectionEvent event) {

		Collection<? extends Object> selection = event.getSelectable().getSelection();
		
		if (selection.size() > 0) {
			Object selectionData = selection.iterator().next();
			if (selectionData instanceof String) {
				String selectedEntity = (String)selectionData;
				GWT.log("[VOWL] Selection is changed: "+ selectedEntity);

				renderDetailsDynamicInfo(dynamicInfoPanel, event.getSelectable().getPanel(), "<h3>Selection Details</h3>");
			}
		}
		 
	}

	/**
	 * This renders details for the selected element, either a node or a label. 
	 * @param containerPanel The main panel.
	 * @param vPanel The panel that describes the attributes of the selected element.
	 * @param header The header of the panel.
	 */
	public void renderDetailsDynamicInfo(DecoratedStackPanel containerPanel, VerticalPanel vPanel, String header) {
		if(containerPanel.getWidgetCount() == 4)
			containerPanel.remove(3);
		containerPanel.add(vPanel, header, true);
		containerPanel.showStack(3);

	}

	/**
	 * GraphListener implementation method.
	 * This is called when the graph is loaded in the Visualization portlet.
	 * It then passes the Visualization Javascript object to the Details portlet,
	 * which in turn, renders the corresponding details.
	 */
	@Override
	public void graphLoaded(GraphLoadedEvent event) {

		visualizationJso = event.getLoadable().getVisualizationObject();
		GWT.log("[VOWL] Graph status: Loaded.");
		if(!loaded)
			renderDetailsStaticInfo();
	}

	/**
	 * This renders general ontology data only the first time the graph is loaded 
	 * (and subsequently onRefresh() and when the Visualization tab is activated). 
	 */
	public void renderDetailsStaticInfo() {

		mainPanel.remove(staticInfoPanel);
		mainPanel.remove(dynamicInfoPanel);

		staticInfoPanel.setHTML(0, 0, "<h1>" + visualizationJso.getOntologyInfo().getTitle() + "</h1>");
		staticInfoPanel.setHTML(1, 0, "<a href=\"" + visualizationJso.getOntologyInfo().getIRI() + "\">"+visualizationJso.getOntologyInfo().getIRI()+"</a>");
		staticInfoPanel.setText(2, 0, "Version: "+String.valueOf(visualizationJso.getOntologyInfo().getVersion()));
		staticInfoPanel.setWidget(3, 0, new Label("Author(s): "+ visualizationJso.getOntologyInfo().getAuthors().join()));

		String langStr="";
		for(int i=0; i< visualizationJso.getOntologyInfo().getLanguages().length(); i++) {
			langStr += "<option value=\""+visualizationJso.getOntologyInfo().getLanguages().get(i)+"\">"+visualizationJso.getOntologyInfo().getLanguages().get(i)+"</option>";
		}
		if(!langStr.isEmpty())
			staticInfoPanel.setHTML(4, 0, 
					"Language: <select>" + 
							langStr +
					"</select>");


		descriptionPanel.add(new Label(visualizationJso.getOntologyInfo().getDescription()));

		metadataPanel.add(new HTML(visualizationJso.getOntologyInfo().getOther().getMetadataElements().join("<br>")));


		statisticsPanel.add(new HTML("Classes: <i>"+visualizationJso.getStatistics().getClassCount()+"</i>"));
		statisticsPanel.add(new HTML("Object prop.: <i>"+visualizationJso.getStatistics().getObjectPropertyCount()+"</i>"));
		statisticsPanel.add(new HTML("Datatype prop.: <i>"+visualizationJso.getStatistics().getDatatypePropertyCount()+"</i>"));
		statisticsPanel.add(new HTML("Individuals: <i>"+visualizationJso.getStatistics().getIndividualCount()+"</i>"));
		statisticsPanel.add(new HTML("Nodes: <i>"+visualizationJso.getStatistics().getNodeCount()+"</i>"));
		statisticsPanel.add(new HTML("Edges: <i>"+visualizationJso.getStatistics().getAxiomCount()+"</i>"));

		mainPanel.add(staticInfoPanel);
		mainPanel.add(dynamicInfoPanel);

		loaded = true;
	}

}
