package edu.stanford.bmir.protege.web.server.visualization.vowl;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.inject.Inject;

import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsAction;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GetGraphSelectionDetailsResult;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GraphDetails;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ValueDetails;

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
		//JSONValue selectedJSONValue = action.getSelectedJSONValue();
		ProjectId projectId = action.getProjectId();
		
		//String convertedOntologyasJSON = VisualizationAPI.convertOntologyForVowlVisualization(projectId, projectManager);
		
		//test
		ArrayList<String> array = new ArrayList<String>();
		array.add("1");
		array.add("2");
		ValueDetails valueDetails = new ValueDetails(array);
		ValueDetails valueDetails2 = new ValueDetails("3");
		HashMap<String, ValueDetails> detailsMap = new HashMap<String, ValueDetails>();
		detailsMap.put("Maria", valueDetails);
		detailsMap.put("Michou", valueDetails2);
		GraphDetails graphDetails = new GraphDetails(detailsMap);
		
		//TODO: REMOVE null value from map and convert json object to a map and return it
		return new GetGraphSelectionDetailsResult(graphDetails);
	}

}
