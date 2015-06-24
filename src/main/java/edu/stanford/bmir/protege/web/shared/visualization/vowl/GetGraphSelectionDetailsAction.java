package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class GetGraphSelectionDetailsAction implements Action<GetGraphSelectionDetailsResult> {
	
	//private JSONValue selectedJSONValue;
	private ProjectId projectId;
	
	/**
     * For serialization purposes only
     */
	GetGraphSelectionDetailsAction() {
		
	}
	
	public GetGraphSelectionDetailsAction(ProjectId projectId) {
		this.projectId = projectId;
	}
	
	public ProjectId getProjectId() {
		return projectId;
	}
	
	/*
	public GetGraphSelectionDetailsAction(JSONValue selectedJSONValue) {
		this.selectedJSONValue = selectedJSONValue;
	}
	
	public JSONValue getSelectedJSONValue() {
		return selectedJSONValue;
	}*/

}
