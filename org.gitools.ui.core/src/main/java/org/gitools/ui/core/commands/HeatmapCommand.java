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
package org.gitools.ui.core.commands;

import com.google.common.base.Strings;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.AbstractEditor;


public abstract class HeatmapCommand extends AbstractCommand {
    protected final String LAST = "LAST";
    protected String heatmapid;
    protected Heatmap heatmap;

    public HeatmapCommand(String heatmap) {
        this.heatmapid = heatmap;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {
        findHeatmap(monitor);
    }

    protected void findHeatmap(IProgressMonitor monitor) throws CommandException {

        Application appframe = Application.get();

        if (Strings.isNullOrEmpty(heatmapid)) {
            AbstractEditor editor = Application.get().getEditorsPanel().getSelectedEditor();
            if (editor.getModel() != null && editor.getModel() instanceof Heatmap) {
                heatmap = (Heatmap) editor.getModel();
                return;
            } else {
                throw new CommandException("No heatmap selected or open. Indicate name or open and select a heatmap");
            }
        }


        String availableHeatmaps = "<br/>Available hetamaps: ";
        monitor.begin("Executing command ...", 1);
        if (heatmapid.equals(LAST)) {
            AbstractEditor editor = appframe.getEditorsPanel().getSelectedEditor();
            if (editor.getModel() != null && editor.getModel() instanceof Heatmap) {
                heatmap = (Heatmap) editor.getModel();
            }
        } else {
            for (AbstractEditor editor : appframe.getEditorsPanel().getEditors()) {
                if (editor.getModel() != null && editor.getModel() instanceof Heatmap) {
                    if (editor.getName().equals(heatmapid)) {
                        heatmap = (Heatmap) editor.getModel();
                    } else {
                        availableHeatmaps += "<br/>- " + editor.getName();
                    }
                }
            }
        }

        if (heatmap == null) {
            throw new CommandException("No such heatmap loaded: " + heatmapid + availableHeatmaps);
        }
    }
}
