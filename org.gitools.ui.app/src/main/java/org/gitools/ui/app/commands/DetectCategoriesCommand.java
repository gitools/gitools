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
package org.gitools.ui.app.commands;


import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.AbstractMatrixFunction;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.heatmap.Heatmap;

import java.util.ArrayList;
import java.util.concurrent.CancellationException;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;

public class DetectCategoriesCommand extends AbstractCommand {


    private Heatmap heatmap;
    private ArrayList<Double> categories;


    public DetectCategoriesCommand(Heatmap heatmap) {

        categories = new ArrayList<>();
        this.heatmap = heatmap;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws Command.CommandException {


        try {

            IMatrixPosition position = heatmap.newPosition();
            IMatrixIterable it = position.iterate(heatmap.getLayers().getTopLayer(), heatmap.getRows(), heatmap.getColumns())
                    .monitor(monitor, "Detecting categories")
                    .transform(new AbstractMatrixFunction<Double, Double>() {
                        @Override
                        public Double apply(Double v, IMatrixPosition position) {
                            if (v != null && !categories.contains(v)) {
                                categories.add((Double) v);
                                if (categories.size() > 30) {
                                    throw new CancellationException("Too many categories");
                                }
                            }
                            return null;
                        }
                    });
            ArrayList dummyList = newArrayList(it);

        } catch (CancellationException e) {
            categories.clear();
            throw new CancellationException(e.getMessage());
        }

        sort(categories);
    }

    public ArrayList<Double> getCategories() {
        return categories;
    }
}

