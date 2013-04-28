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
package org.gitools.ui.batch.tools;

import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.ui.platform.progress.JobRunnable;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class LoadTool extends AbstractTool {

    @Argument(index = 0, metaVar = "<matrix-file>", required = true,
            usage = "Matrix file ")
    private String file;

    @Option(name = "-r", aliases = "--rows", metaVar = "<rows-annotation-file>",
            usage = "File rows annotations")
    private String rows;


    @Option(name = "-c", aliases = "--cols", metaVar = "<cols-annotation-file>",
            usage = "File cols annotations")
    private String cols;


    public LoadTool() {
        super();
    }

    @NotNull
    @Override
    public String getName() {
        return "load";
    }

    @NotNull
    @Override
    protected JobRunnable newJob() {
        return new CommandLoadFile(file, rows, cols);
    }
}
