package es.imim.bg.ztools.ui;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.imim.bg.ztools.ui.views.AbstractView;

public class WorkspacePanel extends JTabbedPane {

	private static final long serialVersionUID = 2170150185478413716L;

	private AbstractView selectedView;
	
	public WorkspacePanel() {
		this.selectedView = null;
		
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				if (selectedView != null)
					selectedView.disableActions();
				
				selectedView = (AbstractView) getSelectedComponent();
				if (selectedView != null)
					selectedView.enableActions();
			}
		});
	}
	
	public void addView(AbstractView view) {
		if (view == null)
			return;
		
		final String name = view.getName() != null ? 
				view.getName() : "";
				
		final Icon icon = view.getIcon();
		
		if (icon == null)
			addTab(name, view);
		else
			addTab(name, icon, view);
		
		if (selectedView != null)
			selectedView.disableActions();
		
		setSelectedComponent(view);
		
		selectedView = view;
		selectedView.enableActions();
	}

	public AbstractView getSelectedView() {
		return (AbstractView) getSelectedComponent();
	}
}
