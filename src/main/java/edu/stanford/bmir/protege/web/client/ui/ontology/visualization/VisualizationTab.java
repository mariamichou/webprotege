/*
 * VisualizationTab.java
 *
 */

package edu.stanford.bmir.protege.web.client.ui.ontology.visualization;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Visualization tab usable for displaying visualization portlets.
 */
public class VisualizationTab extends AbstractTab {
	public VisualizationTab(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
	}
}
