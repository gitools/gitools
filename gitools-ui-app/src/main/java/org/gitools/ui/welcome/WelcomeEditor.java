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
import org.gitools.ui.examples.ExamplesManager;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.editor.HtmlEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WelcomeEditor extends HtmlEditor {

    private static final long serialVersionUID = 6851947500231401412L;

    public WelcomeEditor() {
        super("Welcome", WelcomeEditor.class.getResource("/html/welcome.html"));
    }

    @Override
    protected void exception(Exception e) {
        ExceptionDialog.show(AppFrame.get(), e);
    }

    @Override
    protected void performUrlAction(String name, Map<String, String> params) {
        switch (name) {
            case "goHome":
                try {
                    Desktop.getDesktop().browse(new URI("http://www.gitools.org"));
                } catch (Exception ex) {
                    ExceptionDialog.show(AppFrame.get(), ex);
                }
                break;
            case "importIntogen": {
                IntogenTypeDialog dlg = new IntogenTypeDialog(AppFrame.get());
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
            case "importGo":
                new ImportGoModulesAction().actionPerformed(new ActionEvent(this, 0, name));
                break;
            case "importKegg":
                new ImportKeggModulesAction().actionPerformed(new ActionEvent(this, 0, name));
                break;
            case "importBiomart": {
                BiomartTypeDialog dlg = new BiomartTypeDialog(AppFrame.get());
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
            case "analysis": {
                final Map<String, Class<? extends BaseAction>> actions = new HashMap<>();

                actions.put("Enrichment", EnrichmentAnalysisAction.class);
                actions.put("Oncodrive", OncodriveAnalysisAction.class);
                actions.put("Correlations", NewCorrelationAnalysisAction.class);
                actions.put("Overlapping", NewOverlappingAnalysisAction.class);
                actions.put("Combination", NewCombinationAnalysisAction.class);

                String ref = params.get("ref");
                Class<? extends BaseAction> actionClass = actions.get(ref);
                if (actionClass != null) {
                    try {
                        ActionEvent event = new ActionEvent(this, 0, name);
                        actionClass.newInstance().actionPerformed(event);
                    } catch (Exception ex) {
                        ExceptionDialog.show(AppFrame.get(), ex);
                    }
                }
                break;
            }
            case "open": {
                String ref = params.get("ref");
                if (ref.equals("DataHeatmap")) {
                    new OpenFromFilesystemAction().actionPerformed(new ActionEvent(this, 0, name));
                } else if (ref.equals("DataHeatmapGS")) {
                    new OpenFromGenomeSpaceAction().actionPerformed(new ActionEvent(this, 0, name));
                } else if (ref.equals("Shortcuts")) {
                    new ShortcutsAction().actionPerformed(new ActionEvent(this, 0, name));
                }
                break;
            }
            case "example":
                LoggerFactory.getLogger(WelcomeEditor.class).debug("example: " + params);
                break;
            case "downloadExamples": {
                DownloadExamplesDialog dlg = new DownloadExamplesDialog(AppFrame.get());
                dlg.setPath(Settings.getDefault().getLastWorkPath());
                dlg.setVisible(true);
                downloadExamples(dlg.getPath());
                break;
            }
            case "dataMatrices":
            case "dataModules":
            case "dataTables": {
                DataHelpDialog dlg = new DataHelpDialog(AppFrame.get());
                dlg.setVisible(true);
                break;
            }
        }
    }

    @Override
    public void doVisible() {
    }

    private void downloadExamples(final String path) {
        JobThread.execute(AppFrame.get(), new JobRunnable() {
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
}
