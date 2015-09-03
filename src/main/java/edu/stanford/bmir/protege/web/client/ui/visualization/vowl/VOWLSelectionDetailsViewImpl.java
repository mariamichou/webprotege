package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VOWLSelectionDetailsViewImpl extends Composite implements VOWLSelectionDetailsView {

	private static VOWLSelectionDetailsViewImplUiBinder uiBinder = GWT
			.create(VOWLSelectionDetailsViewImplUiBinder.class);

	public static String ontologyAsJSONStr;

	private HTMLPanel rootElement;

	//protected DecoratedStackPanel dynamicInfoPanel;

	protected VerticalPanel selectionDetailsPanel;

	interface VOWLSelectionDetailsViewImplUiBinder extends
	UiBinder<HTMLPanel, VOWLSelectionDetailsViewImpl> {
	}

	public VOWLSelectionDetailsViewImpl() {
		//initWidget(uiBinder.createAndBindUi(this));

		rootElement = uiBinder.createAndBindUi(this);
		initWidget(rootElement);

		

		selectionDetailsPanel = new VerticalPanel();
		//rootElement.add(selectionDetailsPanel);
		/*dynamicInfoPanel = new DecoratedStackPanel();
		dynamicInfoPanel.setTitle("Dynamic panel");
		dynamicInfoPanel.add(descriptionPanel, "<h3>Description</h3>", true);
		dynamicInfoPanel.add(metadataPanel, "<h3>Metadata</h3>", true);
		dynamicInfoPanel.add(statisticsPanel, "<h3>Metrics</h3>", true);
		dynamicInfoPanel.showStack(2);*/
	}


	/**
	 * This renders details for the selected element, either a node or a label. 
	 * @param vPanel The panel that describes the attributes of the selected element.
	 * @param header The header of the panel.
	 */
	public void renderDetailsDynamicInfo(VerticalPanel vPanel, String header) {
		//selectionDetailsPanel.clear();
		if(rootElement.getWidgetCount() > 0)
			rootElement.remove(0);
		rootElement.add(vPanel);
		/*if(dynamicInfoPanel.getWidgetCount() == 1)
			dynamicInfoPanel.remove(0);
		dynamicInfoPanel.add(vPanel, header, true);*/
		//dynamicInfoPanel.showStack(3);

	}
	
	@Override
	public Widget getWidget() {
		return this;
	}

}
