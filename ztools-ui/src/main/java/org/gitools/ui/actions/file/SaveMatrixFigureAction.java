package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.utils.Options;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public class SaveMatrixFigureAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public SaveMatrixFigureAction() {
		super("matrix figure ...");
		setDesc("Save a matrix figure");
		//setSmallIconFromResource(IconNames.openMatrix16);
		//setLargeIconFromResource(IconNames.openMatrix24);
		setMnemonic(KeyEvent.VK_M);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedPath = getSelectedFile("Select file");
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();

			final IProgressMonitor monitor = createProgressMonitor();
			
			/*AppFrame.instance().getJobProcessor().addJob(
					new OpenMatrixJob(selectedPath, monitor));*/
		}
	}
}
