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

import com.google.common.base.Strings;
import org.apache.commons.io.FilenameUtils;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.core.commands.Command;
import org.gitools.ui.core.commands.tools.AbstractTool;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;

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


    @Override
    public String getName() {
        return "load";
    }


    @Override
    protected Command newJob() {

        // Convert partial or relative path to absolute path
        file = convertToFullPath(file);
        rows = convertToFullPath(rows);
        cols = convertToFullPath(cols);

        return new CommandLoadFile(file, rows, cols);
    }

    private String convertToFullPath(String file) {

        if (Strings.isNullOrEmpty(file)) {
            return file;
        }

        file = file.trim();

        if (file.charAt(0) == File.pathSeparatorChar) {
            return file;
        }

        if (file.contains("://")) {
            return file;
        }

        String basePath = System.getProperty("user.dir");

        return FilenameUtils.concat(basePath, file);
    }
}
