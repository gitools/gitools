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
package org.gitools.ui.platform;

import com.brsanthu.googleanalytics.EventHit;
import com.brsanthu.googleanalytics.GoogleAnalytics;
import org.apache.commons.lang.StringUtils;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.Actions;
import org.gitools.ui.app.actions.MenuActionSet;
import org.gitools.ui.app.actions.ToolBarActionSet;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.welcome.WelcomeEditor;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.EditorsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Application extends JFrame {

    private static final long serialVersionUID = -6899584212813749990L;

    private static final String appName;
    private static final String appVersion;
    private static final String appTracking;
    private static final GoogleAnalytics analytics;

    static {
        appName = "Gitools";
        appTracking = "UA-7111176-2";
        appVersion = StringUtils.defaultIfEmpty(Application.class.getPackage().getImplementationVersion(), "SNAPSHOT");
        analytics = new GoogleAnalytics(appTracking, appName, appVersion);
        analytics.getDefaultRequest().clientId(Settings.getDefault().getUuid());
    }

    private JToolBar toolBar;

    private EditorsPanel editorsPanel;

    private StatusBar statusBar;

    private static Application instance;

    public static Application get() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    private Application() {

        createComponents();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Actions.exitAction.actionPerformed(null);
            }
        });

        setTitle(appName + " " + appVersion);
        setIconImage(IconUtils.getImageIconResource(IconNames.logoMini).getImage());
        setPreferredSize(new Dimension(1200, 750));
        pack();
    }

    public static String getAppName() {
        return appName;
    }

    public static String getAppVersion() {
        return appVersion;
    }

    public static String getAppTracking() {
        return appTracking;
    }

    public static void track(String editor, String action) {
        if (Settings.getDefault().isAllowUsageStatistics()) {
            EventHit hit = new EventHit(editor, action);
            hit.contentDescription(editor);
            hit.clientId(Settings.getDefault().getUuid());
            analytics.postAsync(hit);
        }
    }

    private void createComponents() {
        setJMenuBar(MenuActionSet.INSTANCE.createMenuBar());

        toolBar = ToolBarActionSet.INSTANCE.createToolBar();

        editorsPanel = new EditorsPanel();

        statusBar = new StatusBar();


        configureLayout();
    }


    private void configureLayout() {
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(editorsPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createWelcomeView() {
        AbstractEditor view = new WelcomeEditor();
        editorsPanel.addEditor(view);
    }

    public void start() {

        createWelcomeView();
        editorsPanel.setSelectedIndex(0);

        setLocationByPlatform(true);
        setVisible(true);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    String javaVersion = System.getProperty("java.version");
                    if (javaVersion != null && javaVersion.endsWith("1.7.0_25")) {
                        JOptionPane.showMessageDialog(Application.get(), "You are using Java 7 build 25. This build has some important bugs, please update to the latest Java 7 build.");
                    }

                    if (isNewerGitoolsAvailable()) {
                        JOptionPane.showMessageDialog(Application.get(), "There is a newer version of Gitools.\n Download it from http://www.gitools.org.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        SwingUtilities.invokeLater(runnable);
    }

    boolean isNewerGitoolsAvailable() throws Exception {

        String thisVersion = Application.getAppVersion();
        if (thisVersion.toLowerCase().contains("snapshot")) {
            return false;
        }

        URL latestUrl = new URL("http://www.gitools.org/downloads/latest.txt");

        URLConnection con = latestUrl.openConnection();
        con.setConnectTimeout(5);
        con.setReadTimeout(5);

        BufferedReader in = new BufferedReader(new InputStreamReader(latestUrl.openStream()));

        String latestVersion;
        latestVersion = in.readLine();
        in.close();


        return !latestVersion.equals(thisVersion);
    }

    public EditorsPanel getEditorsPanel() {
        return editorsPanel;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public void setStatusText(String text) {
        statusBar.setText(text);
        repaint();
    }

    public void refresh() {
    }

}
