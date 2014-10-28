/*
 * #%L
 * org.gitools.ui.app
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

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.plugins.mutex.sort.MutualExclusiveMatrixViewSorter;
import org.gitools.ui.core.commands.HeaderCommand;
import org.gitools.ui.platform.settings.Settings;

import java.util.HashSet;
import java.util.List;


public class SortMutualExclusiveCommand extends HeaderCommand {


    private final List<String> items;

    public SortMutualExclusiveCommand(String heatmap, MatrixDimensionKey dimension, String sort, List<String> items, String pattern) {
        super(heatmap, dimension, sort, pattern);

        this.items = items;
    }


    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {
        super.execute(monitor);
        dimension.equals(MatrixDimensionKey.COLUMNS);
        MutualExclusiveMatrixViewSorter.sortByMutualExclusion(
                heatmap,
                pattern,
                new HashSet<>(items),
                false,
                dimension.equals(MatrixDimensionKey.COLUMNS),
                monitor,
                Settings.get().isShowMutualExclusionProgress()
        );
    }

}
