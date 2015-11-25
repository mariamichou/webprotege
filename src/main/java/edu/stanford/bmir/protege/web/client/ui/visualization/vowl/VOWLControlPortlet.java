package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.composites.ExtendedMenuItem;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import com.smartgwt.client.widgets.Slider;

@SuppressWarnings("unchecked")
public class VOWLControlPortlet extends AbstractOWLEntityPortlet implements Selectable {

	private static final String CONTROL_TITLE = "Control Bar";
	private MenuBar controlBar;
	// Listeners to selection events in this portlet
	private Collection<SelectionListener> listeners;
	private boolean paused = false;
	private String option = null;
	private Collection<? extends Object> selection;
	private FlowPanel panel;
	private ExtendedMenuItem cmi, dataProp, solSub, disjInfo, setOp, pickPin, nodeScale, compNotation;
	private Command cmd;

	public VOWLControlPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
		this.listeners = new ArrayList<SelectionListener>();
	}

	@Override
	public void initialize() {
		setTitle(CONTROL_TITLE);
		panel = new FlowPanel();
		controlBar = new MenuBar(false);
		controlBar.setAutoOpen(true);
		panel.add(controlBar);
		createMenuItems();
		add(panel);
	}

	private void addSelectedComponent(ExtendedMenuItem item){
		//Window.alert("Is cmi null? "+cmi==null?"NULL":"NOT NULL: "+cmi.getCaption());
		panel.add(item.getPanel());
	}


	private void createMenuItems() {
		controlBar.moveSelectionDown();

		MenuBar exportMenu = new MenuBar(true);
		controlBar.addItem("Export", exportMenu);
		controlBar.addSeparator();

		MenuBar gravityMenu = new MenuBar(true);
		controlBar.addItem("Gravity", gravityMenu);
		controlBar.addSeparator();

		MenuBar filterMenu = new MenuBar(true);
		controlBar.addItem("Filter", filterMenu);
		controlBar.addSeparator();

		MenuBar modesMenu = new MenuBar(true);
		controlBar.addItem("Modes", modesMenu);
		controlBar.addSeparator();

		//MenuBar resetMenu = new MenuBar(true);
		controlBar.addItem("Reset", new Command() {
			public void execute() {
				//Window.alert("You selected Reset!");
				selection = Arrays.asList("reset");
				GWT.log("[VOWL] Visualization graph, reset.");
				notifySelectionListeners(new SelectionEvent(VOWLControlPortlet.this));
			}
		});
		controlBar.addSeparator();

		//MenuBar pauseMenu = new MenuBar(true);
		controlBar.addItem("Pause", new Command() {
			public void execute() {
				if(paused) {
					selection = Arrays.asList(false);

				}
				else {
					paused = true;
					selection = Arrays.asList(true);
				}
				notifySelectionListeners(new SelectionEvent(VOWLControlPortlet.this));
				//Window.alert("You selected Pause!");
			}
		});
		controlBar.addSeparator();

		MenuBar aboutMenu = new MenuBar(true);
		controlBar.addItem("About", aboutMenu);

		exportMenu.addItem("Export as SVG", new Command() {
			public void execute() {
				;
			}
		});
		exportMenu.addItem("Export as JSON", new Command() {
			public void execute() {
				;
			}
		});
		controlBar.addSeparator();

		gravityMenu.addItem("Class distance", new Command() {
			public void execute() {
				;
			}
		});

		gravityMenu.addItem("Datatype distance", new Command() {
			public void execute() {
				;
			}
		});
		controlBar.addSeparator();

		cmd = new Command() {
			public void execute() {
				//addSelectedComponent(dataProp);
				if (dataProp.isEnabled())
					dataProp.setEnabled(false);
				else
					dataProp.setEnabled(true);
			}
		};

		dataProp = new ExtendedMenuItem("<input type=\"checkbox\" name=\"dataProp\" value=\"0\"> Datatype prop.", true, cmd);
		filterMenu.addItem(dataProp);
		
		
		cmd = new Command() {
			public void execute() {
				//addSelectedComponent(dataProp);
				if (solSub.isEnabled())
					solSub.setEnabled(false);
				else
					solSub.setEnabled(true);
			}
		};

		solSub = new ExtendedMenuItem("<input type=\"checkbox\" name=\"solSub\" value=\"0\"> Solitary subclass", true, cmd);
		filterMenu.addItem(solSub);
		
		/*
		filterMenu.addItem("Solitary subclass", new Command() {
			public void execute() {
				;
			}
		});
		*/
		
		cmd = new Command() {
			public void execute() {
				//addSelectedComponent(dataProp);
				if (disjInfo.isEnabled())
					disjInfo.setEnabled(false);
				else
					disjInfo.setEnabled(true);
			}
		};

		disjInfo = new ExtendedMenuItem("<input type=\"checkbox\" name=\"disjInfo\" value=\"1\" checked> Disjointness info", true, cmd);
		filterMenu.addItem(disjInfo);
		
		cmd = new Command() {
			public void execute() {
				addSelectedComponent(cmi);
				//Window.alert("Is cmi null? "+cmi.getCaption());
				//add(cmi.getPanel());
			}
		};

		cmi = new ExtendedMenuItem("<b>Disjointness info</b>", true, cmd);
		filterMenu.addItem(cmi);
		//add(cmi.getPanel());

		
		cmd = new Command() {
			public void execute() {
				//addSelectedComponent(dataProp);
				if (setOp.isEnabled())
					setOp.setEnabled(false);
				else
					setOp.setEnabled(true);
			}
		};

		setOp = new ExtendedMenuItem("<input type=\"checkbox\" name=\"setOp\" value=\"0\"> Set operators", true, cmd);
		filterMenu.addItem(setOp);
		
		filterMenu.addItem("Degree of collapsing", new Command() {
			public void execute() {
				;
			}
		});
		controlBar.addSeparator();

		/*modesMenu.addItem("Pick & Pin", new Command() {
			public void execute() {
				;
			}
		});*/
		
		cmd = new Command() {
			public void execute() {
				//addSelectedComponent(dataProp);
				if (pickPin.isEnabled())
					pickPin.setEnabled(false);
				else
					pickPin.setEnabled(true);
			}
		};

		pickPin = new ExtendedMenuItem("<input type=\"checkbox\" name=\"pickPin\" value=\"0\"> Pick & Pin", true, cmd);
		modesMenu.addItem(pickPin);
		
		
		cmd = new Command() {
			public void execute() {
				//addSelectedComponent(dataProp);
				if (nodeScale.isEnabled())
					nodeScale.setEnabled(false);
				else
					nodeScale.setEnabled(true);
			}
		};

		nodeScale = new ExtendedMenuItem("<input type=\"checkbox\" name=\"nodeScale\" value=\"1\" checked> Node Scaling", true, cmd);
		modesMenu.addItem(nodeScale);
		
		cmd = new Command() {
			public void execute() {
				//addSelectedComponent(dataProp);
				if (compNotation.isEnabled())
					compNotation.setEnabled(false);
				else
					compNotation.setEnabled(true);
			}
		};

		compNotation = new ExtendedMenuItem("<input type=\"checkbox\" name=\"compNotation\" value=\"0\"> Compact Notation", true, cmd);
		modesMenu.addItem(compNotation);
		
		controlBar.addSeparator();
		aboutMenu.addItem("<a target=\"_blank\" href=\"http://vowl.visualdataweb.org/webvowl/license.txt\">MIT License © 2014/15</a>", true, new Command() {
			public void execute() {
				;
			}
		});
		aboutMenu.addItem("WebVOWL Developers:\r\n" + 
				"Vincent Link, Steffen Lohmann, Eduard Marbach, Stefan Negru", new Command() {
			public void execute() {
				;
			}
		});
		aboutMenu.addSeparator();
		aboutMenu.addItem("<a target=\"_blank\" href=\"http://vowl.visualdataweb.org/webvowl.html#releases\">\r\n" + 
				"Version: beta 0.4.0\r\n" + 
				"<br>\r\n" + 
				"(release history)\r\n" + 
				"</a>", true, new Command() {
			public void execute() {
				;
			}
		});
		aboutMenu.addSeparator();
		aboutMenu.addItem("<a target=\"_blank\" href=\"http://purl.org/vowl/\">VOWL Specification »</a>\r\n", true, new Command() {
			public void execute() {
				;
			}
		});
	}

	@Override
	public void handleActivated() {
		super.handleActivated();
		GWT.log("[VOWL] Control portlet status: Activated.");
	}

	@Override
	public void addSelectionListener(SelectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void notifySelectionListeners(SelectionEvent clickEvent) {
		for (SelectionListener listener: listeners) {
			listener.selectionChanged(new SelectionEvent(this));
		}
	}

	@Override
	public void removeSelectionListener(SelectionListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Collection<? extends Object> getSelection() {
		return selection;
	}

	@Override
	public VerticalPanel getPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelection(Collection<? extends Object> selection) {
		this.selection = selection;

	}



}
