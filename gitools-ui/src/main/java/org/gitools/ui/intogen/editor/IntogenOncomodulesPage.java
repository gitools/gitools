/*
 *  Copyright 2010 cperez.
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

package org.gitools.ui.intogen.editor;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.net.URL;
import java.util.Properties;
import org.gitools.intogen.IntogenService;
import org.gitools.intogen.IntogenServiceException;
import org.gitools.ui.dialog.progress.JobRunnable;
import org.gitools.ui.dialog.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.editor.Html4Editor;
import org.lobobrowser.html.FormInput;


public class IntogenOncomodulesPage extends Html4Editor {

	public IntogenOncomodulesPage() {
		super("Intogen Oncomodules");

		try {
			navigate(new URL("http://ankara:8080/oncomodules"));
		}
		catch (Exception ex) {
			ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
			dlg.setVisible(true);
		}
	}

	@Override
	protected void submitForm(String method, final URL action, String target, String enctype, FormInput[] formInputs) throws LinkVetoException {
		if (method.equalsIgnoreCase("post")) {
			//super.submitForm(method, action, target, enctype, formInputs);

			final File file = new File("/home/cperez/temp/gitools");
			final String prefix = "x";

			final Properties properties = new Properties();
			for (FormInput fi : formInputs)
				properties.setProperty(fi.getName(), fi.getTextValue());
			
			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						IntogenService.getDefault().queryOncomodulesFromPOST(
								file, prefix, action, properties, monitor);
					}
					catch (IntogenServiceException ex) {
						monitor.exception(ex);
					}
				}
			});

			throw new LinkVetoException();
		}
	}

}
