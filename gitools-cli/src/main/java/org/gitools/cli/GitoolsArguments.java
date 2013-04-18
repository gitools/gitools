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

import org.gitools.threads.ThreadManager;
import org.gitools.utils.tools.args.BaseArguments;
import org.kohsuke.args4j.Option;

public class GitoolsArguments extends BaseArguments {

    @Option(name = "-version", usage = "Print the version information and exit.")
    public final boolean version = false;

    @Option(name = "-quiet", usage = "Don't print any information.")
    public final boolean quiet = false;

    @Option(name = "-v", aliases = "-verbose", usage = "Print extra information.")
    public final boolean verbose = false;

    @Option(name = "-debug", usage = "Print debug level information.")
    public final boolean debug = false;

    @Option(name = "-p", aliases = "-max-procs",
            usage = "Maximum number of parallel processors allowed.\n" + "(default: all available processors).", metaVar = "<n>")
    public final int maxProcs = ThreadManager.getAvailableProcessors();

}
