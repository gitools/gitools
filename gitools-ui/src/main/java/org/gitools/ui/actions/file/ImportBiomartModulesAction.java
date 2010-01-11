package org.gitools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import org.biomart._80.martservicesoap.Query;
import org.gitools.biomart.BiomartService;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.progress.ProgressJob;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.biomart.wizard.BiomartModulesWizard;

public class ImportBiomartModulesAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportBiomartModulesAction() {
		super("Modules ...");
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final BiomartModulesWizard wizard = new BiomartModulesWizard(
				BiomartService.getDefault());
		
		WizardDialog wdlg = new WizardDialog(
				AppFrame.instance(), wizard);
		
		wdlg.open();
		if (wdlg.isCancelled())
			return;

		final File file = wizard.getSelectedFile();
		/*if (file.exists()) {
			int res = JOptionPane.showConfirmDialog(AppFrame.instance(),
					"File " + file.getAbsolutePath() + " already exists.\n" +
					"Do you want to overwrite it ?",
					"File exists",
					JOptionPane.WARNING_MESSAGE,
					JOptionPane.YES_NO_OPTION);

			if (res != JOptionPane.YES_OPTION)
				return;
		}*/

		new ProgressJob(AppFrame.instance()) {
			@Override protected void runJob() {
				start("Downloading data...", 1);

				Query query = wizard.getQuery();
				String format = (String) wizard.getFormat().getUserObject();

				try {
					BiomartService.getDefault().queryModule(query, file, format);
				}
				catch (Exception ex) {
					exception(ex);
				}

				done();
			}
		}.execute();
	}

}
