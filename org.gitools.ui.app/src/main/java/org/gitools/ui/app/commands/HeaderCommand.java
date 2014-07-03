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
package org.gitools.ui.app.commands;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.dialog.MessageUtils;

import java.util.concurrent.CancellationException;

public abstract class HeaderCommand extends CommandHeatmap {

    protected final String side;
    protected final String COLUMNS = "COLUMNS";
    protected final String ROWS = "ROWS";

    protected HeatmapDimension hdim;
    protected SortDirection sort;
    protected String pattern;


    public HeaderCommand(String heatmap, String side, String sort, String pattern) {
        super(heatmap);
        this.side = side;
        this.pattern = pattern;
        if (sort.toLowerCase().contains("asc")) {
            this.sort = SortDirection.ASCENDING;
        } else if (sort.toLowerCase().contains("desc")) {
            this.sort = SortDirection.ASCENDING;
        }
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {

        try {

            findHeatmap(monitor);

            switch (side) {
                case ROWS:
                    hdim = heatmap.getRows();
                    break;
                case COLUMNS:
                    hdim = heatmap.getColumns();
                    break;
                default:
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
                MessageUtils.showErrorMessage(Application.get(), text, e);
            }
            setExitStatus(1);
            return;
        }

        setExitStatus(0);
        return;
    }

    protected void applySort() {
        if (sort != null) {
            MatrixViewSorter.sortByLabel(heatmap, hdim.getId().equals(MatrixDimensionKey.ROWS), pattern, sort,
                    false, hdim.getId().equals(MatrixDimensionKey.COLUMNS), pattern, sort, false);
        }
    }
}
