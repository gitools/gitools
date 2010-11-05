package org.gitools.ui.platform.actions;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.IconUtils;

public abstract class BaseAction extends AbstractAction {

	private static final long serialVersionUID = 8312774908067146251L;

	private static final String CHECK_MODE_PROP = "checkMode";
	private static final String SELECTED_PROP = "selected";

	public static final BaseAction separator = new SeparatorAction();

	private boolean defaultEnabled;

	/* whether this is a normal action or a radio/check action */
	private boolean checkMode;

	/* whether if this action is checked/selected */
	private boolean selected;

	/* When checkMode enabled, if actionGroup is defined then
	 * it is a radio action otherwise it is a check action */
	private String actionGroup;
	
	public BaseAction(String name, ImageIcon icon, String desc, Integer mnemonic, boolean checkMode, boolean selected, String actionGroup) {
		super(name, icon);
		
		this.defaultEnabled = false;
		
		if (desc != null)
			putValue(SHORT_DESCRIPTION, desc);
		
		if (mnemonic != null)
			putValue(MNEMONIC_KEY, mnemonic);

		this.checkMode = checkMode;
		this.selected = selected;
		this.actionGroup = actionGroup;
	}

	public BaseAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
		this(name, icon, desc, mnemonic, false, false, null);
	}

	public BaseAction(String name, ImageIcon icon, boolean checkMode, boolean checked, String actionGroup) {
		this(name, icon, null, null, checkMode, checked, actionGroup);
	}

	public BaseAction(String name, boolean checkMode, boolean checked, String actionGroup) {
		this(name, null, null, null, checkMode, checked, actionGroup);
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
		setSmallIcon(getIconResource(name));
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
		setEnabled(defaultEnabled);
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

	public boolean isCheckMode() {
		return checkMode;
	}

	public void setCheckMode(boolean checkMode) {
		boolean old = this.checkMode;
		this.checkMode = checkMode;
		firePropertyChange(CHECK_MODE_PROP, old, checkMode);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		boolean old = this.selected;
		this.selected = selected;
		firePropertyChange(SELECTED_PROP, old, selected);
	}

	public String getActionGroup() {
		return actionGroup;
	}

	public void setActionGroup(String actionGroup) {
		this.actionGroup = actionGroup;
	}
}
