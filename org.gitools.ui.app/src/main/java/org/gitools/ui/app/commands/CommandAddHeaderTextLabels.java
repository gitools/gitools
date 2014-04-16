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
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.ui.platform.Application;

public class CommandAddHeaderTextLabels extends CommandAddHeader {

    private final String pattern;

    public CommandAddHeaderTextLabels(String heatmap, String side, String pattern) {
        super(heatmap, side);
        this.pattern = pattern;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {

        super.execute(monitor);
        if (getExitStatus() > 0) {
            return;
        }

        HeatmapTextLabelsHeader header = new HeatmapTextLabelsHeader();
        header.setLabelPattern(pattern);
        hdim.addHeader(header);

        Application.get().refresh();

        setExitStatus(0);
        return;
    }
}
