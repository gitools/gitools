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
package org.gitools.ui.app.batch.tools;

import org.gitools.ui.app.commands.CloseAndSaveCommand;
import org.gitools.ui.app.commands.Command;
import org.kohsuke.args4j.Option;

import java.io.File;

public class CloseAndSaveHeatmapTool extends HeatmapTool {


    @Option(name = "-s", aliases = "--save", required = false,
            usage = "Save current state as heatmap")
    protected boolean save;

    @Option(name = "-a", aliases = "--as", metaVar = "<FILE_NAME>", required = false,
            usage = "path and filename indicating where to save")
    protected String saveAsFilename;

    @Option(name = "-o", aliases = "--optimize", required = false,
            usage = "Optimize data file (slower saving process)")
    protected boolean optimize = true;

    @Option(name = "-d", aliases = "--discard-hidden", required = false,
            usage = "Discard hidden data")
    protected boolean discardHidden = false;

    public CloseAndSaveHeatmapTool() {
        super();
    }


    @Override
    public String getName() {
        return "close";
    }

    @Override
    protected Command newJob() {
        System.out.println("yeees");
        return new CloseAndSaveCommand(save,
                (saveAsFilename != null) ? new File(saveAsFilename) : null,
                optimize, discardHidden, heatmap);
    }

}
