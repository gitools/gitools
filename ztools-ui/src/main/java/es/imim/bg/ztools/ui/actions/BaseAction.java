package es.imim.bg.ztools.ui.actions;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.View;

public abstract class BaseAction extends AbstractAction {

	private static final long serialVersionUID = 8312774908067146251L;

	protected AppFrame app;
	
	public BaseAction(AppFrame app, String name, ImageIcon icon, String desc, Integer mnemonic) {
		super(name, icon);
		
		this.app = app;
		
		if (desc != null)
			putValue(SHORT_DESCRIPTION, desc);
		
		if (mnemonic != null)
			putValue(MNEMONIC_KEY, mnemonic);
	}
	
	public BaseAction(AppFrame app, String name, ImageIcon icon, String desc) {
		this(app, name, icon, desc, null);
	}
	
	public BaseAction(AppFrame app, String name, ImageIcon icon) {
		this(app, name, icon, null, null);
	}
	
	public BaseAction(AppFrame app, String name) {
		this(app, name, null, null, null);
	}
	
	protected AbstractView getCurrentView() {
		return app.getWorkspaceCurrentView();
	}
}
