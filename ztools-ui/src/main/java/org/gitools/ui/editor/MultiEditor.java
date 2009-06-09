package org.gitools.ui.editor;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MultiEditor extends AbstractEditor {

	private static final long serialVersionUID = -6013660760909524202L;

	private JTabbedPane tabbedPane;
	
	public MultiEditor() {
		createComponents();
	}
	
	private void createComponents() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshActions();
				
				AbstractEditor selectedView = getSelectedView();
				if (selectedView != null)
					selectedView.refreshActions();
			}
		});
		
		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
	}

	protected void addView(AbstractEditor view, String title) {
		tabbedPane.add(view, title);
	}
	
	protected AbstractEditor getSelectedView() {
		return (AbstractEditor) tabbedPane.getSelectedComponent();
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
