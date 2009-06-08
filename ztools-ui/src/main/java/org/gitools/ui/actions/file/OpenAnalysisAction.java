package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.gitools.ui.AppFrame;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.jobs.OpenAnalysisJob;
import org.gitools.ui.utils.Options;

import edu.upf.bg.progressmonitor.ProgressMonitor;

public class OpenAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenAnalysisAction() {
		super("Open analysis...");
		setDesc("Open an analysis from the file system");
		setSmallIconFromResource(IconNames.open16);
		setLargeIconFromResource(IconNames.open24);
		setMnemonic(KeyEvent.VK_O);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedPath = getSelectedPath();
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();

			final ProgressMonitor monitor = createProgressMonitor();
			
			AppFrame.instance().getJobProcessor().addJob(
					new OpenAnalysisJob(selectedPath, monitor));
		}
	}
	
	private File getSelectedPath() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select the analysis folder");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//FileFilter fileFilter = new FileFilter(LangKey.XMI);
		//fileFilter.setDescription(LangManager.instance().getString(LangKey.XMI_FILES));
		//fileChooser.addChoosableFileFilter(fileFilter);
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
}
