/*
 * #%L
 * org.gitools.mutex
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
package org.gitools.plugins.mutex.control;

import org.gitools.ui.core.commands.Command;
import org.gitools.ui.core.commands.tools.HeaderTool;
import org.kohsuke.args4j.Option;

import java.util.List;


public class SortMutualExclusiveTool extends HeaderTool {

    public SortMutualExclusiveTool() {
        super();
    }

    @Option(name = "-s", aliases = "--sort", metaVar = "<sort>", required = true,
            usage = "Sort according to header. Specify either asc[ending] or desc[ending].")
    protected String sort;

    @Option(name = "-i", aliases = "--identifier", metaVar = "<identifier>", required = false,
            usage = "Specify this option for each identifier which you want to include in the mutex sorting")
    private List<String> ids;

    @Override
    protected Command newJob() {
        return new SortMutualExclusiveCommand(heatmap, dimensionKey, sort, ids, pattern);
    }

    @Override
    public String getName() {
        return "sort-mutex";
    }
}
