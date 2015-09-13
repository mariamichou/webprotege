package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

@SuppressWarnings("unchecked")
public class VOWLControlPortlet extends AbstractOWLEntityPortlet {
	
	private static final String CONTROL_TITLE = "Control Bar";
	private HorizontalPanel controlContainer;
	private VerticalPanel pausePanel;
	
	public VOWLControlPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override
	public void initialize() {
		setTitle(CONTROL_TITLE);
		controlContainer = new HorizontalPanel();
		//controlContainer.add(pausePanel);
		add(controlContainer);
	}
	
	@Override
	public void handleActivated() {
		super.handleActivated();
		GWT.log("[VOWL] Control portlet status: Activated.");
	}
	
	

}
