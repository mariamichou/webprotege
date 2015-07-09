package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsAction;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsResult;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ValueDetails;

public class VOWLDetailsPortlet extends AbstractOWLEntityPortlet implements SelectionListener {

	private static final String DETAILS_TITLE = "Details";
	private VerticalPanel mainPanel;
	//private FlexTable staticInfoPanel;
	private Grid staticInfoPanel;
	//private StackPanel dynamicInfoPanel;
	private DecoratedStackPanel dynamicInfoPanel;
	private JSONValue jsonValue;
	public static String ontologyAsJSONStr;
	public static int count=0;

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
		staticInfoPanel.setTitle("Ontology Name");
		staticInfoPanel.setHTML(1, 0, "<a id=\"about\" href=\"http://xmlns.com/foaf/0.1/\" target=\"_blank\">http://xmlns.com/foaf/0.1/</a>");
		staticInfoPanel.setText(2, 0, "Version: ");
		staticInfoPanel.setWidget(3, 0, new Label("Author(s): "));
		staticInfoPanel.setHTML(4, 0, 
				"<label>\r\n" + 
						"Language:\r\n" + 
						"<select id=\"language\" size=\"1\" name=\"language\">\r\n" + 
						"<option value=\"IRI-based\">IRI-based</option>\r\n" + 
						"<option value=\"undefined\">undefined</option>\r\n" + 
						"</select>\r\n" + 
				"</label>");


		// Set up dynamic info panel
		//dynamicInfoPanel = new StackPanel();
		dynamicInfoPanel = new DecoratedStackPanel();
		dynamicInfoPanel.setTitle("Dynamic panel");
		//dynamicInfoPanel.setWidth("200px");


		mainPanel.add(staticInfoPanel);
		//renderDetailsView(Optional.<String>absent());
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

				dynamicInfoPanel.clear();
				mainPanel.remove(dynamicInfoPanel);
				dynamicInfoPanel.add(event.getSelectable().getPanel(), "<h3></h3>", true);
				mainPanel.add(dynamicInfoPanel);
				//renderDetailsView(selectedEntity);
			}
		}

	}

}
