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
