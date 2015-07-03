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
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
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
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ConvertOntologyAction;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ConvertOntologyResult;


@SuppressWarnings("unchecked")
public class VOWLVisualizationPortlet extends AbstractOWLEntityPortlet implements Selectable {

	private static final String VOWL_TITLE = "WebVOWL 0.4.0";
	private VOWLVisualizationJso visualizationJso;
	public static String ontologyAsJSONStr;
	public static JSONValue jsonValue;
	private static Optional<String> selectedElement;
	private static Optional<String> elementType;

	// Listeners to selection events in this portlet
	private Collection<SelectionListener> listeners;

	private Widget graphContainer;
	private VerticalPanel detailsDynamicPanel;
	private Widget selectionDetailsContainer;

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
			}

			graphContainer.addDomHandler(new MyClickHandler(), ClickEvent.getType());
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
		if (selectedElement.isPresent())
			return Arrays.asList(selectedElement.get());
		else return Collections.emptyList();
	}

	//it can either be a property (i.e. label) or a class (i.e. node)
	private void setDetailsContent(String entityId) {
		JSONArray array;
		detailsDynamicPanel = new VerticalPanel();
		detailsDynamicPanel.setSpacing(4);

		String nameStr = "";
		String typeStr = "";;
		String equivalentStr = "";
		String disjointStr = "";
		String domainStr = "";
		String rangeStr = "";
		String inverseStr = "";
		String superPropertyStr = "";
		String characteristicsStr = "Charac.: ";
		String commentStr = "";
		String termStr = "term_status: ";

		//class
		if(elementType.get().equals("node")) {
			array = jsonValue.isObject().get("classAttribute").isArray();
			typeStr = findType(entityId, "class");
		}
		// property
		else { //if(elementType.get().equals("label"))
			array = jsonValue.isObject().get("propertyAttribute").isArray();
			typeStr = findType(entityId, "property");
		}


		boolean found = false;
		for (int i=0; i<array.size(); i++) {

			JSONObject o = array.get(i).isObject();
			if(o.get("id").isString().stringValue().equals(entityId)) {
				GWT.log("----> [VOWL] FOUND ID "+ entityId);

				found = true;

				// for classes and properties that have this key-value pair
				if(o.get("label").isObject().get("undefined") != null) {
					String name = o.get("label").isObject().get("undefined").isString().stringValue();
					if(elementType.get().equals("node")) {
						String iri= o.get("iri").isString().toString();
						nameStr = "Name: <a href=" + iri + ">" + name + "</a>";
						detailsDynamicPanel.add(new HTML(nameStr));
					}
					else {
						nameStr = "Name: " + name;
						detailsDynamicPanel.add(new Label(nameStr));
					}
				}
				// only for properties that have this key-pair
				else {
					String name = o.get("label").isObject().get("IRI-based").isString().stringValue();
					nameStr = "Name: " + name;
					detailsDynamicPanel.add(new Label(nameStr));
				}

				detailsDynamicPanel.add(new Label(typeStr));

				if(o.get("equivalent") != null) {
					equivalentStr = findRelatedEntities("equivalent", o, array);
					if(!equivalentStr.isEmpty()) {
						equivalentStr = "Equiv.:" + equivalentStr;
						detailsDynamicPanel.add(new HTML(equivalentStr));
					}
				}

				if(o.get("domain") != null) {
					//jsonValue.isObject().get("classAttribute").isArray()
					domainStr = findRelatedEntities("domain", o, array);
					if(!domainStr.isEmpty()) {
						domainStr = "Domain:" + domainStr;
						detailsDynamicPanel.add(new HTML(domainStr));
					}
				}

				if(o.get("range") != null) {
					rangeStr = findRelatedEntities("range", o, array);
					if(!rangeStr.isEmpty()) {
						rangeStr = "Range:" + rangeStr;
						detailsDynamicPanel.add(new HTML(rangeStr));
					}
				}

				if(o.get("inverse") != null) {
					inverseStr = findRelatedEntities("inverse", o, array);
					if(!inverseStr.isEmpty()) {
						inverseStr = "Inverse:" + inverseStr;
						detailsDynamicPanel.add(new HTML(inverseStr));
					}
				}

				if(o.get("superproperty") != null) {
					superPropertyStr = findRelatedEntities("superproperty", o, array);
					if(!superPropertyStr.isEmpty()) {
						//superPropertyStr = "Superprop:" + superPropertyStr;
						detailsDynamicPanel.add(new HTML("Superprop:" + superPropertyStr));
					}
				}

				if(o.get("attributes") != null) {
					JSONArray attributes = o.get("attributes").isArray();
					for(int j=0; j<attributes.size(); j++) {
						characteristicsStr += " " + attributes.get(j).isString().stringValue();
					}
					detailsDynamicPanel.add(new Label(characteristicsStr));
					if(o.get("comment") != null) {
						commentStr = "<i>" + o.get("comment").isObject().get("undefined").isString().stringValue() + "</i>";
						detailsDynamicPanel.add(new HTML(commentStr));
					}
				}

				if(o.get("annotations") != null && o.get("annotations").isObject().get("term_status") != null) {
					JSONArray term = o.get("annotations").isObject().get("term_status").isArray();
					for(int j=0; j < term.size(); j++) {
						termStr += "<i>" + term.get(j).isObject().get("value").isString().stringValue() + "</i>";
					}
					detailsDynamicPanel.add(new Label(termStr));
				}

			}
			if (found)
				break;
		}

	}

	private String findRelatedEntities(String relationshipKey, JSONObject o, JSONArray array) {
		String str="";
		GWT.log("----> [VOWL] FOUND "+ relationshipKey);
		if(o.get(relationshipKey).isArray() !=null) {
			JSONArray relArray = o.get(relationshipKey).isArray();
			for (int j=0; j<relArray.size(); j++) {
				String nameId = relArray.get(j).isString().stringValue();
				String[] name = findIRI(nameId, array);
				str += " <a href=" + name[1] + ">" + name[0] + "</a>";
			}
			GWT.log("\t-----> [VOWL] Returned related names: "+ str);

		}// does not work with owl:datatype properties (because range is a string and not an IRI)
		else if(o.get(relationshipKey).isString() !=null) {
			String nameId = o.get(relationshipKey).isString().stringValue();
			GWT.log("----> [VOWL] with id "+ nameId);
			String[] name = findIRI(nameId, jsonValue.isObject().get("classAttribute").isArray());
			GWT.log("\t-----> [VOWL] Returned related name: "+name[0]+ " iri "+ name[1]);
			str = " <a href=" + name[1] + ">" + name[0] + "</a>";
		}
		//}
		return str;
	}

	//need it when searching for IRIs of entities that relate with current entity 
	// e.g. disjointness, domain, range
	private String[] findIRI(String id, JSONArray array) {
		String name="";
		String iri="";

		boolean found = false;
		for (int i=0; i<array.size(); i++) {

			JSONObject o = array.get(i).isObject();
			if(o.get("id").isString().stringValue().equals(id)) {
				GWT.log("\t-----> [VOWL] FOUND related ID "+ id);

				found = true;
				if(o.get("label").isObject().get("undefined") != null)
					name = o.get("label").isObject().get("undefined").isString().stringValue();
				else 
					name = o.get("label").isObject().get("IRI-based").isString().stringValue();
				iri= o.get("iri").isString().toString();
				GWT.log("\t-----> [VOWL] Related name: "+name+ " iri "+ iri);
			}
			if (found)
				break;
		}
		return new String[] {name,iri};
	}

	private String findType(String id, String type) {
		boolean found = false;
		JSONArray array = jsonValue.isObject().get(type).isArray();
		String typeStr="";
		for (int i=0; i<array.size(); i++) {
			JSONObject o = array.get(i).isObject();
			if(o.get("id").isString().stringValue().equals(id)) {
				found = true;
				typeStr = "Type: " + o.get("type").isString().stringValue();
			}
			if(found) 
				break;
		}

		return typeStr;
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
					setDetailsContent(selectedElement.get());
					//Window.alert("<circle> or <rect> element with parent g id " + gElement.getParentElement().getId() + ", and class " + elementType.get() + " was clicked");
					notifySelectionListeners(new SelectionEvent(VOWLVisualizationPortlet.this));
				}
				//event.stopPropagation();
			}

		}

	}
}
