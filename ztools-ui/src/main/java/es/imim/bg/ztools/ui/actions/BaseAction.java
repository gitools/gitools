package es.imim.bg.ztools.ui.actions;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.model.ISectionModel;
import es.imim.bg.ztools.ui.model.ITableModel;
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
	
	protected void setName(String name) {
		putValue(NAME, name);
	}
	
	protected void setDesc(String desc) {
		putValue(SHORT_DESCRIPTION, desc);
	}
	
	protected void setAccelerator(KeyStroke ks) {
		putValue(ACCELERATOR_KEY, ks);
	}
	
	protected void setMnemonic(int vk) {
		putValue(MNEMONIC_KEY, vk);
	}

	protected void setSmallIcon(Icon icon) {
		putValue(SMALL_ICON, icon);
	}
	
	protected void setSmallIconFromResource(String name) {
		setLargeIcon(getIconResource(name));
	}

	protected void setLargeIcon(Icon icon) {
		putValue(LARGE_ICON_KEY, icon);
	}
	
	protected void setLargeIconFromResource(String name) {
		setLargeIcon(getIconResource(name));
	}
	
	private Icon getIconResource(String name) {
		URL url = getClass().getResource(name);
		if (url == null)
			url = getClass().getResource(IconNames.nullResource);
		
		return new ImageIcon(url);
	}
	
	protected AbstractView getSelectedView() {
		return AppFrame.instance()
			.getWorkspace()
			.getSelectedView();
	}
	
	protected ITableModel getTableModel() {
		AbstractView view = getSelectedView();
		if (view == null)
			return null;
		
		ITableModel tableModel = null;
		
		Object model = view.getModel();
		if (model instanceof ISectionModel) {
			ISectionModel sectionModel = (ISectionModel) model;
			tableModel = sectionModel.getTableModel();
		}
		else if (model instanceof ITableModel)
			tableModel = (ITableModel) model;
		
		return tableModel;
	}

	protected ISectionModel getSectionModel() {
		AbstractView view = getSelectedView();
		if (view == null)
			return null;
		
		ISectionModel sectionModel = null;
		
		Object model = view.getModel();
		if (model instanceof ISectionModel)
			sectionModel = (ISectionModel) model;
		
		return sectionModel;
	}
	
	protected ProgressMonitor createProgressMonitor() {
		return AppFrame.instance()
			.createMonitor();
	}
}
