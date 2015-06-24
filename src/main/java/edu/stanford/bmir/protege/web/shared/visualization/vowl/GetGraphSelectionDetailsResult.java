package edu.stanford.bmir.protege.web.shared.visualization.vowl;

import java.util.Map;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

public class GetGraphSelectionDetailsResult implements Result {
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	//private Map<String, ? extends Object> detailsMap;
	//just for testing purposes, still trying to find the error
	private String detailsMap;
	
	/**
     * For serialization purposes only
     */
	GetGraphSelectionDetailsResult() {
		
	}
	
	public GetGraphSelectionDetailsResult(String detailsMap) {
		this.detailsMap = detailsMap;
	}
	
	public String getDetailsMap() {
		return detailsMap;
	}
	
	/*public GetGraphSelectionDetailsResult(Map<String, ? extends Object> detailsMap) {
		this.detailsMap = detailsMap;
	}
	
	public Map<String, ? extends Object> getDetailsMap() {
		return detailsMap;
	}*/

}
