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

import com.google.common.collect.Iterables;
import org.gitools.analysis.groupcomparison.dimensiongroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.format.math33Preview.CombinatoricsUtils;
import org.gitools.analysis.stats.test.MannWhitneyWilcoxonTest;
import org.gitools.analysis.stats.test.OneWayAnovaTest;
import org.gitools.analysis.stats.test.results.GroupComparisonResult;
import org.gitools.analysis.stats.test.results.OneWayAnovaResult;
import org.gitools.api.matrix.AbstractMatrixFunction;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;

import java.util.*;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

public class OneWayAnovaFunction extends AbstractMatrixFunction<Map<String, OneWayAnovaResult>, String> {

    private OneWayAnovaTest test;
    private IMatrixLayer<Double> valueLayer;
    private IMatrixDimension sourceDimension;
    private DimensionGroup[] groups;
    private NullConversion nullConversion;

    public OneWayAnovaFunction(OneWayAnovaTest test,
                               IMatrixDimension sourceDimension,
                               IMatrixLayer<Double> valueLayer,
                               NullConversion nullConversion,
                               DimensionGroup... groups) {
        this.test = test;
        this.sourceDimension = sourceDimension;
        this.valueLayer = valueLayer;
        this.groups = groups;
        this.nullConversion = nullConversion;
    }

    public OneWayAnovaFunction() {

    }

    @Override
    public Map<String, OneWayAnovaResult> apply(String identifier, IMatrixPosition position) {

        List<Iterable<Double>> doubleGroups = new ArrayList<>();

        Map<String, OneWayAnovaResult> resultHashMap = new LinkedHashMap<>();

        for (DimensionGroup dimensionGroup : groups) {

            doubleGroups.add(filter(transform(
                    position.iterate(valueLayer, sourceDimension).filter(dimensionGroup.getPredicate()), nullConversion),
                    notNull()));

        }
        OneWayAnovaResult result = test.processTest(doubleGroups);
        resultHashMap.put(
                "oneWayAnova",
                result);
        return resultHashMap;

    }


}
