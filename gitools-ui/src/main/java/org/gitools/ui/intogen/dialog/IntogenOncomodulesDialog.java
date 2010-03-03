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

/*
 * IntogenOncomodulesDialog.java
 *
 * Created on Feb 24, 2010, 3:34:25 PM
 */

package org.gitools.ui.intogen.dialog;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.net.URL;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.gitools.intogen.IntogenService;
import org.gitools.intogen.IntogenServiceException;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;

public class IntogenOncomodulesDialog extends javax.swing.JDialog {

	//private HtmlPanel htmlPanel;
	private SimpleHtmlRendererContext rcontext;

    /** Creates new form IntogenOncomodulesDialog */
    public IntogenOncomodulesDialog(java.awt.Window parent, boolean modal) {
        super(parent);
		setModal(modal);
		
        initComponents();

		namePrefix.setText("unnamed");
		namePrefix.setSelectionStart(0);
		namePrefix.setSelectionEnd(namePrefix.getDocument().getLength());
		namePrefix.requestFocusInWindow();
		folder.setText(Settings.getDefault().getLastExportPath());
		
		//htmlPanel = new HtmlPanel();
		rcontext = new SimpleHtmlRendererContext(htmlPanel, new SimpleUserAgentContext()) {

			@Override
			public void error(String message, Throwable throwable) {
				String url = Settings.getDefault().getIntogenOncomodulesUrl();
				if (!url.equals(Settings.DEFAULT_INTOGEN_ONCOMODULES_URL))
					Settings.getDefault().setIntogenOncomodulesUrl(Settings.DEFAULT_INTOGEN_ONCOMODULES_URL);

				int ret = JOptionPane.showConfirmDialog(AppFrame.instance(),
							"There was an error trying to conect to " + url +
							"\nPress OK to try again or Cancel to close this dialog and try later.",
							"Error",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.ERROR_MESSAGE);

				if (ret == JOptionPane.OK_OPTION) {
					try {
						rcontext.navigate(new URL(Settings.getDefault().getIntogenOncomodulesUrl()), "_this");
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
			
			protected boolean isNavigationAsynchronous() {
				return false;
			}
			
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

				final File file = new File(folder.getText());
				final String prefix = namePrefix.getText();

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
			}
		};

		/*panel.setLayout(new BorderLayout());
		panel.add(htmlPanel, BorderLayout.CENTER);*/

		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				try {
					rcontext.navigate(new URL(Settings.getDefault().getIntogenOncomodulesUrl()), "_this");
				}
				catch (Exception ex) {
					if (!Settings.getDefault().getIntogenOncomodulesUrl().equals(Settings.DEFAULT_INTOGEN_ONCOMODULES_URL))
						Settings.getDefault().setIntogenOncomodulesUrl(Settings.DEFAULT_INTOGEN_ONCOMODULES_URL);
					
					try {
						rcontext.navigate(new URL(Settings.getDefault().getIntogenOncomodulesUrl()), "_this");
					}
					catch (Exception ex2) {
						setVisible(false);
						ExceptionDialog dlg = new ExceptionDialog(AppFrame.instance(), ex2);
						dlg.setVisible(true);
					}
				}
			}
		});
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        namePrefix = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        folder = new javax.swing.JTextField();
        folderBtn = new javax.swing.JButton();
        htmlPanel = new org.lobobrowser.html.gui.HtmlPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("IntOGen Oncomodules import");
        setLocationByPlatform(true);

        jLabel1.setText("Name prefix");

        jLabel2.setText("Folder");

        folderBtn.setText("Browse...");
        folderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                folderBtnActionPerformed(evt);
            }
        });

        htmlPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(htmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(namePrefix, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                            .addComponent(folder, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(folderBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(namePrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(folder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(folderBtn))
                .addGap(18, 18, 18)
                .addComponent(htmlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void folderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_folderBtnActionPerformed
		File selPath = FileChooserUtils.selectPath(
				"Select folder", folder.getText());

		if (selPath != null) {
			folder.setText(selPath.getAbsolutePath());
			Settings.getDefault().setLastExportPath(selPath.getAbsolutePath());
		}
	}//GEN-LAST:event_folderBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField folder;
    private javax.swing.JButton folderBtn;
    private org.lobobrowser.html.gui.HtmlPanel htmlPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField namePrefix;
    // End of variables declaration//GEN-END:variables

}
