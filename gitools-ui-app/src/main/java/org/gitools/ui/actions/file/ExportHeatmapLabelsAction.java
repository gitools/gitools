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
package org.gitools.ui.actions.file;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.filter.PatternFunction;
import org.gitools.ui.actions.HeatmapAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.common.ExportHeatmapLabelsWizard;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.newHashSet;

public class ExportHeatmapLabelsAction extends HeatmapAction {

    public ExportHeatmapLabelsAction() {
        super("Export labels ...");

        setDesc("Export row or column labels");
        setMnemonic(KeyEvent.VK_N);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap hm = getHeatmap();

        final ExportHeatmapLabelsWizard wiz = new ExportHeatmapLabelsWizard(hm);
        WizardDialog dlg = new WizardDialog(AppFrame.get(), wiz);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        final File file = wiz.getSavePage().getPathAsFile();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting labels ...", 1);
                    monitor.info("File: " + file.getName());

                    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

                    Iterable<String> identifiers = null;
                    IAnnotations annMatrix = null;

                    switch (wiz.getWhichLabels()) {
                        case VISIBLE_ROWS:
                            identifiers = hm.getRows();
                            annMatrix = hm.getRows().getAnnotations();
                            break;

                        case VISIBLE_COLUMNS:
                            identifiers = hm.getColumns();
                            annMatrix = hm.getColumns().getAnnotations();
                            break;

                        case HIDDEN_ROWS:
                            identifiers = filter(hm.getContents().getRows(), not(in(newHashSet(hm.getRows()))));
                            annMatrix = hm.getRows().getAnnotations();
                            break;

                        case HIDDEN_COLUMNS:
                            identifiers = filter(hm.getContents().getColumns(), not(in(newHashSet(hm.getColumns()))));
                            annMatrix = hm.getColumns().getAnnotations();
                            break;
                    }

                    for (String identifier : transform(identifiers, new PatternFunction(wiz.getPattern(), annMatrix))) {
                        pw.println(identifier);
                    }

                    pw.close();

                    monitor.end();
                } catch (IOException ex) {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText("Labels exported.");
    }

}
