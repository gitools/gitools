package org.gitools.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;

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

			BiomartModulesWizard wizard = new BiomartModulesWizard(BiomartServiceFactory.createDefaultservice());
			WizardDialog wizDlg = new WizardDialog(AppFrame.instance(), wizard);
			wizDlg.open();

		} catch (BiomartServiceException ex) {
			Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			System.err.println("Error loading Look&Feel: " + e);
		}
	}
}
