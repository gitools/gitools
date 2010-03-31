package org.gitools.ui.actions.file;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.biomart.restful.model.Query;
import org.gitools.ui.IconNames;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.biomart.wizard.BiomartTableWizard;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
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
			//final BiomartCentralPortalSoapService service = BiomartCentralPortalSoapService.getDefault();
			//List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();
			//BiomartSource bsrc = lBs.get(0);
			//final BiomartRestfulService service = BiomartServiceFactory.createRestfulService(bsrc);
		
			final BiomartTableWizard wizard = new BiomartTableWizard();

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
					BiomartRestfulService service = wizard.getService();
					try {
						service.queryTable(query, file, format, wizard.isSkipRowsWithEmptyValuesEnabled(), wizard.emptyValuesReplacement(), monitor);
					} catch (Exception ex) {
						monitor.exception(ex);
					}
					monitor.end();
				}
			});

	}

}
