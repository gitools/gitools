/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.app.commands;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.AbstractEditor;


public abstract class CommandHeatmap extends AbstractCommand {
    protected final String LAST = "LAST";
    protected String heatmapid;
    protected Heatmap heatmap;

    public CommandHeatmap(String heatmap) {
        this.heatmapid = heatmap;
    }

    protected void findHeatmap(IProgressMonitor monitor) throws Exception {
        Application appframe = Application.get();

        String availableHeatmaps = "<br/>Available hetamaps: ";
        monitor.begin("Executing command ...", 1);
        if (heatmapid.equals(LAST)) {
            AbstractEditor e = appframe.getEditorsPanel().getSelectedEditor();
            if (e instanceof HeatmapEditor) {
                heatmap = ((HeatmapEditor) e).getModel();
            }
        } else {
            for (AbstractEditor e : appframe.getEditorsPanel().getEditors()) {
                if (e instanceof HeatmapEditor) {
                    if (e.getName().equals(heatmapid)) {
                        heatmap = ((HeatmapEditor) e).getModel();
                    } else {
                        availableHeatmaps += "<br/>- " + e.getName();
                    }
                }
            }
        }

        if (heatmap == null) {
            throw new Exception("No such heatmap loaded: " + heatmapid + availableHeatmaps);
        }
    }
}
