package org.gitools.ui.actions;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.IconUtils;

public abstract class BaseAction extends AbstractAction {

	private static final long serialVersionUID = 8312774908067146251L;
	
	public static final BaseAction separator = new SeparatorAction();

	private boolean defaultEnabled;
	
	public BaseAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
		super(name, icon);
		
		this.defaultEnabled = false;
		
		setEnabled(defaultEnabled);
		
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
			url = getClass().getResource(IconUtils.nullResourceImage);
		
		return new ImageIcon(url);
	}
	
	public boolean isSeparator() {
		return false;
	}
	
	public void setDefaultEnabled(boolean defaultEnabled) {
		this.defaultEnabled = defaultEnabled;
	}
	
	public void setTreeEnabled(boolean enabled) {
		setEnabled(enabled);
	}

	public boolean updateEnabledByEditor(IEditor editor) {
		boolean en = isEnabledByEditor(editor);
		setEnabled(en);
		return en;
	}
	
	public boolean isEnabledByEditor(IEditor editor) {
		if (editor != null) {
			Object model = editor.getModel();
			if (model != null)
				return isEnabledByModel(model);
		}
		
		return defaultEnabled;
	}
	
	public boolean updateEnabledByModel(Object model) {
		boolean en = isEnabledByModel(model);
		setEnabled(en);
		return en;
	}
	
	protected boolean isEnabledByModel(Object model) {
		return defaultEnabled;
	}
}
