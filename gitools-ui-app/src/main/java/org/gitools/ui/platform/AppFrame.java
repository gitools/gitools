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

import org.gitools.ui.IconNames;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.welcome.WelcomeEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AppFrame extends JFrame {

    private static final long serialVersionUID = -6899584212813749990L;
    private static final String appName;
    private static String appVersion;

    static {
        appName = "Gitools";

        appVersion = AppFrame.class.getPackage().getImplementationVersion();
        if (appVersion == null) {
            appVersion = "SNAPSHOT";
        }
    }

    private JToolBar toolBar;

    private EditorsPanel editorsPanel;

    private StatusBar statusBar;

    private static AppFrame instance;

    public static AppFrame get() {
        if (instance == null) {
            instance = new AppFrame();
        }
        return instance;
    }

    private AppFrame() {

        createComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Settings.getDefault().save();
                System.exit(0);
            }
        });

        setTitle(appName + " " + appVersion);
        setIconImage(IconUtils.getImageIconResource(IconNames.logoMini).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 750));
        pack();
    }

    public static String getAppName() {
        return appName;
    }

    public static String getAppVersion() {
        return appVersion;
    }

    private void createComponents() {
        setJMenuBar(Actions.menuActionSet.createMenuBar());

        toolBar = Actions.toolBarActionSet.createToolBar();

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
                    if (!isLatestGitools()) {
                        JOptionPane.showMessageDialog(AppFrame.get(), "There is a newer version of Gitools.\n Download it from http://www.gitools.org.");
                    }
                } catch (Exception e) {

                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();

    }

    boolean isLatestGitools() throws Exception {

        String thisVersion = AppFrame.getAppVersion();
        if (thisVersion.toLowerCase().contains("snapshot")) {
            return true;
        }

        URL latestUrl = new URL("http://www.gitools.org/download/latest.txt");

        URLConnection con = latestUrl.openConnection();
        con.setConnectTimeout(5);
        con.setReadTimeout(5);

        BufferedReader in = new BufferedReader(new InputStreamReader(latestUrl.openStream()));

        String latestVersion;
        latestVersion = in.readLine();
        in.close();

        if (latestUrl != null && !latestVersion.equals(thisVersion)) {
            return false;
        }
        return true;
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
