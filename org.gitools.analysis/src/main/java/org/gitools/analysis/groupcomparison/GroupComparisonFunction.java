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

import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.format.math33Preview.CombinatoricsUtils;
import org.gitools.analysis.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.analysis.stats.test.results.GroupComparisonResult;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.model.AbstractMatrixFunction;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

public class GroupComparisonFunction extends AbstractMatrixFunction<Map<String, GroupComparisonResult>, String> {

    private MannWhitneyWilxoxonTest test;
    private IMatrixLayer<Double> valueLayer;
    private IMatrixDimension dimension;
    private DimensionGroup[] groups;
    private NullConversion nullConversion;

    public GroupComparisonFunction(MannWhitneyWilxoxonTest test,
                                   IMatrixDimension dimension,
                                   IMatrixLayer<Double> valueLayer,
                                   NullConversion nullConversion,
                                   DimensionGroup... groups) {
        this.test = test;
        this.dimension = dimension;
        this.valueLayer = valueLayer;
        this.groups = groups;
        this.nullConversion = nullConversion;
    }

    public GroupComparisonFunction() {

    }

    @Override
    public Map<String, GroupComparisonResult> apply(String identifier, IMatrixPosition position) {

        // Filter according to Predicate (groupFilter), transform according to nullConversion,
        // and finally remove nulls.

        Iterator<int[]> combIterator = CombinatoricsUtils.combinationsIterator(groups.length, 2);
        // LinkedHashMap to guarantee order in result heatmap
        Map<String, GroupComparisonResult> resultHashMap = new LinkedHashMap<>();

        while (combIterator.hasNext()) {
            int[] groupIndices = combIterator.next();

            DimensionGroup dimensionGroup1 = groups[groupIndices[0]];
            DimensionGroup dimensionGroup2 = groups[groupIndices[1]];

            Iterable<Double> group1 =
                    filter(
                            transform(
                                    position.iterate(valueLayer, dimension).filter(dimensionGroup1.getPredicate()), nullConversion),
                            notNull());
            Iterable<Double> group2 =
                    filter(
                            transform(
                                    position.iterate(valueLayer, dimension).filter(dimensionGroup2.getPredicate()), nullConversion),
                            notNull());

            resultHashMap.put(
                    dimensionGroup1.getName() + " VS " + dimensionGroup2.getName(),
                    test.processTest(group1, group2));

        }

        return resultHashMap;
    }
}
