package org.gitools.ui.actions;

import java.io.File;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.editor.AbstractEditor;
import org.gitools.ui.utils.Options;

import edu.upf.bg.progressmonitor.IProgressMonitor;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;

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
	
	protected AbstractEditor getSelectedEditor() {
		return AppFrame.instance()
			.getWorkspace()
			.getSelectedView();
	}
	
	protected IMatrixView getMatrixView() {
		AbstractEditor editor = getSelectedEditor();
		if (editor == null)
			return null;
		
		IMatrixView matrixView = null;
		
		Object model = editor.getModel();
		if (model instanceof IMatrixView)
			matrixView = (IMatrixView) model;
		else if (model instanceof MatrixFigure)
			matrixView = ((MatrixFigure)model).getMatrixView();
		
		return matrixView;
	}
	
	protected MatrixFigure getMatrixFigure() {
		AbstractEditor editor = getSelectedEditor();
		if (editor == null)
			return null;
		
		MatrixFigure figure = null;
		
		Object model = editor.getModel();
		if (model instanceof MatrixFigure)
			figure = (MatrixFigure)model;
		
		return figure;
	}
	
	protected File getSelectedFile(String title) {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastExportPath());
		
		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}
	
		return null;
	}
	
	protected File getSelectedPath(String title) {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastExportPath());
		
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
		return AppFrame.instance()
			.createMonitor();
	}
	
	public void setTreeEnabled(boolean enabled) {
		setEnabled(enabled);
	}
}
