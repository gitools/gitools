package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;
import org.biomart._80.martservicesoap.Query;
import org.gitools.biomart.BiomartService;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.biomart.wizard.BiomartTableWizard;
import org.gitools.ui.dialog.progress.JobRunnable;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ImportBiomartTableAction extends BaseAction {

	private static final long serialVersionUID = 4381993756203388654L;

	public ImportBiomartTableAction() {
		super("Table ...");
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final BiomartTableWizard wizard = new BiomartTableWizard(
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
					BiomartService.getDefault()
							.queryTable(query, file, format, monitor);
				}
				catch (Exception ex) {
					monitor.exception(ex);
				}

				monitor.end();
			}
		});
	}

}
