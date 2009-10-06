package org.gitools.ui;

import javax.swing.UIManager;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.wizard.WizardDialog;
import org.gitools.ui.wizard.biomart.BiomartModulesWizard;

public class MainW {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error loading Look&Feel: " + e);
		}
		
		BiomartModulesWizard wizard = new BiomartModulesWizard();
		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(), wizard);
		
		wizDlg.open();
	}

}
