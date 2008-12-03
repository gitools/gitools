package es.imim.bg.ztools.ui;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.imim.bg.ztools.ui.actions.Actions;
import es.imim.bg.ztools.ui.actions.FileActionSet;
import es.imim.bg.ztools.ui.actions.HelpActionSet;
import es.imim.bg.ztools.ui.actions.MenuActionSet;
import es.imim.bg.ztools.ui.views.AbstractView;

public class WorkspacePanel extends JTabbedPane {

	private static final long serialVersionUID = 2170150185478413716L;

	private AbstractView selectedView;
	
	private JTabbedPane tabbedPane;
	
	public WorkspacePanel() {
		this.selectedView = null;
	
		createComponents();
		
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				/*if (selectedView != null)
					selectedView.disableActions();*/
				refreshActions();
				
				selectedView = (AbstractView) getSelectedComponent();
				if (selectedView != null)
					selectedView.refreshActions();
			}
		});
	}
	
	private void createComponents() {
		tabbedPane = new JTabbedPane();
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
		
		/*if (selectedView != null)
			selectedView.disableActions();*/
		refreshActions();
		
		setSelectedComponent(view);
		
		selectedView = view;
		selectedView.refreshActions();
	}

	public void removeView(AbstractView view) {
		if (view == null)
			return;
		
		int i = indexOfComponent(view);
		if (i != -1)
			remove(i);
		
		refreshActions();
		
		view = (AbstractView) getSelectedComponent();
		if (view != null)
			view.refreshActions();
	}
	
	public AbstractView getSelectedView() {
		return (AbstractView) getSelectedComponent();
	}
	
	private void refreshActions() {
		Actions.menuActionSet.setTreeEnabled(false);
		
		MenuActionSet.fileActionSet.setEnabled(true);
		MenuActionSet.helpActionSet.setEnabled(true);
		
		FileActionSet.openAnalysisAction.setEnabled(true);
		FileActionSet.closeAction.setEnabled(getTabCount() > 0);
		FileActionSet.exitAction.setEnabled(true);
		Actions.zcalcAnalysisAction.setEnabled(true);
		HelpActionSet.aboutAction.setEnabled(true);
	}
}
