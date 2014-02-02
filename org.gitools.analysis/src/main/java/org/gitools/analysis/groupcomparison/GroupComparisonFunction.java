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
package org.gitools.analysis.groupcomparison;

import org.gitools.analysis.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.analysis.stats.test.results.GroupComparisonResult;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.IMatrixPredicate;
import org.gitools.matrix.model.AbstractMatrixFunction;

public class GroupComparisonFunction extends AbstractMatrixFunction<GroupComparisonResult, String> {

    private MannWhitneyWilxoxonTest test;
    private IMatrixLayer<Double> valueLayer;
    private IMatrixDimension dimension;
    private IMatrixPredicate<Double> group1Filter;
    private IMatrixPredicate<Double> group2Filter;

    public GroupComparisonFunction(MannWhitneyWilxoxonTest test, IMatrixDimension dimension, IMatrixLayer<Double> valueLayer, IMatrixPredicate<Double> group1Filter, IMatrixPredicate<Double> group2Filter) {
        this.test = test;
        this.dimension = dimension;
        this.valueLayer = valueLayer;
        this.group1Filter = group1Filter;
        this.group2Filter = group2Filter;
    }

    public GroupComparisonFunction() {

    }

    @Override
    public GroupComparisonResult apply(String identifier, IMatrixPosition position) {

        Iterable<Double> group1 = position.iterate(valueLayer, dimension).filter(group1Filter);
        Iterable<Double> group2 = position.iterate(valueLayer, dimension).filter(group2Filter);

        return test.processTest(group1, group2);
    }
}
