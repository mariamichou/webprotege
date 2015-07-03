package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.GraphDetails;
import edu.stanford.bmir.protege.web.shared.visualization.vowl.ValueDetails;

// currently not usable
public class VisualizationAPI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private ProjectId projectId;
	//private OWLAPIProjectManager projectManager;
	private String jsonString;
	private ProjectId projectId;
	private OWLAPIProjectManager projectManager;
	
	VisualizationAPI() {

	}
	
	public void setProjectId(ProjectId projectId) {
		this.projectId = projectId;
	}
	
	public void setProjectManager(OWLAPIProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public VisualizationAPI(ProjectId projectId, OWLAPIProjectManager projectManager) {
		this.projectId = projectId;
		this.projectManager = projectManager;
		OWLOntology ontology = projectManager.getProject(projectId).getRootOntology();
		OWLOntologyID id = ontology.getOntologyID();

		Owl2Vowl owl2Vowl = new Owl2Vowl(ontology, toName(id));
		this.jsonString = owl2Vowl.getJsonAsString();
	}
	
	private void convertOntologyForVowlVisualization(ProjectId projectId, OWLAPIProjectManager projectManager) {

		OWLOntology ontology = projectManager.getProject(projectId).getRootOntology();
		OWLOntologyID id = ontology.getOntologyID();

		Owl2Vowl owl2Vowl = new Owl2Vowl(ontology, toName(id));

		this.jsonString = owl2Vowl.getJsonAsString();
	}
	
	private String toName(OWLOntologyID id) {
		if (id.isAnonymous()) {
			return id.toString();
		}
		else {
			return id.getOntologyIRI().toString();
		}
	}

	/*public static String convertOntologyForVowlVisualization(ProjectId projectId, OWLAPIProjectManager projectManager) {

		OWLOntology ontology = projectManager.getProject(projectId).getRootOntology();
		OWLOntologyID id = ontology.getOntologyID();

		Owl2Vowl owl2Vowl = new Owl2Vowl(ontology, toName(id));

		return owl2Vowl.getJsonAsString();
	}

	private String toName(OWLOntologyID id) {
		if (id.isAnonymous()) {
			return id.toString();
		}
		else {
			return id.getOntologyIRI().toString();
		}
	}
*/

	public JSONValue getJSONValue() {
		//convertOntologyForVowlVisualization(projectId, projectManager);
		Window.alert("json string empty: "+ this.jsonString.isEmpty());
		
		return JSONParser.parseStrict(this.jsonString);
	}

	public GraphDetails getPrunedJSONObject(String elementId) {
		JSONObject jsonObject = getJSONValue().isObject();
		Window.alert("JSON Object is null "+jsonObject.isNull());
		
		HashMap<String, ValueDetails> detailsMap = new HashMap<String, ValueDetails>();
		ArrayList<String> array = new ArrayList<String>();
		String value="";
		array.add("1");
		array.add("2");

		// static panel
		String title = jsonObject.get("description").isObject().get("undefined").isString().stringValue();
		ValueDetails staticDetails = new ValueDetails(title);
		detailsMap.put("title", staticDetails);

		/*ValueDetails valueDetails = new ValueDetails(array);
		ValueDetails valueDetails2 = new ValueDetails("3");
		detailsMap.put("Maria", valueDetails);
		detailsMap.put("Michou", valueDetails2);
		 */
		GraphDetails graphDetails = new GraphDetails(detailsMap);

		return graphDetails;
	}

}
