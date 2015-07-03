package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import java.util.Map;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

public class GetGraphSelectionDetailsResult implements Result {
	
	//private GraphDetails graphDetails;
	private Map<String,ValueDetails> detailsMap;
	
	/**
     * For serialization purposes only
     */
	GetGraphSelectionDetailsResult() {
		
	}
	
	// to be tested
	public GetGraphSelectionDetailsResult(Map<String, ValueDetails> detailsMap) {
	
		this.detailsMap = detailsMap;
	}
	
	public Map<String, ValueDetails> getDetailsMap() {
		return detailsMap;
	}
	
	/*public GetGraphSelectionDetailsResult(GraphDetails graphDetails) {
	
	this.graphDetails = graphDetails;
	} 

	public GraphDetails getGraphDetails() {
		return graphDetails;
	}*/

}
