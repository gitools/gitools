/*
 * #%L
 * gitools-cli
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
package org.gitools.cli;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class AnalysisArguments extends GitoolsArguments
{

    @NotNull
    @Option(name = "-N", aliases = "-name", metaVar = "<name>",
            usage = "Analysis name. A folder with this name will be created\n" + "in the workdir. (default: unnamed).")
    public final String analysisName = "unnamed";

    @Option(name = "-T", aliases = "-title", metaVar = "<title>",
            usage = "Set the analysis title. (default: analysis name)")
    public String analysisTitle;

    @Option(name = "-notes", metaVar = "<notes>",
            usage = "Set analysis description and notes.")
    public String analysisNotes;

    @NotNull
    @Option(name = "-A", aliases = "-attribute", metaVar = "<name=value>",
            usage = "Define an analysis attribute.")
    public final List<String> analysisAttributes = new ArrayList<String>(0);

    @Option(name = "-w", aliases = "-workdir", metaVar = "<dir>",
            usage = "Working directory (default: current dir).")
    public final String workdir = System.getProperty("user.dir");
}
