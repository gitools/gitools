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
package org.gitools.ui.welcome;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.ui.actions.file.*;
import org.gitools.ui.actions.help.ShortcutsAction;
import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.ui.examples.ExamplesManager;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.editor.HtmlEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class WelcomeEditor extends HtmlEditor {

    private static final long serialVersionUID = 6851947500231401412L;

    public WelcomeEditor() {
        super("Welcome", getWelcomeURL());
    }

    @Override
    protected void exception(Exception e) {
        ExceptionDialog.show(Application.get(), e);
    }

    @Override
    protected void performAction(String name, Map<String, String> params) {
        switch (name) {
            case "download":
                switch (params.get("source")) {
                    case "intogen": {
                        IntogenTypeDialog dlg = new IntogenTypeDialog(Application.get());
                        dlg.setVisible(true);
                        if (!dlg.isCancelled()) {
                            switch (dlg.getSelection()) {
                                case IntogenTypeDialog.MATRIX:
                                    new ImportIntogenMatrixAction().actionPerformed(new ActionEvent(this, 0, name));
                                    break;

                                case IntogenTypeDialog.ONCOMODULES:
                                    new ImportIntogenOncomodulesAction().actionPerformed(new ActionEvent(this, 0, name));
                                    break;
                            }
                        }
                        break;
                    }
                    case "go":
                        new ImportGoModulesAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                    case "biomart": {
                        BiomartTypeDialog dlg = new BiomartTypeDialog(Application.get());
                        dlg.setVisible(true);
                        if (!dlg.isCancelled()) {
                            switch (dlg.getSelection()) {
                                case BiomartTypeDialog.TABLE:
                                    new ImportBiomartTableAction().actionPerformed(new ActionEvent(this, 0, name));
                                    break;

                                case BiomartTypeDialog.MODULES:
                                    new ImportBiomartModulesAction().actionPerformed(new ActionEvent(this, 0, name));
                                    break;
                            }
                        }
                        break;
                    }
                    case "kegg":
                        new ImportKeggModulesAction().actionPerformed(new ActionEvent(this, 0, name));
                        break;
                }
                break;
            case "analysis": {
                final Map<String, Class<? extends BaseAction>> actions = new HashMap<>();
                actions.put("enrichment", EnrichmentAnalysisAction.class);
                actions.put("oncodrive", OncodriveAnalysisAction.class);
                actions.put("correlations", NewCorrelationAnalysisAction.class);
                actions.put("overlapping", NewOverlappingAnalysisAction.class);
                actions.put("combination", NewCombinationAnalysisAction.class);
                String ref = params.get("ref");
                Class<? extends BaseAction> actionClass = actions.get(ref);
                if (actionClass != null) {
                    try {
                        ActionEvent event = new ActionEvent(this, 0, name);
                        actionClass.newInstance().actionPerformed(event);
                    } catch (Exception ex) {
                        ExceptionDialog.show(Application.get(), ex);
                    }
                }
                break;
            }
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

    private void downloadExamples(final String path) {
        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    File pathFile = new File(path);
                    URL url = new URL("http://webstart.gitools.org/examples.zip");
                    ExamplesManager.downloadAndExtract(pathFile, monitor, url);
                } catch (Exception ex) {
                    monitor.exception(ex);
                }
            }
        });
    }

    private static URL getWelcomeURL() {
        try {
            URL url = new URL("http://www.gitools.org/welcome");
            URLConnection connection = url.openConnection();

            if(connection.getContentLength() != -1){
                return url;
            }

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }

        return WelcomeEditor.class.getResource("/html/welcome.html");
    }
}
