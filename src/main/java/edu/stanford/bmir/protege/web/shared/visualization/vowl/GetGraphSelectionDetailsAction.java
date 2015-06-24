package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import com.google.gwt.json.client.JSONValue;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;

public class GetGraphSelectionDetailsAction implements Action<GetGraphSelectionDetailsResult> {
	
	private JSONValue selectedJSONValue;
	
	/**
     * For serialization purposes only
     */
	GetGraphSelectionDetailsAction() {
		
	}
	
	public GetGraphSelectionDetailsAction(JSONValue selectedJSONValue) {
		this.selectedJSONValue = selectedJSONValue;
	}
	
	public JSONValue getSelectedJSONValue() {
		return selectedJSONValue;
	}

}
