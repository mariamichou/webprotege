package edu.stanford.bmir.protege.web.client.ui.visualization.composites;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExtendedMenuItem extends MenuItem implements ClickHandler {
	
	private TextBox textBox = new TextBox();
	private CheckBox checkBox = new CheckBox();
	private VerticalPanel panel = new VerticalPanel();
	
	public ExtendedMenuItem(String html, boolean htmlON, ScheduledCommand cmd) {
	    super(html, htmlON, cmd);
	 // Place the check above the text box using a vertical panel.
	 		panel.add(checkBox);
	 		panel.add(textBox);

	 		// Set the check box's caption, and check it by default.
	 		checkBox.setText(html);
	 		checkBox.setChecked(true);
	 		//checkBox.addClickHandler(this);
	 		textBox.addClickHandler(this);
	 		// All composites must call initWidget() in their constructors.
	 		//initWidget(panel);

	 		// Give the overall composite a style name.
	 		setStyleName("example-OptionalCheckBox");
	  }
	
	
	public ExtendedMenuItem(String html, boolean htmlON, ScheduledCommand cmd, boolean checked) {
	    super(html, htmlON, cmd);
	 // Place the check above the text box using a vertical panel.
	 		panel.add(checkBox);
	 		checkBox.setText(html);
	 		checkBox.setChecked(checked);
	 		checkBox.addClickHandler(this);

	 		// All composites must call initWidget() in their constructors.
	 		//initWidget(panel);

	 		// Give the overall composite a style name.
	 		setStyleName("example-OptionalCheckBox");
	  }
	
	public void onClick(ClickEvent event) {
		
		//new MyPopup().show();
		
		Object sender = event.getSource();
		
		/*
		if (sender == checkBox) {
			// When the check box is clicked, update the text box's enabled state.
			textBox.setEnabled(checkBox.isChecked());
		}
		*/
		
		if (sender instanceof CheckBox) {
			// When the check box is clicked, update the text box's enabled state.
			if(checkBox.getValue())
				Window.alert("Checked");
			else
				Window.alert("Checked");
		}
	}
	
	private static class MyPopup extends PopupPanel {

	    public MyPopup() {
	      // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
	      // If this is set, the panel closes itself automatically when the user
	      // clicks outside of it.
	      super(true);

	      // PopupPanel is a SimplePanel, so you have to set it's widget property to
	      // whatever you want its contents to be.
	      setWidget(new Label("Click outside of this popup to close it"));
	    }
	  }

	/**
	 * Sets the caption associated with the check box.
	 * 
	 * @param caption the check box's caption
	 */
	public void setCaption(String caption) {
		// Note how we use the use composition of the contained widgets to provide
		// only the methods that we want to.
		checkBox.setText(caption);
	}

	/**
	 * Gets the caption associated with the check box.
	 * 
	 * @return the check box's caption
	 */
	public String getCaption() {
		return checkBox.getText();
	}
	
	public VerticalPanel getPanel() {
		return panel;
	}
	
	
}
