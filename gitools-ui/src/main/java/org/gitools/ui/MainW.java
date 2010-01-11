package org.gitools.ui;

import javax.swing.UIManager;
import org.gitools.biomart.BiomartService;

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
		} catch (Exception e) {
			System.err.println("Error loading Look&Feel: " + e);
		}

		/*OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		System.out.println(osbean.getSystemLoadAverage());*/
		
		BiomartModulesWizard wizard = new BiomartModulesWizard(
				BiomartService.getDefault());
		
		WizardDialog wizDlg = new WizardDialog(
				AppFrame.instance(), wizard);
		
		wizDlg.open();
	}

}
