package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import com.google.common.base.Optional;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class GetGraphSelectionDetailsAction implements Action<GetGraphSelectionDetailsResult> {
	
	//private JSONValue selectedJSONValue;
	
	// important to show static data of the whole graph
	private ProjectId projectId;
	/*
	 *  Holds the id of the selected entity, e.g. "class13", "literal4", etc
	 *  as represented in the JSON converted ontology.
	 */
	private String entityId;
	
	/**
     * For serialization purposes only
     */
	GetGraphSelectionDetailsAction() {
		
	}
	
	/*public GetGraphSelectionDetailsAction(ProjectId projectId) {
		this.projectId = projectId;
	}*/
	
	public GetGraphSelectionDetailsAction(ProjectId projectId, String entityId) {
		this.projectId = projectId;
		//this.entityId = Optional.of(entityId);
		this.entityId = entityId;
	}
	
	public ProjectId getProjectId() {
		return projectId;
	}
	
	public String getEntityId() {
		return entityId;
	}
	
	/*
	public GetGraphSelectionDetailsAction(JSONValue selectedJSONValue) {
		this.selectedJSONValue = selectedJSONValue;
	}
	
	public JSONValue getSelectedJSONValue() {
		return selectedJSONValue;
	}*/

}
