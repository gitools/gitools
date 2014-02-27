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

import com.alee.laf.WebLookAndFeel;
import org.gitools.api.ApplicationContext;
import org.gitools.persistence.PersistenceManager;
import org.gitools.ui.app.actions.Actions;
import org.gitools.ui.app.batch.CommandExecutor;
import org.gitools.ui.app.batch.CommandListener;
import org.gitools.ui.app.dialog.TipsDialog;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.help.Help;
import org.gitools.ui.platform.help.Tips;
import org.gitools.ui.platform.os.SystemInfo;
import org.gitools.utils.progressmonitor.NullProgressMonitor;
import org.jboss.weld.environment.se.StartMain;
import org.jboss.weld.environment.se.WeldContainer;
import org.jdesktop.swingx.painter.MattePainter;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.LogManager;

public class Main {

    public static void main(String[] args) {

        // Initialize Weld and ApplicationContext
        WeldContainer container = new StartMain(args).go();
        ApplicationContext.setPersistenceManager(container.instance().select(PersistenceManager.class).get());
        ApplicationContext.setProgressMonitor(new NullProgressMonitor());

        // Check arguments syntax
        CommandExecutor cmdExecutor = new CommandExecutor();
        if (args.length > 0) {
            if (!cmdExecutor.checkArguments(args, new PrintWriter(System.err))) {
                return;
            }
        }

        // Initialize look and feel
        WebLookAndFeel.install();
        WebLookAndFeel.initializeManagers();

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
        try {
            Tips.get().load(Main.class.getResourceAsStream("/help/tips.properties"));
            Help.get().loadProperties(Main.class.getResourceAsStream("/help/help.properties"));
            Help.get().loadUrlMap(Main.class.getResourceAsStream("/help/help.mappings"));
        } catch (Exception ex) {
            System.err.println("Error loading help system:");
            ex.printStackTrace();
        }

        // Start CommandListener
        boolean portEnabled = Settings.getDefault().isPortEnabled();
        String portString = null;
        if (portEnabled || portString != null) {
            int port = Settings.getDefault().getDefaultPort();
            if (portString != null) {
                port = Integer.parseInt(portString);
            }
            CommandListener.start(port, args);
        }

        // Initialize actions
        Actions.init();

        // Launch frame
        Application.get().start();

        // Execute arguments
        if (args.length > 0) {
            cmdExecutor.execute(args, new PrintWriter(System.err));
        }

        // Show tips dialog
        TipsDialog tipsDialog = new TipsDialog();
        tipsDialog.show();
    }


}
