package org.gitools.ui.editor;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gitools.ui.platform.AppFrame;

import edu.upf.bg.progressmonitor.IProgressMonitor;

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
				AppFrame.instance().getEditorsPanel().refreshActions();
				/*refreshActions();
				
				AbstractEditor selectedView = getSelectedView();
				if (selectedView != null)
					selectedView.refreshActions();*/
			}
		});
		
		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
	}

	protected void addView(AbstractEditor view, String title) {
		tabbedPane.add(view, title);
	}
	
	protected AbstractEditor getSelectedEditor() {
		return (AbstractEditor) tabbedPane.getSelectedComponent();
	}
	
	@Override
	public Object getModel() {
		return getSelectedEditor().getModel();
	}
	
	@Override
	public boolean isDirty() {
		return getSelectedEditor().isDirty();
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return getSelectedEditor().isSaveAsAllowed();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		getSelectedEditor().doSave(monitor);
	}
	
	@Override
	public void doSaveAs(IProgressMonitor monitor) {
		getSelectedEditor().doSaveAs(monitor);
	}

	@Override
	public void refresh() {
		getSelectedEditor().refresh();
	}
}
