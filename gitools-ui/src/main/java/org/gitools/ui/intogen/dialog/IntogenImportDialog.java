/*
 *  Copyright 2010 chris.
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

package org.gitools.ui.intogen.dialog;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.gitools.intogen.IntogenService;
import org.gitools.intogen.IntogenServiceException;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.DialogHeaderPanel;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.xml.sax.SAXException;

public class IntogenImportDialog extends JDialog {

	public enum ImportType {
		ONCODATA, ONCOMODULES
	}

	private ImportType type;

	private DialogHeaderPanel headerPanel;

	private HtmlPanel htmlPanel;
	private SimpleHtmlRendererContext rcontext;

	public IntogenImportDialog(Window parent, final ImportType type) {
		super(parent);

		this.type = type;

		setModal(true);

		setLocationByPlatform(true);

		setTitle("IntOGen import...");

		headerPanel = new DialogHeaderPanel();
		headerPanel.setTitle("www.intogen.org");
		headerPanel.setMessage("");
		headerPanel.setRightLogo(IconUtils.getIconResource(IconNames.LOGO_INTOGEN));
		headerPanel.setRightLogoLink("http://www.intogen.org");
		
		htmlPanel = new HtmlPanel();

		SimpleUserAgentContext uagent = new SimpleUserAgentContext();

		rcontext = new SimpleHtmlRendererContext(htmlPanel, uagent) {
			@Override
			public void submitForm(String method, final URL action, String target, String enctype, FormInput[] formInputs) {
				/*System.out.println("method=" + method + ", action=" + action + ", target=" + target + ", enctype="+ enctype);
				if (formInputs != null)
					for (FormInput fi : formInputs)
						System.out.println("name=" + fi.getName() + ", value=" + fi.getTextValue() + ", file=" + fi.getFileValue());*/

				boolean startDownload = false;
				if (method.equalsIgnoreCase("post") && formInputs != null) {

					// Look for a download=TRUE field.
					for (FormInput fi : formInputs) {
						if (fi.getName().equalsIgnoreCase("download") && fi.getTextValue().equalsIgnoreCase("true")) {
							startDownload = true;
							break;
						}
					}
				}

				if (!startDownload) {
					super.submitForm(method, action, target, enctype, formInputs);
					return;
				}

				IntogenImportDownloadDialog saveDlg = new IntogenImportDownloadDialog(IntogenImportDialog.this);
				saveDlg.setVisible(true);
				if (saveDlg.isCancelled())
					return;

				final File folder = new File(saveDlg.getFolder());
				final String prefix = saveDlg.getNamePrefix();

				final Properties properties = new Properties();
				for (FormInput fi : formInputs)
					properties.setProperty(fi.getName(), fi.getTextValue());

				JobThread.execute(AppFrame.instance(), new JobRunnable() {
					@Override
					public void run(IProgressMonitor monitor) {
						try {
							IntogenService.getDefault().queryFromPOST(
									folder, prefix, action, properties, monitor);

							SwingUtilities.invokeLater(new Runnable() {
								@Override public void run() {
									setVisible(false);
								}
							});
						}
						catch (IntogenServiceException ex) {
							monitor.exception(ex);
						}
					}
				});
			}

			@Override
			protected void submitFormSync(String method, URL action, String target, String enctype, FormInput[] formInputs) throws IOException, SAXException {
				super.submitFormSync(method, action, target, enctype, formInputs);

				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						htmlPanel.repaint();
						IntogenImportDialog.this.repaint();
					}
				});
			}

			@Override
			public void setStatus(String message) {
				headerPanel.setMessageStatus(MessageStatus.INFO);
				headerPanel.setMessage(message);
			}

			@Override
			public void error(String message, Throwable throwable) {
				int ret = JOptionPane.showConfirmDialog(AppFrame.instance(),
							"There was an error trying to conect to " + getUrl() +
							"\nPress OK to try again or Cancel to close this dialog and try later.",
							"Error",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.ERROR_MESSAGE);

				if (ret == JOptionPane.OK_OPTION) {
					try {
						rcontext.navigate(new URL(getUrl()), "_this");
					}
					catch (Exception ex) {
						setVisible(false);
						ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
						dlg.setVisible(true);
					}
				}
				else {
					setVisible(false);
				}
			}

			@Override
			protected boolean isNavigationAsynchronous() {
				return false;
			}
		};

		setLayout(new BorderLayout());
		add(headerPanel, BorderLayout.NORTH);
		add(htmlPanel, BorderLayout.CENTER);

		setPreferredSize(new Dimension(700, 520));

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					rcontext.navigate(getUrl());
				} catch (MalformedURLException ex) {
					setVisible(false);
					ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex);
					dlg.setVisible(true);
				}
			}
		});
		
		pack();
	}

	private String getUrl() {
		switch (type) {
			case ONCODATA: return Settings.getDefault().getIntogenDataUrl();
			case ONCOMODULES: return Settings.getDefault().getIntogenOncomodulesUrl();
			default: return null;
		}
	}
}
