package org.gitools.ui.actions.file;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.jobs.OpenMatrixJob;
import org.gitools.ui.utils.Options;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class OpenMatrixAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenMatrixAction() {
		super("matrix ...");
		setDesc("Open a matrix from the file system");
		setSmallIconFromResource(IconNames.open16);
		setLargeIconFromResource(IconNames.open24);
		setMnemonic(KeyEvent.VK_M);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedPath = getSelectedPath();
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();

			final IProgressMonitor monitor = createProgressMonitor();
			
			AppFrame.instance().getJobProcessor().addJob(
					new OpenMatrixJob(selectedPath, monitor));
		}
	}
	
	private File getSelectedPath() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select file");
		fileChooser.setMinimumSize(new Dimension(800,600));
		fileChooser.setPreferredSize(new Dimension(800,600));
		//fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//FileFilter fileFilter = new FileFilter(LangKey.XMI);
		//fileFilter.setDescription(LangManager.instance().getString(LangKey.XMI_FILES));
		//fileChooser.addChoosableFileFilter(fileFilter);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
}
