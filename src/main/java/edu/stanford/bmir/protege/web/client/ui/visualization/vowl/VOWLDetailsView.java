package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public interface VOWLDetailsView {

	void renderDetailsDynamicInfo(VerticalPanel vPanel, String header);
	void renderDetailsStaticInfo(VOWLVisualizationJso visualizationJso);
	Widget getWidget();
	
}
