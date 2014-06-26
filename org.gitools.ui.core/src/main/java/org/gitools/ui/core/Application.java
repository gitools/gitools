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
package org.gitools.ui.core;

import com.alee.laf.label.WebLabel;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.NotificationOption;
import com.alee.managers.notification.WebNotificationPopup;
import com.brsanthu.googleanalytics.EventHit;
import com.brsanthu.googleanalytics.ExceptionHit;
import com.brsanthu.googleanalytics.GoogleAnalytics;
import org.gitools.resource.SemanticVersion;
import org.gitools.ui.core.components.StatusBar;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.core.components.editor.EditorsPanel;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.application.IApplicationTracking;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.Settings;
import org.gitools.utils.color.ColorRegistry;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class Application extends JFrame implements IApplicationTracking {

    private static final long serialVersionUID = -6899584212813749990L;

    private static final String appName;
    private static final SemanticVersion appVersion;
    private static final String appTracking;
    private static final GoogleAnalytics analytics;
    private static ColorRegistry colorRegistry = ColorRegistry.get();

    private static String test;

    static {
        appName = "Gitools";
        appTracking = "UA-7111176-2";

        Properties settings = new Properties();
        try {
            settings.load(Application.class.getResourceAsStream("/settings.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        appVersion = new SemanticVersion(settings.getProperty("version"));
        analytics = new GoogleAnalytics(appTracking, appName, appVersion.toString());
        analytics.getDefaultRequest().clientId(Settings.get().getUuid());
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

        //initApplication();
    }

    public void initApplication() {
        createComponents();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setTitle(appName + " " + appVersion);
        setIconImage(IconUtils.getImageIconResource(IconNames.logoMini).getImage());
        setPreferredSize(new Dimension(1200, 750));
        pack();
    }

    public static String getAppName() {
        return appName;
    }

    public static SemanticVersion getGitoolsVersion() {
        return appVersion;
    }

    public static String getAppTracking() {
        return appTracking;
    }

    @Override
    public void trackEvent(String category, String action, String label) {
        if (Settings.get().isAllowUsageStatistics()) {
            EventHit hit = new EventHit(category, action, label, null);
            hit.clientId(Settings.get().getUuid());
            analytics.postAsync(hit);
        }
    }

    @Override
    public void trackException(String description) {
        if (Settings.get().isAllowUsageStatistics()) {
            ExceptionHit hit = new ExceptionHit(description);
            hit.clientId(Settings.get().getUuid());
            analytics.postAsync(hit);
        }
    }

    public void setToolBar(JToolBar toolbar) {
        this.toolBar = toolbar;
    }

    private void createComponents() {

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

    public void addEditor(AbstractEditor editor) {
        editorsPanel.addEditor(editor);
    }

    public void start() {

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

        SemanticVersion thisVersion = Application.getGitoolsVersion();

        URL latestUrl = new URL("http://www.gitools.org/downloads/latest.txt");

        URLConnection con = latestUrl.openConnection();
        con.setConnectTimeout(5);
        con.setReadTimeout(5);

        BufferedReader in = new BufferedReader(new InputStreamReader(latestUrl.openStream()));

        String latestVersion;
        latestVersion = in.readLine();
        in.close();

        SemanticVersion latest = new SemanticVersion(latestVersion);

        return latest.isNewerThan(thisVersion);
    }

    public EditorsPanel getEditorsPanel() {
        return editorsPanel;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }


    /**
     * @param text
     * @param displayTime miliseconds
     */
    public void showNotification(String text, int displayTime) {
        WebNotificationPopup popup = new WebNotificationPopup();
        popup.setIcon(NotificationIcon.clock.getIcon());
        popup.setContent (new WebLabel(text));
        popup.setDisplayTime(displayTime);
        showNotificationPopup(popup);
    }

    /**
     *
     * @param text
     */
    public void showNotification(String text) {
        showNotification(text, 4000);
    }

    public void showNotificationPermanent(String text, NotificationOption... options) {
        NotificationManager.showNotification(text, options);
    }

    public void showNotificationPopup(WebNotificationPopup popup) {
        NotificationManager.showNotification(popup);
    }

    public void refresh() {
    }

    public void setCursorWaiting() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    public void setCursorNormal() {
        this.setCursor(Cursor.getDefaultCursor());
    }
}
