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
package org.gitools.analysis.stats.test;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.gitools.analysis.stats.test.results.OneWayAnovaResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class OneWayAnovaTest extends AbstractEnrichmentTest {

    private NaturalRanking naturalRanking;

    public OneWayAnovaTest() {
        super("oneWayAnova", OneWayAnovaResult.class);
        naturalRanking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.AVERAGE);
    }

    public OneWayAnovaResult processTest(List<Iterable<Double>> groups) {


        Collection<double[]> groupsCollection = new ArrayList<>();
        //int[] groupNs = new int[groups.size()];
        ArrayList<Integer> groupNs = new ArrayList<>();

        int counter = 0;
        int totalN = 0;
        int minN = 0;
        for (Iterable<Double> g : groups) {
            double[] doubles = Doubles.toArray(Lists.newArrayList(g));
            if (doubles.length > 1) {
                groupsCollection.add(doubles);
                totalN += doubles.length;
                minN = minN == 0 ? doubles.length : Math.min(doubles.length, minN);
                groupNs.add(doubles.length);
            }
        }

        OneWayAnova anova = new OneWayAnova();
        if (groupNs.size() > 1) {
            double pvalue = anova.anovaPValue(groupsCollection);
            double fvalue = anova.anovaFValue(groupsCollection);
            return new OneWayAnovaResult(totalN, minN, groupNs.size(), pvalue, fvalue);
        } else {
            return new OneWayAnovaResult(totalN, minN, groupNs.size(), Double.NaN, Double.NaN);
        }
    }

}
