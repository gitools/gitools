package org.gitools.ui;

import javax.swing.UIManager;

import org.gitools.ui.platform.AppFrame;

public class Main {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error loading Look&Feel: " + e);
		}
		
		AppFrame.instance().start();
	}

}
