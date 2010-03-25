package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;
import org.gitools.biomart.BiomartCentralPortalSoapService;
import org.gitools.biomart.soap.model.Query;
import org.gitools.ui.IconNames;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.biomart.wizard.BiomartTableWizard;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ImportBiomartTableAction extends BaseAction {

	private static final long serialVersionUID = 4381993756203388654L;

	public ImportBiomartTableAction() {
		super("Table ...");
		setLargeIconFromResource(IconNames.biomart24);
		setSmallIconFromResource(IconNames.biomart16);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//final IBiomartService service = BiomartServiceFactory.createDefaultservice();
		final BiomartCentralPortalSoapService service = BiomartCentralPortalSoapService.getDefault();
		final BiomartTableWizard wizard = new BiomartTableWizard(service);
		WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wizard);
		wdlg.open();
		if (wdlg.isCancelled()) {
			return;
		}
		final File file = wizard.getSelectedFile();
		JobThread.execute(AppFrame.instance(), new JobRunnable() {

			@Override
			public void run(IProgressMonitor monitor) {
				monitor.begin("Downloading data...", 1);
				Query query = wizard.getQuery();
				String format = (String) wizard.getFormat().getMime();
				try {
					service.queryTable(query, file, format,
							wizard.isSkipRowsWithEmptyValuesEnabled(),
							wizard.emptyValuesReplacement(), monitor);
				} catch (Exception ex) {
					monitor.exception(ex);
				}
				monitor.end();
			}
		});
	}

}
