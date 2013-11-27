/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.datasources.intogen.dialog;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.datasources.intogen.IntogenService;
import org.gitools.datasources.intogen.IntogenServiceException;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.Application;
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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class IntogenImportDialog extends JDialog {

    public enum ImportType {
        ONCODATA, ONCOMODULES
    }

    private final ImportType type;

    private final DialogHeaderPanel headerPanel;

    private final HtmlPanel htmlPanel;

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
        headerPanel.setLeftLogo(IconUtils.getIconResource(IconNames.LOGO_INTOGEN));
        headerPanel.setLeftLogoLink("http://www.intogen.org");
        headerPanel.setRightLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_INTOGEN_IMPORT, 96));

        htmlPanel = new HtmlPanel();

        SimpleUserAgentContext uagent = new SimpleUserAgentContext();

        rcontext = new SimpleHtmlRendererContext(htmlPanel, uagent) {
            @Override
            public void submitForm(String method, final URL action, String target, String enctype, FormInput[] formInputs) {

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
                if (saveDlg.isCancelled()) {
                    return;
                }

                final File folder = new File(saveDlg.getFolder());
                final String prefix = saveDlg.getNamePrefix();

                final List<String[]> properties = new ArrayList<String[]>(formInputs.length);
                for (FormInput fi : formInputs)
                    properties.add(new String[]{fi.getName(), fi.getTextValue()});

                JobThread.execute(Application.get(), new JobRunnable() {
                    @Override
                    public void run(IProgressMonitor monitor) {
                        try {
                            IntogenService.getDefault().queryFromPOST(folder, prefix, action, properties, monitor);

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    setVisible(false);
                                }
                            });
                        } catch (IntogenServiceException ex) {
                            monitor.exception(ex);
                        }
                    }
                });
            }

            @Override
            protected void submitFormSync(String method, URL action, String target, String enctype, FormInput[] formInputs) throws IOException, SAXException {
                super.submitFormSync(method, action, target, enctype, formInputs);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
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
                int ret = JOptionPane.showConfirmDialog(Application.get(), "There was an error trying to conect to " + getUrl() +
                        "\nPress OK to try again or Cancel to close this dialog and try later.", "Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

                if (ret == JOptionPane.OK_OPTION) {
                    try {
                        rcontext.navigate(new URL(getUrl()), "_this");
                    } catch (Exception ex) {
                        setVisible(false);
                        ExceptionDialog dlg = new ExceptionDialog(Application.get(), ex);
                        dlg.setVisible(true);
                    }
                } else {
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

        setPreferredSize(new Dimension(780, 520));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    rcontext.navigate(getUrl());
                } catch (MalformedURLException ex) {
                    setVisible(false);
                    ExceptionDialog dlg = new ExceptionDialog(Application.get(), ex);
                    dlg.setVisible(true);
                }
            }
        });

        pack();
    }


    private String getUrl() {
        switch (type) {
            case ONCODATA:
                return Settings.getDefault().getIntogenDataUrl();
            case ONCOMODULES:
                return Settings.getDefault().getIntogenOncomodulesUrl();
            default:
                return null;
        }
    }
}
