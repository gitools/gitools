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
package org.gitools.ui.commands;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageUtils;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CancellationException;

public abstract class CommandAddHeader extends AbstractCommand {

    protected final String side;
    protected String heatmapid;
    protected final String LAST = "last";
    protected final String COLUMNS = "COLUMNS";
    protected final String ROWS = "ROWS";

    protected Heatmap heatmap;
    protected HeatmapDimension hdim;

    public CommandAddHeader(String heatmap, String side) {
        this.heatmapid = heatmap;
        this.side = side;
    }

    @Override
    public void execute(@NotNull IProgressMonitor monitor) throws CommandException {

        AppFrame appframe = AppFrame.get();

        String availableHeatmaps = "<br/>Available hetamaps: ";
        try {
            monitor.begin("Adding header ...", 1);
            if (heatmapid.equals(LAST)) {
                AbstractEditor e = appframe.getEditorsPanel().getSelectedEditor();
                if (e instanceof HeatmapEditor)  {
                    heatmap = ((HeatmapEditor) e).getModel();
                }
            } else {
                for (AbstractEditor e : appframe.getEditorsPanel().getEditors()) {
                    if (e instanceof HeatmapEditor) {
                        if ( e.getName().equals(heatmapid)) {
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


            if (side.equals(ROWS)) {
                hdim = heatmap.getRows();
            } else if (side.equals(COLUMNS)) {
                hdim = heatmap.getColumns();
            } else {
                throw new Exception("No valid side of heatmap. Choose rows or columns: " + side);
            }

        } catch (Exception e) {

            if (!(e.getCause() instanceof CancellationException)) {
               String text = "<html>Command error:<br>" +
                       "<div style='margin: 5px 0px; padding:10px; width:300px; border: 1px solid black;'><strong>" +
                       e.getLocalizedMessage() +
                       "</strong></div>" +
                       "You may find the <strong>'User guide'</strong> at <a href='http://www.gitools.org'>" +
                       "www.gitools.org</a><br></html>";
               MessageUtils.showErrorMessage(AppFrame.get(), text, e);
            }
            setExitStatus(1);//Error!
            return;
        }

        setExitStatus(0);
        return;
    }
}
