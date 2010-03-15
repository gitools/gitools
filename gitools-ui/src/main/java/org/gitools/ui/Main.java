package org.gitools.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.gitools.ui.actions.Actions;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.help.Help;

public class Main {

	public static void main(String[] args) {

		// Initialize look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error loading Look&Feel:");
			e.printStackTrace();
		}

		// Initialize loggers
		Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);

		// Initialize help system
		try {
			Help.getDefault().loadProperties(Main.class.getResourceAsStream("/help/help.properties"));
			Help.getDefault().loadUrlMap(Main.class.getResourceAsStream("/help/help.mappings"));
		}
		catch (Exception ex) {
			System.err.println("Error loading help system:");
			ex.printStackTrace();
		}

		// Initialize actions
		Actions.init();

		// Launch frame
		AppFrame.instance().start();
	}

}
