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
package org.gitools.ui.app.welcome;

import com.brsanthu.googleanalytics.AppViewHit;
import com.google.common.base.Strings;
import org.gitools.ui.app.actions.file.OpenFromFilesystemAction;
import org.gitools.ui.app.actions.file.OpenFromGenomeSpaceAction;
import org.gitools.ui.app.actions.help.ShortcutsAction;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.editor.HtmlEditor;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;

public class WelcomeEditor extends HtmlEditor {

    private static final long serialVersionUID = 6851947500231401412L;

    public WelcomeEditor() {
        super("Welcome", getWelcomeURL());

        Application.track("welcome", "open");
    }

    @Override
    protected void exception(Exception e) {
        ExceptionDialog.show(Application.get(), e);
    }

    @Override
    protected void performAction(String name, Map<String, String> params) {
        switch (name) {
            case "open": {
                switch (params.get("ref")) {
                    case "filesystem":
                        new OpenFromFilesystemAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                    case "genomespace":
                        new OpenFromGenomeSpaceAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                    case "shortcuts":
                        new ShortcutsAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                }
                break;
            }
            case "reload":
                navigate(getWelcomeURL());
                break;
        }
    }

    @Override
    protected void performLoad(String href) {
        CommandLoadFile loadFile = new CommandLoadFile(href);
        JobThread.execute(Application.get(), loadFile);
    }

    @Override
    public void doVisible() {
    }

    private static URL getWelcomeURL() {
        try {
            URL url = new URL("http://www.gitools.org/welcome?uuid="+Settings.getDefault().getUuid());
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000);

            if (connection.getContentLength() != -1) {
                return url;
            }

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        return WelcomeEditor.class.getResource("/html/welcome.html");
    }
}
