package edu.stanford.bmir.protege.web.server.visualization.vowl;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import de.uni_stuttgart.vis.vowl.owl2vowl.Owl2Vowl;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class VisualizationAPI {

	//private ProjectId projectId;
	//private OWLAPIProjectManager projectManager;
	
	VisualizationAPI() {
	
	}
	
	public static String convertOntologyForVowlVisualization(ProjectId projectId, OWLAPIProjectManager projectManager) {
		
		OWLOntology ontology = projectManager.getProject(projectId).getRootOntology();
		OWLOntologyID id = ontology.getOntologyID();

		Owl2Vowl owl2Vowl = new Owl2Vowl(ontology, toName(id));

		return owl2Vowl.getJsonAsString();
	}

	private static String toName(OWLOntologyID id) {
		if (id.isAnonymous()) {
			return id.toString();
		}
		else {
			return id.getOntologyIRI().toString();
		}
	}
}
