package es.imim.bg.ztools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.WorkspacePanel;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.views.AbstractView;

public class CloseAction extends BaseAction {

	private static final long serialVersionUID = 2399811452235609343L;

	public CloseAction() {
		super("Close");
		
		setDesc("Close current tab");
		setSmallIconFromResource(IconNames.close16);
		setLargeIconFromResource(IconNames.close24);
		setMnemonic(KeyEvent.VK_O);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		WorkspacePanel workspace = AppFrame.instance().getWorkspace();
		AbstractView currentView = workspace.getSelectedView();
		if (currentView != null)
			workspace.removeView(currentView);
		
		AppFrame.instance().refresh();
		AppFrame.instance().setStatusText("View closed.");
	}

}
