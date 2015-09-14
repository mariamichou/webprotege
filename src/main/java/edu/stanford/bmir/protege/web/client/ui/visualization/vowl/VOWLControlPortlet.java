package edu.stanford.bmir.protege.web.client.ui.visualization.vowl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.visualization.loading.GraphLoadedEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.visualization.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

@SuppressWarnings("unchecked")
public class VOWLControlPortlet extends AbstractOWLEntityPortlet implements Selectable {

	private static final String CONTROL_TITLE = "Control Bar";
	private MenuBar controlBar;
	// Listeners to selection events in this portlet
	private Collection<SelectionListener> listeners;
	private boolean paused = false;
	private String option = null;
	private Collection<? extends Object> selection;

	public VOWLControlPortlet(SelectionModel selectionModel, Project project) {
		super(selectionModel, project);
		this.listeners = new ArrayList<SelectionListener>();
	}

	@Override
	public void initialize() {
		setTitle(CONTROL_TITLE);
		controlBar = new MenuBar(false);
		controlBar.setAutoOpen(true);
		createMenuItems();
		add(controlBar);
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

		MenuBar resetMenu = new MenuBar(true);
		controlBar.addItem("Reset", new Command() {
			public void execute() {
				//Window.alert("You selected Reset!");
				selection = Arrays.asList("reset");
				GWT.log("[VOWL] Visualization graph, reset.");
				notifySelectionListeners(new SelectionEvent(VOWLControlPortlet.this));
			}
		});
		controlBar.addSeparator();

		MenuBar pauseMenu = new MenuBar(true);
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
				Window.alert("You selected a menu item!");
			}
		});
		exportMenu.addItem("Export as JSON", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		controlBar.addSeparator();
		
		gravityMenu.addItem("Class distance", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});

		gravityMenu.addItem("Datatype distance", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		controlBar.addSeparator();

		filterMenu.addItem("Datatype prop.", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		filterMenu.addItem("Solitary subclass", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		filterMenu.addItem("Disjointness info", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		filterMenu.addItem("Set operators", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		filterMenu.addItem("Degree of collapsing", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		controlBar.addSeparator();
		
		modesMenu.addItem("Pick & Pin", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		modesMenu.addItem("Node Scaling", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		modesMenu.addItem("Compact Notation", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		controlBar.addSeparator();
		
		aboutMenu.addItem("MIT...", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		aboutMenu.addItem("WebVOWL Developers:\r\n" + 
				"Vincent Link, Steffen Lohmann, Eduard Marbach, Stefan Negru", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		aboutMenu.addSeparator();
		aboutMenu.addItem("Version...", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		});
		aboutMenu.addSeparator();
		aboutMenu.addItem("VOWL Specification...", new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
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
