package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;
import org.biomart._80.martservicesoap.Query;
import org.gitools.biomart.BiomartService;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.biomart.wizard.BiomartModulesWizard;
import org.gitools.ui.dialog.progress.JobRunnable;

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

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Downloading data...", 1);

				Query query = wizard.getQuery();
				String format = (String) wizard.getFormat().getUserObject();

				try {
					BiomartService.getDefault().queryModule(query, file, format);
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}

				monitor.end();
			}
		});
	}

}
