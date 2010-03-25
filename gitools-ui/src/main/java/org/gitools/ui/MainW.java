package org.gitools.ui;

import javax.swing.UIManager;
import org.gitools.biomart.BiomartCentralPortalSoapService;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.biomart.wizard.BiomartModulesWizard;

public class MainW {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//try {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex) {
			System.err.println("Error loading Look&Feel:");
			ex.printStackTrace();
		}

		BiomartModulesWizard wizard = new BiomartModulesWizard(
				BiomartCentralPortalSoapService.getDefault());
				//BiomartServiceFactory.createDefaultservice());
		WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);
		wizDlg.open();
	}
}
