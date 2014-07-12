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

import org.gitools.ui.app.commands.AddHeaderTextLabelsCommand;
import org.gitools.ui.core.commands.Command;
import org.gitools.ui.core.commands.tools.HeaderTool;
import org.kohsuke.args4j.Option;

public class AddHeaderTextLabelsTool extends HeaderTool {

    @Option(name = "-s", aliases = "--sort", metaVar = "<sort>", required = false,
            usage = "Sort according to header. Specify either asc[ending] or desc[ending].")
    protected String sort;

    public AddHeaderTextLabelsTool() {
        super();
    }


    @Override
    public String getName() {
        return "add-header-text-labels";
    }


    @Override
    protected Command newJob() {
        return new AddHeaderTextLabelsCommand(heatmap, dimensionKey, pattern, sort);
    }
}
