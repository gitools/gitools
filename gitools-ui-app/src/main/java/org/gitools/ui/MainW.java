package org.gitools.ui;

import javax.swing.UIManager;

public class MainW {

	public static void main(String[] args) {
		
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ex) {
				System.err.println("Error loading Look&Feel:");
				ex.printStackTrace();
			}		
	}
}
