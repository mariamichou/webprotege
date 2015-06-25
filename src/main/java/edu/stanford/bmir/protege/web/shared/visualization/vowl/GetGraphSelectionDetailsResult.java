package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

public class GetGraphSelectionDetailsResult implements Result {
	
	private GraphDetails graphDetails;
	
	/**
     * For serialization purposes only
     */
	GetGraphSelectionDetailsResult() {
		
	}
	
	public GetGraphSelectionDetailsResult(GraphDetails graphDetails) {
		
		this.graphDetails = graphDetails;
	} 
	
	public GraphDetails getGraphDetails() {
		return graphDetails;
	}
	
	/* to be tested
	public GetGraphSelectionDetailsResult(HashMap<String, ValueDetails> detailsMap) {
	
		this.detailsMap = detailsMap;
	}
	
	public HashMap<String, ValueDetails> getDetailsMap() {
		return detailsMap;
	}*/

}
