package edu.stanford.bmir.protege.web.client.ui.ontology.discussions;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.notes.DiscussionThreadPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.discussions.ExtendedClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * A single view that shows the classes in an ontology.
 * 
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class DiscussionsTab extends AbstractTab {

	private ExtendedClassTreePortlet clsTreePortlet;
	private DiscussionThreadPortlet discussionsThreadPorlet;


	public DiscussionsTab(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}


	/*@Override
	public void setup() {
		super.setup();

		clsTreePortlet = (ExtendedClassTreePortlet) getPortletByClassName(ExtendedClassTreePortlet.class.getName());
		discussionsThreadPorlet = (DiscussionThreadPortlet) getPortletByClassName(DiscussionThreadPortlet.class.getName());

		setControllingPortlet(clsTreePortlet);
	}*/

}
