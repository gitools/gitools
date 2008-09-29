package es.imim.bg.ztools.ui.actions;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.views.AbstractView;

public abstract class BaseAction extends AbstractAction {

	private static final long serialVersionUID = 8312774908067146251L;
	
	public BaseAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
		super(name, icon);
		
		setEnabled(false);
		
		if (desc != null)
			putValue(SHORT_DESCRIPTION, desc);
		
		if (mnemonic != null)
			putValue(MNEMONIC_KEY, mnemonic);
	}
	
	public BaseAction(String name, ImageIcon icon, String desc) {
		this(name, icon, desc, null);
	}
	
	public BaseAction(String name, ImageIcon icon) {
		this(name, icon, null, null);
	}
	
	public BaseAction(String name) {
		this(name, null, null, null);
	}
	
	protected AbstractView getSelectedView() {
		return AppFrame.instance()
			.getWorkspace()
			.getSelectedView();
	}
	
	protected ProgressMonitor createProgressMonitor() {
		return AppFrame.instance()
			.createMonitor();
	}
}
