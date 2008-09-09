package es.imim.bg.ztools.zcalc.ui;

import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error loading Look&Feel: " + e);
		}
		
		AppView gui = new AppView();
		AppController ctrl = new AppController(gui);
		
		ctrl.start();
	}

}
