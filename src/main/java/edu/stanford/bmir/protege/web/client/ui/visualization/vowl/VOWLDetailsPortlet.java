package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsAction;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsResult;

public class VOWLDetailsPortlet extends AbstractOWLEntityPortlet implements SelectionListener {

	private static final String DETAILS_TITLE = "Details";
	private VerticalPanel mainPanel;
	//private FlexTable staticInfoPanel;
	private Grid staticInfoPanel;
	private StackPanel dynamicInfoPanel;

	public VOWLDetailsPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override
	public void initialize() {
		setTitle(DETAILS_TITLE);

		/*Widget graphContainer = new HTML();
		graphContainer.getElement().setId(getContainerId());
		add(graphContainer);*/

		// Set up main panel
		mainPanel = new VerticalPanel();  
		
		// Set up static info panel
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
		dynamicInfoPanel = new DecoratedStackPanel();
		
		mainPanel.add(staticInfoPanel);
		mainPanel.add(dynamicInfoPanel);
		
		add(mainPanel);

		initializeView();
	}

	public void initializeView() {
		DispatchServiceManager.get().execute(new GetGraphSelectionDetailsAction(JSONParser.parseStrict(VOWLVisualizationPortlet.ontologyAsJSONStr)), new DispatchServiceCallback<GetGraphSelectionDetailsResult>() {
			@Override
			public void handleSuccess(GetGraphSelectionDetailsResult result) {
				Map<String, ? extends Object> detailsMap = result.getDetailsMap();
				//TODO set panel, widgets, etc
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
		//TODO
		Collection<? extends Object> selection = event.getSelectable().getSelection();
		/*if (selection.size() > 0) {
			Object selectionData = selection.iterator().next();
			if (selectionData instanceof String) {
				String odpUri = (String)selectionData;

				DispatchServiceManager.get().execute(new GetOdpDetailsAction(odpUri), new DispatchServiceCallback<GetOdpDetailsResult>() {
					@Override
					public void handleSuccess(GetOdpDetailsResult result) {
						odp = result.getDetails();
						renderOdpDetails(odp);
					}
				});
			}
		}*/
	}

}
