package org.gitools.ui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.biomart.wizard.BiomartModulesWizard;

public class MainW {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {
				System.err.println("Error loading Look&Feel:");
				ex.printStackTrace();
			}
			
			//List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();
			//BiomartSource bsrc = lBs.get(0);
			//final BiomartRestfulService service = BiomartServiceFactory.createRestfulService(bsrc);
			//BiomartModulesWizard wizard = new BiomartModulesWizard(BiomartCentralPortalSoapService.getDefault());

			BiomartModulesWizard wizard = new BiomartModulesWizard();
			//BiomartServiceFactory.createDefaultservice());
			WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);
			wizDlg.open();
		
	}
}
