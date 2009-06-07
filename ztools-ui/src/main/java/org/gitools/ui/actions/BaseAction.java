package org.gitools.ui.actions;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.views.AbstractView;

import es.imim.bg.progressmonitor.ProgressMonitor;
import org.gitools.model.table.ITable;

public abstract class BaseAction extends AbstractAction {

	private static final long serialVersionUID = 8312774908067146251L;
	
	public static final BaseAction separator = new SeparatorAction();
	
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
	
	public String getName() {
		return getValue(NAME).toString();
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

	protected ImageIcon getSmallIcon() {
		return (ImageIcon) getValue(SMALL_ICON);
	}
	
	protected void setSmallIcon(ImageIcon icon) {
		putValue(SMALL_ICON, icon);
	}
	
	protected void setSmallIconFromResource(String name) {
		setLargeIcon(getIconResource(name));
	}
	
	protected ImageIcon getLargeIcon() {
		return (ImageIcon) getValue(LARGE_ICON_KEY);
	}
	
	protected void setLargeIcon(ImageIcon icon) {
		putValue(LARGE_ICON_KEY, icon);
	}
	
	protected void setLargeIconFromResource(String name) {
		setLargeIcon(getIconResource(name));
	}
	
	private ImageIcon getIconResource(String name) {
		URL url = getClass().getResource(name);
		if (url == null)
			url = getClass().getResource(IconNames.nullResource);
		
		return new ImageIcon(url);
	}
	
	public boolean isSeparator() {
		return false;
	}
	
	protected AbstractView getSelectedView() {
		return AppFrame.instance()
			.getWorkspace()
			.getSelectedView();
	}
	
	protected ITable getTable() {
		AbstractView view = getSelectedView();
		if (view == null)
			return null;
		
		ITable table = null;
		
		Object model = view.getModel();
		if (model instanceof ITable)
			table = (ITable) model;
		
		return table;
	}
	
	protected ProgressMonitor createProgressMonitor() {
		return AppFrame.instance()
			.createMonitor();
	}
	
	public void setTreeEnabled(boolean enabled) {
		setEnabled(enabled);
	}
}
