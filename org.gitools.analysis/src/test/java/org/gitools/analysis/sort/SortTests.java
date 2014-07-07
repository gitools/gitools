/*
 * #%L
 * org.gitools.analysis
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
package org.gitools.analysis.sort;

import com.google.common.collect.Lists;
import org.gitools.analysis.AssertMatrix;
import org.gitools.analysis.WeldRunner;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.SortDirection;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.aggregation.MeanAggregator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.gitools.api.ApplicationContext.getProgressMonitor;

@RunWith(WeldRunner.class)
public class SortTests {
    //"/sort/test04-data.cdm.gz"
    //"/sort/test04-data-sorted.tdm.gz"   //rows & columns mean ascending

    IMatrix data;
    Heatmap resultsHeatmap;

    public SortTests() {

    }

    @Before
    public void setUp() throws Exception {

        String resourceUrl = "/sort/test04-data.cdm.gz";
        String resultResourceUrl = "/sort/test04-data-sorted.heatmap.zip";

        data = new ResourceReference<>(
                new UrlResourceLocator(getClass().getResource(resourceUrl)),
                IMatrix.class
        ).get();

        resultsHeatmap = new ResourceReference<>(
                new UrlResourceLocator(getClass().getResource(resultResourceUrl)),
                Heatmap.class
        ).get();

    }

    @Test
    public void testSortedMatrix() {
        Heatmap dataHeatmap = new Heatmap(data);

        final IMatrixLayer layer = dataHeatmap.getLayers().getTopLayer();
        layer.setSortDirection(SortDirection.ASCENDING);
        layer.setAggregator(MeanAggregator.INSTANCE);
        List<IMatrixLayer> criteriaArray = Lists.newArrayList(layer);

        MatrixViewSorter.sortByValue(dataHeatmap, criteriaArray, true, true, getProgressMonitor());

        AssertMatrix.assertEquals(dataHeatmap.getRows(), resultsHeatmap.getRows());
        AssertMatrix.assertEquals(dataHeatmap.getColumns(), resultsHeatmap.getColumns());

        //AssertMatrix.assertEquals(dataHeatmap, resultsHeatmap);

    }


}
