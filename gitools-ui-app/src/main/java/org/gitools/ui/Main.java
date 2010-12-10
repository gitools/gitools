/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.gitools.persistence.PersistenceInitialization;
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

		// Initialize file formats
		PersistenceInitialization.registerFormats();
		
		// Initialize actions
		Actions.init();

		// Launch frame
		AppFrame.instance().start();
	}
}
