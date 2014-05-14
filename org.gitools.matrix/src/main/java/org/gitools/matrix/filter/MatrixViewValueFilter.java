/*
 * #%L
 * gitools-core
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
package org.gitools.matrix.filter;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.matrix.view.IMatrixViewDimension;
import org.gitools.matrix.MatrixUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatrixViewValueFilter {

    public static void filter(IMatrixView matrixView, List<ValueFilterFunction> criteriaList, boolean allCriteria, boolean allElements, boolean invertCriteria, boolean applyToRows, boolean applyToColumns, IProgressMonitor monitor) {

        if (applyToColumns) {
            filter(matrixView, matrixView.getColumns(), matrixView.getRows(), criteriaList, allCriteria, allElements, invertCriteria, monitor);
        }

        if (applyToRows) {
            filter(matrixView, matrixView.getRows(), matrixView.getColumns(), criteriaList, allCriteria, allElements, invertCriteria, monitor);
        }
    }

    private static void filter(IMatrixView matrixView, IMatrixViewDimension filterDimension, IMatrixViewDimension otherDimension, List<ValueFilterFunction> criteriaList, boolean allCriteria, boolean allElements, boolean invertCriteria, IProgressMonitor monitor) {

        Set<String> selection = otherDimension.getSelected();
        if (selection.isEmpty()) {
            selection = Sets.newHashSet(otherDimension);
        }

        Set<String> filterin = new HashSet<>();

        IMatrixPosition position = matrixView.newPosition();
        int length = filterDimension.size();
        monitor.begin("Filtering " + filterDimension.getId().getLabel() + "s", length);
        for (String filterItem : position.iterate(filterDimension)) {

            boolean cellsAnd = true;
            boolean cellsOr = false;

            for (String otherDimItem : position.iterate(otherDimension.subset(selection))) {

                boolean critAnd = true;
                boolean critOr = false;

                for (ValueFilterFunction criteria : criteriaList) {

                    double value = MatrixUtils.doubleValue(matrixView.get(criteria.getLayer(), position));
                    boolean critRes = criteria.getComparator().compare(value, criteria.getCutoffValue());
                    critAnd &= critRes;
                    critOr |= critRes;
                }
                boolean critFilterIn = allCriteria ? critAnd : critOr;
                cellsAnd &= critFilterIn;
                cellsOr |= critFilterIn;
            }

            boolean cellsFilterIn = allElements ? cellsAnd : cellsOr;
            if (invertCriteria) {
                cellsFilterIn = !cellsFilterIn;
            }

            if (cellsFilterIn) {
                filterin.add(filterItem);
            }
            monitor.worked(1);
            if (monitor.isCancelled()) {
                return;
            }
        }

        filterDimension.show(Predicates.in(filterin));
    }

}
