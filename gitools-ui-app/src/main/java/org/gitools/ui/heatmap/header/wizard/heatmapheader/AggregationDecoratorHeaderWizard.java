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

import org.gitools.core.clustering.ClusteringData;
import org.gitools.core.clustering.ClusteringException;
import org.gitools.core.clustering.ClusteringMethod;
import org.gitools.core.clustering.ClusteringResults;
import org.gitools.core.clustering.method.annotations.AnnPatClusteringData;
import org.gitools.core.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.HeatmapLayers;
import org.gitools.core.heatmap.header.HeatmapDecoratorHeader;
import org.gitools.core.matrix.model.matrix.AnnotationMatrix;
import org.gitools.core.utils.MatrixUtils;
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
        dataSourceAggregationPage = new AggregationDataSourcePage(headerDimension, aggregationDimension, layers.getLayerNames(), layers.getTopLayerIndex());
        addPage(dataSourceAggregationPage);

        dataSourceAnnotationPage = new AnnotationSourcePage(aggregationDimension, "The aggregation is calculated for each distinct value of the chosen annotation individually");
        addPage(dataSourceAnnotationPage);

        super.addPages();
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {

        if (page == dataSourceAggregationPage) {
            if (dataSourceAggregationPage.aggregateAnnotationsSeparately()) {
                return dataSourceAnnotationPage;
            } else {
                try {
                    prepareAggregationByColumns();
                    createAggregationAnnotations();
                } catch (Exception e) {
                    dataSourceAggregationPage.setMessage(MessageStatus.ERROR, "Error aggregating this values");
                    e.printStackTrace();
                    return dataSourceAggregationPage;
                }

                return super.getNextPage(super.getNextPage(page));
            }
        }

        if (page == this.dataSourceAnnotationPage) {
            try {
                prepareAggregationByAnnotations();
                createAggregationAnnotations();
            } catch (Exception e) {
                dataSourceAnnotationPage.setMessage(MessageStatus.ERROR, "Error clustering the selected annotation.");
                e.printStackTrace();
                return dataSourceAnnotationPage;
            }
        }

        return super.getNextPage(page);

    }

    private void prepareAggregationByAnnotations() throws ClusteringException {

        String pattern = dataSourceAnnotationPage.getSelectedPattern();
        getHeader().setAnnotationPattern(pattern);
        final ClusteringData data = new AnnPatClusteringData(aggregationDimension, pattern);

        String annotationLabelPrefix = dataSourceAggregationPage.getAggregator() + " of " + dataSourceAggregationPage.getSelectedDataValueName();
        ClusteringMethod clusteringMethod = new AnnPatClusteringMethod(annotationLabelPrefix);
        ClusteringResults results = clusteringMethod.cluster(data, ProgressMonitor.get());
        aggregationAnnotationLabels = Arrays.asList(results.getClusterTitles());
        aggregationIndicesByAnnotation = results.getDataIndicesByClusterTitle();
    }

    private void prepareAggregationByColumns() {

        String annotationLabel = dataSourceAggregationPage.getAggregator() + " of ";

        int[] indicesToAggregate;
        if (dataSourceAggregationPage.useAllColumnsOrRows()) {
            annotationLabel += "all";
            indicesToAggregate = new int[aggregationDimension.size()];
            for (int i = 0; i < aggregationDimension.size(); i++)
                indicesToAggregate[i] = i;
        } else {
            indicesToAggregate = aggregationDimension.getSelected();
            annotationLabel += indicesToAggregate.length;
        }

        annotationLabel += " " + dataSourceAggregationPage.getSelectedDataValueName();
        aggregationAnnotationLabels = Arrays.asList(annotationLabel);

        Map<String, int[]> indicesMap = new HashMap<String, int[]>();
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

            MatrixUtils.DoubleCast doubleCast = MatrixUtils.createDoubleCast(heatmap.getLayers().get(aggregationLayer).getValueClass());

            for (int index = 0; index < headerDimension.size(); index++) {

                // Full the buffer with the values in the
                boolean useRows = (headerDimension == heatmap.getColumns());
                for (int i = 0; i < indices.length; i++) {
                    Object value = (useRows ? heatmap.getValue(indices[i], index, aggregationLayer) : heatmap.getValue(index, indices[i], aggregationLayer));
                    valueBuffer[i] = (value == null ? Double.NaN : doubleCast.getDoubleValue(value));
                }

                double aggregatedValue = aggregator.aggregate(valueBuffer);
                if (!Double.isNaN(aggregatedValue)) {
                    annotations.setAnnotation(headerDimension.getLabel(index), annotationLabel, Double.toString(aggregatedValue));
                }
            }
        }

        int maxSize = 160;
        int preferredSize = headerDimension.getCellSize() * aggregationAnnotationLabels.size();

        getHeader().setAnnotationLabels(aggregationAnnotationLabels);
        getHeader().setSize(preferredSize > maxSize ? maxSize : preferredSize);
        getHeader().setTitle(dataSourceAggregationPage.getAggregator() + " of " + dataSourceAggregationPage.getSelectedDataValueName());
    }


}
