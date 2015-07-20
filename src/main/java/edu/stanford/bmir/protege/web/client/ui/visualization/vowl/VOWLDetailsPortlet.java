package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphListener;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphLoadedEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsAction;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsResult;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ValueDetails;

public class VOWLDetailsPortlet extends AbstractOWLEntityPortlet implements SelectionListener, GraphListener {

	private static final String DETAILS_TITLE = "Details";
	private VerticalPanel mainPanel;
	private VerticalPanel descriptionPanel;
	private VerticalPanel metadataPanel;
	private VerticalPanel statisticsPanel;
	//private FlexTable staticInfoPanel;
	private Grid staticInfoPanel;
	//private StackPanel dynamicInfoPanel;
	private DecoratedStackPanel dynamicInfoPanel;
	private JSONValue jsonValue;
	public static String ontologyAsJSONStr;
	public VOWLVisualizationJso visualizationJso;
	public static int count=0;
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
		//descriptionPanel.add(new HTML("<b>Description</b>"));

		metadataPanel = new VerticalPanel();
		//metadataPanel.add(new HTML("<b>Metadata</b>"));

		statisticsPanel = new VerticalPanel();
		//statisticsPanel.add(new HTML("<b>Statistics</b>"));


		dynamicInfoPanel = new DecoratedStackPanel();
		dynamicInfoPanel.setTitle("Dynamic panel");
		dynamicInfoPanel.add(descriptionPanel, "<h3>Description</h3>", true);
		dynamicInfoPanel.add(metadataPanel, "<h3>Metadata</h3>", true);
		dynamicInfoPanel.add(statisticsPanel, "<h3>Metrics</h3>", true);
		dynamicInfoPanel.showStack(2);
		//mainPanel.add(staticInfoPanel);
		//mainPanel.add(dynamicInfoPanel);

		add(mainPanel);

