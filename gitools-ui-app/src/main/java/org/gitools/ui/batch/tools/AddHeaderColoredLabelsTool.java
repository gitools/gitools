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

import org.gitools.ui.commands.Command;
import org.gitools.ui.commands.CommandAddHeaderColoredLabels;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Option;

import java.util.List;

public class AddHeaderColoredLabelsTool extends AddHeaderTool {

    @Option(name = "-p", aliases = "--pattern", metaVar = "<pattern>", required = true,
            usage = "The pattern of annotations as e.g. ${annotation-id}")
    private String pattern;

    @Option(name = "-c", aliases = "--color", metaVar = "<color>", required = false,
            usage = "A Hex color corresponding to the string for id: #FF0000")
    private List<String> colors;

    @Option(name = "-v", aliases = "--value", metaVar = "<value>", required = false,
            usage = "A value corresponding to a color")
    private List<String> ids;

    @Option(name = "-n", aliases = "--no-auto-generate", required = false,
    usage= "Specify if auto color generation is not desired.")
    private boolean noAutoGenerate;

    public AddHeaderColoredLabelsTool() {
        super();
    }

    @NotNull
    @Override
    public String getName() {
        return "add-header-colored-labels";
    }

    @NotNull
    @Override
    protected Command newJob() {
        return new CommandAddHeaderColoredLabels(heatmap, side.name(), pattern, colors, ids, !noAutoGenerate);
    }

}
