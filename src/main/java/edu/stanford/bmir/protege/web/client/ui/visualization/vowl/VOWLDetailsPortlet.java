package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphListener;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphLoadedEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * This renders general details for the selected ontology.
 * @author Maria Michou
 *
 */
public class VOWLDetailsPortlet extends AbstractOWLEntityPortlet implements GraphListener {

	private static final String DETAILS_TITLE = " Ontology Details";
	
	private VOWLDetailsViewPresenter presenter;
	private VOWLDetailsView view;
	private boolean loaded = false;
	
	public VOWLDetailsPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override
	public void initialize() {
		setLayout(new FitLayout());
		setTitle(DETAILS_TITLE);
		view = new VOWLDetailsViewImpl();
		
		//presenter = new VOWLDetailsViewPresenter(getProjectId(), view);
        //add(presenter.getWidget());
		add(view.getWidget());
        
	}
	
	
	/**
	 * SelectionListener implementation method. 
	 * Called when notified by some {@link edu.stanford.bmir.protege.web.client.ui.visualization.selection.Selectable} object 
	 * (e.g., a class, property, etc in the visualization portlet), that 
	 * their selections have been updated and that listeners should refresh content.
	 */
	/*@Override
	public void selectionChanged(SelectionEvent event) {

		Collection<? extends Object> selection = event.getSelectable().getSelection();
		
		if (selection.size() > 0) {
			Object selectionData = selection.iterator().next();
			if (selectionData instanceof String) {
				String selectedEntity = (String)selectionData;
				GWT.log("[VOWL] Selection is changed: "+ selectedEntity);

				view.renderDetailsDynamicInfo(event.getSelectable().getPanel(), "<h3>Selection Details</h3>");
			}
		}
		 
	}*/
	
	/**
	 * GraphListener implementation method.
	 * This is called when the graph is loaded in the Visualization portlet.
	 * It then passes the Visualization Javascript object to the Details portlet,
	 * which in turn, renders the corresponding details.
	 */
	@Override
	public void graphLoaded(GraphLoadedEvent event) {

		VOWLVisualizationJso visualizationJso = event.getLoadable().getVisualizationObject();
		GWT.log("[VOWL] Graph status: Loaded.");
		if(!loaded) {
			view.renderDetailsStaticInfo(visualizationJso);
			loaded = true;
		}
	}


	@Override
	public void handleActivated() {
		super.handleActivated();
		GWT.log("[VOWL] Ontology Details portlet status: Activated.");
	}

	@Override
	protected void onRefresh() {

	}	

}