		/*DispatchServiceManager.get().execute(new GetDetailsAction(getProjectId()), new DispatchServiceCallback<GetDetailsResult>() {
			@Override
			public void handleSuccess(GetDetailsResult result) {
				ontologyAsJSONStr = result.getOntologyasJSONStr();
				GWT.log("[VOWL] Inside DetailsPortlet->handleSuccess");
			}
		});

		this.jsonValue = JSONParser.parseStrict(ontologyAsJSONStr);
		 */
	}


	@Override
	public void handleActivated() {
		super.handleActivated();
		GWT.log("[VOWL] Details portlet: I'm finally activated! Hooray!");
		//visualizationJso = VOWLVisualizationJso.getObject();

	}

	@Override
	protected void onRefresh() {

	}

	/**
	 * Create the Description item.
	 *
	 * @return the list of Description
	 */
	/*private void renderDynamicItems(Map<String, ValueDetails> details) {
		dynamicInfoPanel.add(new Label("TEST2.2"));
		VerticalPanel descPanel = new VerticalPanel();
		descPanel.setSpacing(4);
		//dynamicInfoPanel.add(new Label("TEST3.0: " + details.toString()));
		for(String key : details.keySet()) {
			ValueDetails valueDetails = details.get(key);
			if(valueDetails.getType().equals("String")) {
				dynamicInfoPanel.add(new Label("TEST3.1"));
				descPanel.add(new Label(valueDetails.getValue()));
			}
			else {
				dynamicInfoPanel.add(new Label("TEST3.2"));
				for (String desc : valueDetails.getArray()) {
					descPanel.add(new Label(desc));
				}
			}
		}

		dynamicInfoPanel.add(descPanel);

		//String[] str = {"Just", "Testing"};

	}*/


	private void renderDynamicItems(Map<String, ValueDetails> details) {
		dynamicInfoPanel.add(new Label("TEST2.2"));
		//VerticalPanel descPanel = new VerticalPanel();
		//descPanel.setSpacing(4);
		//dynamicInfoPanel.add(new Label("TEST3.0: " + details.toString()));
		dynamicInfoPanel.add(new Label("map is empty? "+details.isEmpty()));

		dynamicInfoPanel.add(new Label("map --> "+details.toString()));
		dynamicInfoPanel.add(new Label("  keys --> "+details.keySet().toString()));
		dynamicInfoPanel.add(new Label("  values --> "+details.values().toString()));
		//for(String key : details.keySet()) {
		//dynamicInfoPanel.add(new Label(key + ": " + details.get(key)));
		/*ValueDetails valueDetails = details.get(key);
			if(valueDetails.getType().equals("String")) {
				dynamicInfoPanel.add(new Label("TEST3.1"));
				//descPanel.add(new Label(valueDetails.getValue()));
			}
			else {
				dynamicInfoPanel.add(new Label("TEST3.2"));
				for (String desc : valueDetails.getArray()) {
					//descPanel.add(new Label(desc));
				}
			}*/
		//}

		//dynamicInfoPanel.add(descPanel);

		//String[] str = {"Just", "Testing"};

	}

	public void renderDetailsView(String elementId) {
		GetGraphSelectionDetailsAction action = new GetGraphSelectionDetailsAction(getProjectId(), elementId);
		DispatchServiceManager.get().execute(action, new DispatchServiceCallback<GetGraphSelectionDetailsResult>() {
			@Override
			public void handleSuccess(GetGraphSelectionDetailsResult result) {
				//to be tested
				//Map<String, ValueDetails> detailsMap = result.getDetailsMap();
				//It has to be a class that implements Serializable or a wrapper class that holds Serializable objects
				// source: http://www.gwtproject.org/doc/latest/DevGuideServerCommunication.html#DevGuideSerializableTypes

				//douleuei
				mainPanel.add(new Label("TEST2"));
				Map<String,ValueDetails> detailsMap = result.getDetailsMap();
				//null kapou edw
				//Window.alert(" map: "+ detailsMap.toString());
				String msg = detailsMap.size()==0?"empty":String.valueOf(detailsMap.size());
				mainPanel.add(new Label("TEST2.1 map size: " + msg));
				GWT.log("[VOWL] map: " + detailsMap.toString());
				//logger.log(Level.INFO, "[VOWL] DispatchServiceManager executed successfully.");
				//TODO set panel, widgets, etc

				dynamicInfoPanel.clear();

				// Add the Description item
				//mainPanel.remove(dynamicInfoPanel);

				//uparxei provlhma me auta, den ta kanei draw kai xalaei kai ta apo katw pou douleuoun
				//dynamicInfoPanel.add(createDescriptionItem(graphDetails.getMap()));
				//Window.alert("Egine add 1 item");
				//dynamicInfoPanel.add(createDescriptionItem(graphDetails.getMap()));

				//renderDynamicItems(detailsMap);
				//dynamicInfoPanel.add(new Label("map is empty? "+detailsMap.isEmpty()));

				//dynamicInfoPanel.add(new Label("map "+detailsMap.toString()));
				//dynamicInfoPanel.add(new Label("  keys "+detailsMap.keySet().toString()));
				//dynamicInfoPanel.add(new Label("  values "+detailsMap.values().toString()));

				for (String key : detailsMap.keySet()) {
					dynamicInfoPanel.add(createItem(detailsMap, key), key, false);

				}
				GWT.log("[VOWL] map keys: " + detailsMap.keySet().toString());
				GWT.log("[VOWL] map values: " + detailsMap.values().toString());


				mainPanel.add(dynamicInfoPanel);
				mainPanel.add(new Label("TEST4"));

				String str = "{\r\n" + 
						"    \"id\": 1,\r\n" + 
						"    \"name\": \"A green door\",\r\n" + 
						"    \"price\": 12.50,\r\n" + 
						"    \"tags\": [\"home\", \"green\"]\r\n" + 
						"}";

				JSONValue jsonObj = JSONParser.parseStrict(str);
				Double s = jsonObj.isObject().get("id").isNumber().doubleValue();
				GWT.log("[VOWL] id value of key 'id':"+ s);

			}
		});
		//}
	}

	private VerticalPanel createItem(Map<String, ValueDetails> map, String key) {
		VerticalPanel filtersPanel = new VerticalPanel();
		filtersPanel.setSpacing(4);
		filtersPanel.add(new Label(map.get(key).toString()));
		return filtersPanel;
	}

	private String getContainerId() {
		/* The value has to begin with a letter to be valid for selecting the element.
		 * It is also important to use not only the project id, because this class is instantiated
		 * sometimes multiple times for the same project.
		 */
		return "project-id-" + getProjectId().getId() + "-hash-code-" + hashCode();
	}

	/* ---- SelectionListener implementation method ---- 
	 * Called when notified by some Selectable object (e.g., a class, property, etc in the visualization portlet) that 
	 * their selections have been updated and that listeners should refresh content. */
	@Override
	public void selectionChanged(SelectionEvent event) {
		//mainPanel.clear();
		//mainPanel.add(new Label("count: "+ count++));
		//Window.alert("Selection is changed");



		Collection<? extends Object> selection = event.getSelectable().getSelection();

		if (selection.size() > 0) {
			Object selectionData = selection.iterator().next();
			if (selectionData instanceof String) {
				String selectedEntity = (String)selectionData;
				//Window.alert("Selection is changed: "+ selectedEntity);
				GWT.log("----> [VOWL] Selection is changed: "+ selectedEntity);

				//dynamicInfoPanel.clear();
				//mainPanel.remove(dynamicInfoPanel);
				//dynamicInfoPanel.remove(3);
				if(dynamicInfoPanel.getWidgetCount() == 4)
					dynamicInfoPanel.remove(3);
				dynamicInfoPanel.add(event.getSelectable().getPanel(), "<h3>Selection Details</h3>", true);
				dynamicInfoPanel.showStack(3);
				
				//int idx = mainPanel.getWidgetIndex(dynamicInfoPanel);


			}
		}

	}

	@Override
	public void graphLoaded(GraphLoadedEvent event) {
		// TODO Auto-generated method stub
		// edw na fortwnw ta static components

		visualizationJso = event.getLoadable().getVisualizationObject();
		GWT.log("[VOWL] Graph is loaded, IRI: "+ visualizationJso.getOntologyInfo().getIRI());
		if(!loaded)
			setDetailsStaticInfo();

	}

	public void setDetailsStaticInfo() {

		mainPanel.remove(staticInfoPanel);
		mainPanel.remove(dynamicInfoPanel);
		// Set up main panel
		//mainPanel = new VerticalPanel();  


		// Set up static info panel (contains name, IRI, version, author(s) and language)
		//staticInfoPanel = new Grid(5, 1);
		//staticInfoPanel.setCellSpacing(5);
		//staticInfoPanel.setCellPadding(3);
		//staticInfoPanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		//staticInfoPanel.setTitle(visualizationJso.getOntologyInfo().getTitle());
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


		//descriptionPanel = new VerticalPanel();
		//descriptionPanel.add(new HTML("<b>Description</b>"));
		descriptionPanel.add(new Label(visualizationJso.getOntologyInfo().getDescription()));

		metadataPanel.add(new HTML(visualizationJso.getOntologyInfo().getOther().getMetadataElements().join("<br>")));

		//statisticsPanel = new VerticalPanel();
		//statisticsPanel.add(new HTML("<b>Statistics</b>"));

		statisticsPanel.add(new HTML("Classes: <i>"+visualizationJso.getStatistics().getClassCount()+"</i>"));
		statisticsPanel.add(new HTML("Object prop.: <i>"+visualizationJso.getStatistics().getObjectPropertyCount()+"</i>"));
		statisticsPanel.add(new HTML("Datatype prop.: <i>"+visualizationJso.getStatistics().getDatatypePropertyCount()+"</i>"));
		statisticsPanel.add(new HTML("Individuals: <i>"+visualizationJso.getStatistics().getIndividualCount()+"</i>"));
		statisticsPanel.add(new HTML("Nodes: <i>"+visualizationJso.getStatistics().getNodeCount()+"</i>"));
		statisticsPanel.add(new HTML("Edges: <i>"+visualizationJso.getStatistics().getAxiomCount()+"</i>"));
		// Set up dynamic info panel
		/*dynamicInfoPanel = new DecoratedStackPanel();
		dynamicInfoPanel.setTitle("Dynamic panel");
		dynamicInfoPanel.add(descriptionPanel);
		dynamicInfoPanel.add(metadataPanel);
		dynamicInfoPanel.add(statisticsPanel);
		mainPanel.add(staticInfoPanel);
		mainPanel.add(dynamicInfoPanel);
		 */
		mainPanel.add(staticInfoPanel);
		mainPanel.add(dynamicInfoPanel);

		//add(mainPanel);

		loaded = true;
	}

}
