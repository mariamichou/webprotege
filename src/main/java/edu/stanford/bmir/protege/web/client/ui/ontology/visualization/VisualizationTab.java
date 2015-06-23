/*
 * VisualizationTab.java
 *
 */

package edu.stanford.bmir.protege.web.client.ui.ontology.visualization;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.visualization.vowl.VOWLDetailsPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.vowl.VOWLVisualizationPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Visualization tab usable for displaying visualization portlets.
 */
@SuppressWarnings("unchecked")
public class VisualizationTab extends AbstractTab {

	private VOWLVisualizationPortlet vowlVisualizationPortlet;
	private VOWLDetailsPortlet vowlDetailsPortlet;

	public VisualizationTab(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}

	@Override
	public void setup() {
		super.setup();    

		vowlDetailsPortlet = (VOWLDetailsPortlet) getPortletByClassName(VOWLDetailsPortlet.class.getName());
		vowlVisualizationPortlet = (VOWLVisualizationPortlet)getPortletByClassName(VOWLVisualizationPortlet .class.getName());

		// Hook up details portlet to listen to changes in visualization (graph) portlet
		vowlVisualizationPortlet.addSelectionListener(vowlDetailsPortlet);

	}
}
