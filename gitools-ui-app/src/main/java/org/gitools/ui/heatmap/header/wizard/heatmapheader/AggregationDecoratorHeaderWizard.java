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
package org.gitools.ui.heatmap.header.wizard.heatmapheader;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringException;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.method.annotations.AnnPatClusteringData;
import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayers;
import org.gitools.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.progressmonitor.ProgressMonitor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregationDecoratorHeaderWizard extends DecoratorHeaderWizard {

    private final HeatmapDimension headerDimension;
    private final HeatmapDimension aggregationDimension;
    private final Heatmap heatmap;

    private List<String> aggregationAnnotationLabels;
    private Map<String, int[]> aggregationIndicesByAnnotation;

    // wizard pages
    private AggregationDataSourcePage dataSourceAggregationPage;
    private AnnotationSourcePage dataSourceAnnotationPage;

    public AggregationDecoratorHeaderWizard(HeatmapDecoratorHeader header, Heatmap heatmap, HeatmapDimension headerDimension, HeatmapDimension aggregationDimension) {
        super(header);

        this.heatmap = heatmap;
        this.headerDimension = headerDimension;
        this.aggregationDimension = aggregationDimension;
    }

    @Override
    public void addPages() {

        HeatmapLayers layers = heatmap.getLayers();
        dataSourceAggregationPage = new AggregationDataSourcePage(headerDimension, layers.getLayerNames(), layers.getTopLayerIndex());
        addPage(dataSourceAggregationPage);

        dataSourceAnnotationPage = new AnnotationSourcePage(headerDimension, "The aggregation is calculated for each distinct value of the chosen annotation individually");
        addPage(dataSourceAnnotationPage);

        super.addPages();
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        IWizardPage nextPage;

        if (page == this.dataSourceAnnotationPage) {

            if (dataSourceAggregationPage.aggregateAnnotationsSeparately()) {
                try {
                    prepareAggregationByAnnotations();
                } catch (ClusteringException e) {
                    dataSourceAnnotationPage.setMessage(MessageStatus.ERROR, "Error clustering the selected annotation.");
                    return page;
                }

            } else {
                prepareAggregationByColumns();
            }

            createAggregationAnnotations();
            nextPage = super.getNextPage(page);

        } else {
            nextPage = super.getNextPage(page);
        }

        return nextPage;
    }

    private void prepareAggregationByAnnotations() throws ClusteringException {

            String pattern = dataSourceAnnotationPage.getSelectedPattern();
            getHeader().setAnnotationPattern(pattern);
            final ClusteringData data = new AnnPatClusteringData(headerDimension, pattern);

            ClusteringMethod clusteringMethod = new AnnPatClusteringMethod();
            ClusteringResults results = clusteringMethod.cluster(data, ProgressMonitor.get());
            aggregationAnnotationLabels = Arrays.asList(results.getClusterTitles());
            aggregationIndicesByAnnotation = results.getDataIndicesByClusterTitle();
    }

    private void prepareAggregationByColumns() {

            aggregationAnnotationLabels = Arrays.asList("All Cols");
            Map<String, int[]> indicesMap = new HashMap<String, int[]>();

            int[] indicesToAggregate;
            if (dataSourceAggregationPage.useAllColumnsOrRows()) {
                indicesToAggregate = new int[aggregationDimension.size()];
                for (int i = 0; i < aggregationDimension.size(); i++)
                    indicesToAggregate[i] = i;
            } else {
                indicesToAggregate = aggregationDimension.getSelected();
            }
            indicesMap.put(aggregationAnnotationLabels.get(0), indicesToAggregate);
            aggregationIndicesByAnnotation = indicesMap;
    }

    private void createAggregationAnnotations() {

        // Compute the annotations and store the results.
        AnnotationMatrix annotations = headerDimension.getAnnotations();
        IAggregator aggregator = dataSourceAggregationPage.getAggregator();
        int aggregationLayer = dataSourceAggregationPage.getAggregationLayer();

        for (String annotationLabel : aggregationAnnotationLabels) {

            int[] indices = aggregationIndicesByAnnotation.get(annotationLabel);
            final double[] valueBuffer = new double[indices.length];

            for (int index = 0; index < headerDimension.size(); index++) {

                // Full the buffer with the values in the
                boolean useRows = (headerDimension == heatmap.getColumns());
                for (int i=0; i < indices.length; i++) {
                    valueBuffer[i] = (Double) (useRows ? heatmap.getCellValue(indices[i], index, aggregationLayer) : heatmap.getCellValue(index, indices[i], aggregationLayer));
                }

                double aggregatedValue = aggregator.aggregate(valueBuffer);
                annotations.setAnnotation(headerDimension.getLabel(index), annotationLabel, Double.toString(aggregatedValue));
            }
        }

        getHeader().setAnnotationLabels(aggregationAnnotationLabels);
        getHeader().setTitle("Data: " + dataSourceAggregationPage.getAggregator() + " of " + dataSourceAggregationPage.getSelectedDataValueName());
    }
}
