package org.gitools.ui.views;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedView extends AbstractView {

	private static final long serialVersionUID = -6013660760909524202L;

	private JTabbedPane tabbedPane;
	
	public TabbedView() {
		createComponents();
	}
	
	private void createComponents() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshActions();
				
				AbstractView selectedView = getSelectedView();
				if (selectedView != null)
					selectedView.refreshActions();
			}
		});
		
		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
	}

	protected void addView(AbstractView view, String title) {
		tabbedPane.add(view, title);
	}
	
	protected AbstractView getSelectedView() {
		return (AbstractView) tabbedPane.getSelectedComponent();
	}
	
	@Override
	public Object getModel() {
		return getSelectedView().getModel();
	}

	@Override
	public void refresh() {
		getSelectedView().refresh();
	}
}
