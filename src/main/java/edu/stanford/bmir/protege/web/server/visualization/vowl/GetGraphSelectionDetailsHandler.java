package edu.stanford.bmir.protege.web.server.visualization.vowl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
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
		ProjectId projectId = action.getProjectId();
		String entityId = action.getEntityId();
		
		String str = "{\r\n" + 
				"    \"id\": 1,\r\n" + 
				"    \"name\": \"A green door\",\r\n" + 
				"    \"price\": 12.50,\r\n" + 
				"    \"tags\": [\"home\", \"green\"]\r\n" + 
				"}";
		JSONObject jo = new JSONObject(str);
		String s = jo.get("id").toString();

		
		/*
		 Here we have to get the entityId and translate our converted ontology 
		 stored as a String serialization of a JSON object to a pruned JSON object 
		 and then to a GraphDetails object (essentially a map with data w.r.t 
		 the selected entity, node (i.e. class) or label (i.e. property).
		 So, instead of converting multiple times the ontology from String to JSON, 
		 we have to find a smart way to have our JSON object converted and 
		 stored statically *somewhere* (possibly client-side impl). Steps:
		 -- The first time visualization portlet loads the graph THEN ontology is 
		 converted and stored. Only the first time and onRefresh()!
		 -- Each time we select an entity (and Details portlet captures the 
		 onClick event, because it listens to changes), Details dispatcher passes the 
		 selected entityID (as string) and goes once again *somewhere* to set the 
		 pruned JSON on demand (right now it's done via the onClick event without 
		 the dispatcher, i.e. only client-side, not involving the server) and finally 
		 convert it to a Map<String,ValueDetails> object.
		 */
		
		// test, statically define a map (which ideally contains pruned JSON object)
		// i.e. selected entity's - important to us - data. 
		ArrayList<String> array = new ArrayList<String>();
		array.add("1");
		array.add("2");
		ValueDetails valueDetails = new ValueDetails(array);
		ValueDetails valueDetails2 = new ValueDetails("3."+s);
		Map<String, ValueDetails> detailsMap = new HashMap<String, ValueDetails>();
		detailsMap.put("Maria", valueDetails);
		detailsMap.put("Michou", valueDetails2);
		GraphDetails graphDetails = new GraphDetails(detailsMap);

		//TODO: Parse json string into a JSON object (server-side, HOW?)
		// currently it's done client-side, not sure if it's the most efficient
		// solution.
		return new GetGraphSelectionDetailsResult(detailsMap);
	}

}
