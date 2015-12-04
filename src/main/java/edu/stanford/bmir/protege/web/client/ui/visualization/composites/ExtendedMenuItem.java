package edu.stanford.bmir.protege.web.client.ui.visualization.composites;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedMenuItem extends MenuItem implements ClickHandler, KeyPressHandler {
	
	//private TextBox textBox = new TextBox();
	private TextBox textBox;
	private CheckBox checkBox = new CheckBox();
	private VerticalPanel panel;
	//private VerticalPanel panel = new VerticalPanel();
	private Button button = new Button();
	private FlowPanel fPanel;
	//Slider classDistanceSlider = new Slider("Class distance");  
	//Slider classDistanceSlider = new Slider("Class distance", 10, 600, 200);
	
	
	public ExtendedMenuItem(String html, boolean htmlON, ScheduledCommand cmd) {
	    super(html, htmlON, cmd);
	 // Place the check above the text box using a vertical panel.
	 		//panel.add(checkBox);
	    panel = new VerticalPanel();
	    textBox = new TextBox();
	    	panel.add(textBox);
	 		//panel.add(button);
	 		textBox.setTitle(html);
	 		textBox.setValue("200");
	 		//button.setText("Ok");
	 		//panel.add(classDistanceSlider);
	 		
	
	 		// Set the check box's caption, and check it by default.
	 		//checkBox.setText(html);
	 		//checkBox.setChecked(true);
	 		//checkBox.addClickHandler(this);
	 		//textBox.addClickHandler(this);
	 		textBox.addKeyPressHandler(this);
	 		// All composites must call initWidget() in their constructors.
	 		//initWidget(panel);

	 		// Give the overall composite a style name.
	 		setStyleName("example-OptionalCheckBox");
	  }
	
	
	public ExtendedMenuItem(String html, boolean htmlON, ScheduledCommand cmd, boolean checked) {
	    super(html, htmlON, cmd);
	 // Place the check above the text box using a vertical panel.
	    panel = new VerticalPanel();
	    textBox = new TextBox();
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
	
	private class MyPopup extends PopupPanel {

	    public MyPopup() {
	      // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
	      // If this is set, the panel closes itself automatically when the user
	      // clicks outside of it.
	      super(true);

	      // PopupPanel is a SimplePanel, so you have to set it's widget property to
	      // whatever you want its contents to be.
	      setWidget(new Label("Click outside of this popup to close it"));
	      //setWidget(textBox);
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


	@Override
	public void onKeyPress(KeyPressEvent event) {
		Object sender = event.getSource();
		
		if (sender == textBox && event.getUnicodeCharCode() == 0) {
			
			//Window.alert("You pressed ENTER");
			
			panel.setVisible(false);
			// When the check box is clicked, update the text box's enabled state.
			//textBox.setEnabled(checkBox.isChecked());
		}
		
	}
	
	public void setParentPanel(FlowPanel fPanel) {
		this.fPanel = fPanel;
	}
	
	public void unsetPanel(Widget w) {
		this.fPanel.remove(w);
	}
	
}
