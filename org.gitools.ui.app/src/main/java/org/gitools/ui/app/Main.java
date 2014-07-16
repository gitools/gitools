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
package org.gitools.ui.app;

import com.alee.extended.image.WebImage;
import com.alee.extended.window.WebProgressDialog;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.checkbox.WebCheckBoxStyle;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.managers.notification.NotificationManager;
import com.google.common.base.Strings;
import org.gitools.api.ApplicationContext;
import org.gitools.api.components.IEditorManager;
import org.gitools.heatmap.plugins.PluginManager;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.app.actions.Actions;
import org.gitools.ui.app.actions.MenuActionSet;
import org.gitools.ui.app.actions.ToolBarActionSet;
import org.gitools.ui.app.actions.help.GitoolsSatsSection;
import org.gitools.ui.app.batch.CommandExecutor;
import org.gitools.ui.app.batch.CommandListener;
import org.gitools.ui.app.dialog.TipsDialog;
import org.gitools.ui.app.welcome.WelcomeEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.help.Help;
import org.gitools.ui.platform.help.Tips;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.os.SystemInfo;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.WeldContainer;
import org.jdesktop.swingx.painter.MattePainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.LogManager;

public class Main {

    public static void main(final String[] args) {

        // Start CommandListener
        boolean portEnabled = Settings.get().isPortEnabled();
        String portString = null;
        if (portEnabled || portString != null) {
            int port = Settings.get().getDefaultPort();
            if (portString != null) {
                port = Integer.parseInt(portString);
            }
            CommandListener.start(port, args);
        }

        // Initialize look and feel
        WebLookAndFeel.install();
        WebLookAndFeel.initializeManagers();
        NotificationManager.setLocation(NotificationManager.NORTH_EAST);
        WebCheckBoxStyle.animated = false;

        // Splash screen , loading dialog

        // Exampler loading dialog
        final WebProgressDialog progress = createProgressDialog();
        progress.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                // Stop loading demo on dialog close
                System.exit(0);
            }
        });
        progress.setVisible(true);


        setProgressText(progress, "Loading Gitools interface");
        // Initialize Weld and ApplicationContext
        WeldContainer container = new StartMain(args).go();
        ApplicationContext.setPersistenceManager(container.instance().select(PersistenceManager.class).get());
        ApplicationContext.setPluginManger(container.instance().select(PluginManager.class).get());
        ApplicationContext.setEditorManger(container.instance().select(IEditorManager.class).get());
        ApplicationContext.setProgressMonitor(new NullProgressMonitor());

        setProgressText(progress, "Loading command executor");
        // Check arguments syntax
        final CommandExecutor cmdExecutor = new CommandExecutor();
        if (args.length > 0) {
            if (!cmdExecutor.checkArguments(args, new PrintWriter(System.err))) {
                return;
            }
        }

        // Workaround to force windows to paint the TaskPaneContainer background
        UIManager.put("TaskPaneContainer.backgroundPainter", new MattePainter(Color.WHITE));

        // Workaround to put a dropdown into a JToolBar
        UIManager.put("PopupMenu.consumeEventOnClose", Boolean.TRUE);

        // Force silence lobobrowser loggers
        try {
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream("org.lobobrowser.level=OFF".getBytes("UTF-8")));
        } catch (IOException e) {
        }

        // Load OS specific things
        if (SystemInfo.isMac) {
            com.apple.eawt.Application osxApp = com.apple.eawt.Application.getApplication();
            osxApp.setDockIconImage(IconUtils.getImageResource(IconNames.logoNoText));
        }

        // Initialize help system
        setProgressText(progress, "Loading help system");
        try {
            Tips.get().load(Main.class.getResourceAsStream("/help/tips.properties"));
            Help.get().loadProperties(Main.class.getResourceAsStream("/help/help.properties"));
            Help.get().loadUrlMap(Main.class.getResourceAsStream("/help/help.mappings"));
        } catch (Exception ex) {
            System.err.println("Error loading help system:");
            ex.printStackTrace();
        }

        // Initialize actions
        setProgressText(progress, "Loading Gitools actions");
        Actions.init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // Launch frame
                Application app = Application.get();
                app.setJMenuBar(MenuActionSet.INSTANCE.createMenuBar());
                app.setToolBar(ToolBarActionSet.INSTANCE.createToolBar());
                Application.get().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        Actions.exitAction.actionPerformed(null);
                    }
                });
                app.initApplication();

                app.addEditor(new WelcomeEditor());
                app.start();

                if (args.length > 0) {

                    // Execute arguments
                    cmdExecutor.execute(args, new PrintWriter(System.err));

                    Application.get().trackEvent("main", isRunningJavaWebStart() ? "webstart" : "start", "with arguments");

                } else {

                    if (Strings.isNullOrEmpty(Settings.get().getStatisticsConsentmentVersion()) ||
                            (!Settings.get().isAllowUsageStatistics() && !Application.getGitoolsVersion().equals(Settings.get().getStatisticsConsentmentVersion()))) {

                        Settings.get().setAllowUsageStatistics(true);
                        JPanel panel = new GitoolsSatsSection(Settings.get()).getPanel();
                        JOptionPane.showMessageDialog(Application.get(), panel, "Statistics", JOptionPane.QUESTION_MESSAGE);
                        Settings.get().setStatisticsConsentmentVersion(Application.getGitoolsVersion().toString());

                    } else {

                        // Show tips dialog
                        TipsDialog tipsDialog = new TipsDialog();
                        tipsDialog.show();

                    }

                    Application.get().trackEvent("main", isRunningJavaWebStart() ? "webstart" : "start", "no arguments");
                }


            }
        });

        // Displaying Gitools and hiding loading dialog
        progress.setVisible(false);

    }

    private static boolean isRunningJavaWebStart() {
        boolean hasJNLP = false;
        try {
            Class.forName("javax.jnlp.ServiceManager");
            hasJNLP = true;
        } catch (ClassNotFoundException ex) {
            hasJNLP = false;
        }
        return hasJNLP;
    }

    private static void setProgressText(WebProgressDialog progress, String s) {
        ProgressWebPanel bar = (ProgressWebPanel) progress.getMiddleComponent();
        bar.setString(s);
    }

    private static WebProgressDialog createProgressDialog() {
        final WebProgressDialog progress = new WebProgressDialog(null, "Loading Gitools " + Application.getGitoolsVersion());

        progress.setIconImage(new ImageIcon(IconNames.class.getResource(IconNames.logoNoText)).getImage());
        progress.setShowProgressBar(false);
        progress.setMiddleComponent(new ProgressWebPanel());
        return progress;
    }

    static class ProgressWebPanel extends WebPanel {
        WebProgressBar progressBar;

        ProgressWebPanel() {
            super();

            this.setLayout(new BorderLayout());

            progressBar = new WebProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setStringPainted(true);
            progressBar.setString("Loading Gitools ...");
            progressBar.setPreferredSize(new Dimension(300, 80));
            progressBar.setVisible(true);
            this.add(progressBar, BorderLayout.SOUTH);

            WebLabel label = new WebLabel("<html><body>" +
                    "<br/><center>" +
                    "<b>Gitools v. " + Application.getGitoolsVersion() + "</b>" +
                    "</center><br/></body</html>");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);

            ImageIcon im = new ImageIcon(IconNames.class.getResource(IconNames.logoNoText));
            add(new WebImage(im), BorderLayout.NORTH);


        }

        public void setString(String s) {
            progressBar.setString(s);
        }

    }

}
