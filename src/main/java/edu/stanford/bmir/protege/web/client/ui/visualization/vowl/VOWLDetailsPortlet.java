package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
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
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GraphDetails;

public class VOWLDetailsPortlet extends AbstractOWLEntityPortlet implements SelectionListener {

	private static final String DETAILS_TITLE = "Details";
	private VerticalPanel mainPanel;
	//private FlexTable staticInfoPanel;
	private Grid staticInfoPanel;
	//private StackPanel dynamicInfoPanel;
	private DecoratedStackPanel dynamicInfoPanel;

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
		renderDetailsView(Optional.<String>absent());
	}

	/**
	 * Create the Description item.
	 *
	 * @return the list of Description
	 */
	private VerticalPanel createDescriptionItem() {
		VerticalPanel descPanel = new VerticalPanel();
		descPanel.setSpacing(4);
		String[] str = {"Just", "Testing"};
		for (String desc : str) {
			descPanel.add(new Label(desc));
		}
		return descPanel;
	}

	public void renderDetailsView(Optional<String> elementId) {
		//JSONValue value = JSONParser.parseStrict(VOWLVisualizationPortlet.ontologyAsJSONStr);

		GetGraphSelectionDetailsAction action;
		
		if(elementId.isPresent())
			action = new GetGraphSelectionDetailsAction(getProjectId(), elementId.get());
		else
			action = new GetGraphSelectionDetailsAction(getProjectId());
		DispatchServiceManager.get().execute(action, new DispatchServiceCallback<GetGraphSelectionDetailsResult>() {
			@Override
			public void handleSuccess(GetGraphSelectionDetailsResult result) {

				//to be tested
				//Map<String, ValueDetails> detailsMap = result.getDetailsMap();
				//It has to be a class that implements Serializable or a wrapper class that holds Serializable objects
				// source: http://www.gwtproject.org/doc/latest/DevGuideServerCommunication.html#DevGuideSerializableTypes
				GraphDetails graphDetails = result.getGraphDetails();
				//logger.log(Level.INFO, "[MICHOU] DispatchServiceManager executed successfully.");
				//TODO set panel, widgets, etc

				// Add the Description item
				dynamicInfoPanel.add(createDescriptionItem(), "Description");

				// Add the Metadata item
				//dynamicInfoPanel.add(new Label("Metadata"), "mtd");
				dynamicInfoPanel.add(createDescriptionItem(), "Metadata");

				// Add the Statistics item
				dynamicInfoPanel.add(createDescriptionItem(), "Statistics");

				// Add the Selection Details item
				dynamicInfoPanel.add(createDescriptionItem(), "Selection Details");

				mainPanel.add(staticInfoPanel);
				mainPanel.add(dynamicInfoPanel);

				add(mainPanel);
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

	/* ---- SelectionListener implementation method ---- 
	 * Called when notified by some Selectable object (e.g., a class, property, etc in the visualization portlet) that 
	 * their selections have been updated and that listeners should refresh content. */
	@Override
	public void selectionChanged(SelectionEvent event) {
		//TODO to change the dynamic panel, only this will change
		//Collection<? extends Object> selection = event.getSelectable().getSelection();
		//String selectedEntity = (String)event.getSelectable().getSelection();

		GWT.log("@@@@@@@@@@@ [MICHOU] Selection changed inside VOWLDetails portlet");
		
		//renderDetailsView(Optional.of(selectedEntity));
		
	}

}
