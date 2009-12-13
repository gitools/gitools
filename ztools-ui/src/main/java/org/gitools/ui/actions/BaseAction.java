package org.gitools.ui.actions;

import java.io.File;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import org.gitools.ui.IconNames;
import org.gitools.ui.editor.AbstractEditor;
import org.gitools.ui.editor.IEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.utils.Options;

import edu.upf.bg.progressmonitor.IProgressMonitor;

import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.matrix.IMatrixView;

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
			url = getClass().getResource(IconNames.nullResource);
		
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
	
	protected AbstractEditor getSelectedEditor() {
		return AppFrame.instance()
			.getEditorsPanel()
			.getSelectedEditor();
	}
	
	protected IMatrixView getMatrixView() {
		AbstractEditor editor = getSelectedEditor();
		if (editor == null)
			return null;
		
		IMatrixView matrixView = null;
		
		Object model = editor.getModel();
		if (model instanceof IMatrixView)
			matrixView = (IMatrixView) model;
		else if (model instanceof Heatmap)
			matrixView = ((Heatmap)model).getMatrixView();
		
		return matrixView;
	}
	
	protected Heatmap getMatrixFigure() {
		AbstractEditor editor = getSelectedEditor();
		if (editor == null)
			return null;
		
		Heatmap figure = null;
		
		Object model = editor.getModel();
		if (model instanceof Heatmap)
			figure = (Heatmap)model;
		
		return figure;
	}
	
	protected File getSelectedFile(String title) {
		return getSelectedFile(title, Options.instance().getLastPath());
	}
	
	protected File getSelectedFile(String title, String currentPath) {
		JFileChooser fileChooser = new JFileChooser(currentPath);

		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	
		int retval = fileChooser.showDialog(AppFrame.instance(), null);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}

		return null;
	}
	
	protected File getSelectedPath(String title) {
		return getSelectedPath(title, Options.instance().getLastPath());
	}
	
	protected File getSelectedPath(String title, String currentPath) {
		JFileChooser fileChooser = new JFileChooser(currentPath);
		
		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}
	
		return null;
	}
	
	protected IProgressMonitor createProgressMonitor() {
		return AppFrame.instance().createMonitor();
	}
}
