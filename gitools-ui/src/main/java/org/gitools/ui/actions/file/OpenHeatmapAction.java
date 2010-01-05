package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.IconNames;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.jobs.OpenMatrixJob;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.settings.Settings;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.utils.FileChooserUtils;

public class OpenHeatmapAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenHeatmapAction() {
		super("Heatmap ...");
		setDesc("Open a heatmap from the file system");
		setSmallIconFromResource(IconNames.openMatrix16);
		setLargeIconFromResource(IconNames.openMatrix24);
		setMnemonic(KeyEvent.VK_M);
		setDefaultEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedPath = FileChooserUtils.selectFile(
				"Select file", FileChooserUtils.MODE_OPEN);
		
		if (selectedPath != null) {
			Settings.getDefault().setLastPath(selectedPath.getParent());
			Settings.getDefault().save();

			final IProgressMonitor monitor = createProgressMonitor();
			
			AppFrame.instance().getJobProcessor().addJob(
					new OpenMatrixJob(selectedPath, monitor));
		}
	}
	
	/*private File getSelectedPath() {
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
	}*/
}
