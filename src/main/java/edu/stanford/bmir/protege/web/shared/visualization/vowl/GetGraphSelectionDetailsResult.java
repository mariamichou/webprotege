package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import java.util.Map;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

public class GetGraphSelectionDetailsResult implements Result {
	
	private Map<String, ? extends Object> detailsMap;
	
	GetGraphSelectionDetailsResult() {
		
	}
	
	public GetGraphSelectionDetailsResult(Map<String, ? extends Object> detailsMap) {
		this.detailsMap = detailsMap;
	}
	
	public Map<String, ? extends Object> getDetailsMap() {
		return detailsMap;
	}

}
