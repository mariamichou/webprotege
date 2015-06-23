package edu.stanford.bmir.protege.web.server.visualization.vowl;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Inject;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsAction;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsResult;

public class GetGraphSelectionDetailsHandler
		implements
		ActionHandler<GetGraphSelectionDetailsAction, GetGraphSelectionDetailsResult> {

	private OWLAPIProjectManager projectManager;
	
	@Inject
	public GetGraphSelectionDetailsHandler(OWLAPIProjectManager projectManager) {
		this.projectManager = projectManager;
	}
	
	@Override
	public Class<GetGraphSelectionDetailsAction> getActionClass() {
		return GetGraphSelectionDetailsAction.class;
	}

	@Override
	public RequestValidator<GetGraphSelectionDetailsAction> getRequestValidator(
			GetGraphSelectionDetailsAction action, RequestContext requestContext) {
		return NullValidator.get();
	}

	@Override
	public GetGraphSelectionDetailsResult execute(
			GetGraphSelectionDetailsAction action,
			ExecutionContext executionContext) {
		JSONValue selectedJSONValue = action.getSelectedJSONValue();
		GWT.log("[MESSAGE] ****************** inside Handler's execute ***********************");
		
		Map<String, Object> detailsMap = new HashMap<String, Object>();
		detailsMap.put("key", "value");
		//TODO: REMOVE null value from map and convert json object to a map and return it
		return new GetGraphSelectionDetailsResult(detailsMap);
	}

}
