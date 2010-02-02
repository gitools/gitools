package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;
import org.biomart._80.martservicesoap.Query;
import org.gitools.biomart.BiomartService;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.biomart.wizard.BiomartTableWizard;
import org.gitools.ui.dialog.progress.ProgressJob;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ImportBioMartTableAction extends BaseAction {

	private static final long serialVersionUID = 4381993756203388654L;

	public ImportBioMartTableAction() {
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
		
		new ProgressJob(AppFrame.instance()) {
			@Override protected void runJob() {
				IProgressMonitor monitor = getProgressMonitor();
				monitor.begin("Downloading data...", 1);

				Query query = wizard.getQuery();
				String format = (String) wizard.getFormat().getUserObject();

				try {
					BiomartService.getDefault().queryTable(query, file, format);
				}
				catch (Exception ex) {
					exception(ex);
				}

				monitor.end();
			}
		}.execute();
	}

}
