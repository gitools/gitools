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

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.clustering.method.annotations.AnnPatColumnClusteringData;
import org.gitools.clustering.method.annotations.AnnPatRowClusteringData;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.matrix.IAnnotations;
import org.gitools.matrix.model.matrix.DoubleMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.heatmap.header.wizard.TextLabelsConfigPage;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.utils.aggregation.IAggregator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AggregatedHeatmapHeaderWizard extends AbstractWizard
{

    public enum DataSourceEnum
    {
        aggregatedData,
        annotation
    }

    private DataSourceEnum dataSource;

    private final Heatmap heatmap;
    private Heatmap headerValueHeatmap;
    private final boolean applyToRows;
    private boolean editionMode;
    private final boolean labelVisible;
    private final HeatmapDataHeatmapHeader header;
    private HeatmapDimension heatmapDim;
    private String[] aggregationTitles;
    private Map<String, int[]> dataIndicesToAggregateByTitle;

    // wizard pages
    private AggregationDataSourcePage dataSourceAggregationPage;
    private AnnotationSourcePage dataSourceAnnotationPage;
    //private ColorScalePage colorScalePage;
    private HeatmapHeaderConfigPage configPage;
    private TextLabelsConfigPage textConfigPage;

    public AggregatedHeatmapHeaderWizard(Heatmap heatmap, @NotNull HeatmapDataHeatmapHeader header, boolean applyToRows)
    {
        super();

        this.heatmap = heatmap;
        this.applyToRows = applyToRows;

        this.header = header;
        this.labelVisible = header.isLabelVisible();

    }

    @Override
    public void addPages()
    {
        if (!editionMode)
        {
            if (dataSource == DataSourceEnum.aggregatedData)
            {
                dataSourceAggregationPage = new AggregationDataSourcePage(heatmap, applyToRows);
                addPage(dataSourceAggregationPage);

                heatmapDim = applyToRows ? heatmap.getColumns() : heatmap.getRows();
                dataSourceAnnotationPage = new AnnotationSourcePage(heatmapDim, "The aggregation is calculated for each " + "distinct value of the chosen annotation individually");
                addPage(dataSourceAnnotationPage);
            }
            else if (dataSource == DataSourceEnum.annotation)
            {
                heatmapDim = applyToRows ? heatmap.getRows() : heatmap.getColumns();
                dataSourceAnnotationPage = new AnnotationSourcePage(heatmapDim, "The annotation column must not contain " + "numeric values");
                addPage(dataSourceAnnotationPage);
            }
        }

        configPage = new HeatmapHeaderConfigPage(header);
        addPage(configPage);

        //colorScalePage = new ColorScalePage(header);
        //addPage(colorScalePage);

        textConfigPage = new TextLabelsConfigPage(header);
        addPage(textConfigPage);

    }

    public boolean isLastPage(IWizardPage page)
    {
        if (page == this.configPage)
        {
            if (!header.isLabelVisible() && page == this.configPage)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return super.isLastPage(page);
        }
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page)
    {
        IWizardPage nextPage;
        if (page == this.dataSourceAggregationPage)
        {

            if (!this.dataSourceAggregationPage.aggregateAnnotationsSeparately())
            {
                generateHeaderHeatmap();
                nextPage = configPage;
            }
            else
            {
                nextPage = super.getNextPage(page);
            }

        }
        else if (dataSource == DataSourceEnum.aggregatedData && page == this.dataSourceAnnotationPage)
        {
            generateHeaderHeatmap();
            nextPage = super.getNextPage(page);

        }
        else if (dataSource == DataSourceEnum.annotation && page == this.dataSourceAnnotationPage)
        {
            try
            {
                headerValueHeatmap = annotationToHeatmap();
            } catch (Exception e)
            {
                dataSourceAnnotationPage.setMessage(MessageStatus.ERROR, "The selected annotation doesn't contain any numeric values.");
                return page;
            }
            dataSourceAnnotationPage.setMessage(MessageStatus.INFO, dataSourceAnnotationPage.infoMessage);
            header.setHeaderHeatmap(headerValueHeatmap);

            //generate Label to id map
            Map<String, Integer> map = generateMap(headerValueHeatmap);
            header.setLabelIndexMap(map);

            // generate title
            header.setTitle("Data: " + dataSourceAnnotationPage.getSelectedAnnotation());

            nextPage = super.getNextPage(page);

        }
        else if (page == this.configPage)
        {

            textConfigPage.setFgColorEnabled(header.isForceLabelColor() || header.getLabelPosition() != HeatmapDataHeatmapHeader.LabelPositionEnum.inside);
            nextPage = super.getNextPage(page);

        }
        else
        {
            nextPage = super.getNextPage(page);
        }
        return nextPage;
    }

    private void generateHeaderHeatmap()
    {
        boolean useAll = dataSourceAggregationPage.useAllColumnsOrRows();

        if (dataSourceAggregationPage.aggregateAnnotationsSeparately())
        {
            getIndecesByAnnotation();
        }
        else
        {
            //get all indices
            setAggregationTitles(new String[]{"All Cols"});
            Map<String, int[]> indicesMap = new HashMap<String, int[]>();
            int[] aggregationGroups;

            if (applyToRows)
            {
                if (useAll)
                {
                    aggregationGroups = new int[heatmap  .getColumns().size()];
                    for (int i = 0; i < heatmap  .getColumns().size(); i++)
                        aggregationGroups[i] = i;
                }
                else
                {
                    aggregationGroups = heatmap  .getColumns().getSelected(  );
                }
            }
            else
            {
                if (useAll)
                {
                    aggregationGroups = new int[heatmap  .getRows().size()];
                    for (int i = 0; i < heatmap  .getRows().size(); i++)
                        aggregationGroups[i] = i;
                }
                else
                {
                    aggregationGroups = heatmap  .getRows().getSelected(  );
                }
            }
            indicesMap.put(aggregationTitles[0], aggregationGroups);
            setDataIndicesToAggregateByTitle(indicesMap);
            headerValueHeatmap = aggregateToHeatmap();
            header.setHeaderHeatmap(headerValueHeatmap);
            //generate Label to id map
            Map<String, Integer> map = generateMap(headerValueHeatmap);
            header.setLabelIndexMap(map);
        }

        // generate title
        StringBuilder sb = new StringBuilder("Data: ");
        sb.append(dataSourceAggregationPage.getDataAggregator().toString());
        sb.append(" of ");
        sb.append(dataSourceAggregationPage.getSelectedDataValueName());
        header.setTitle(sb.toString());
    }

    @NotNull
    private Map<String, Integer> generateMap(@NotNull IMatrixView data)
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (applyToRows)
        {
            for (int i : data.getRows().getVisible())
                map.put(data.getRows().getLabel(i), i);
        }
        else
        {
            for (int i : data.getColumns().getVisible())
                map.put(data.getColumns().getLabel(i), i);
        }
        return map;
    }


    @NotNull
    private Heatmap aggregateToHeatmap()
    {


        int elementsToAggregate;
        String[] columnNames;
        String[] rowNames;
        IAggregator aggregator = dataSourceAggregationPage.getDataAggregator();
        int valueIndex = dataSourceAggregationPage.getSelectedDataValueIndex();
        DoubleMatrix2D valueMatrix;

        if (applyToRows)
        {

            int[] rows = heatmap  .getRows().getVisible();
            valueMatrix = DoubleFactory2D.dense.make(rows.length, aggregationTitles.length, 0.0);

            for (int i = 0; i < aggregationTitles.length; i++)
            {
                int[] columns = dataIndicesToAggregateByTitle.get(aggregationTitles[i]);

                elementsToAggregate = columns.length;
                final double[] valueBuffer = new double[elementsToAggregate];

                for (int j = 0; j < rows.length; j++)
                {
                    double aggregatedValue = aggregateValue(heatmap  , columns, j, valueIndex, aggregator, valueBuffer);
                    valueMatrix.set(j, i, aggregatedValue);
                }
            }

            rowNames = new String[rows.length];
            for (int i = 0; i < rows.length; i++)
                rowNames[i] = heatmap  .getRows().getLabel(i);

            columnNames = aggregationTitles;


        }
        else
        {

            int[] columns = heatmap  .getColumns().getVisible();
            valueMatrix = DoubleFactory2D.dense.make(aggregationTitles.length, columns.length, 0.0);


            for (int i = 0; i < aggregationTitles.length; i++)
            {
                int[] rows = dataIndicesToAggregateByTitle.get(aggregationTitles[i]);

                elementsToAggregate = rows.length;
                final double[] valueBuffer = new double[elementsToAggregate];

                for (int j = 0; j < columns.length; j++)
                {
                    double aggregatedValue = aggregateValue(heatmap  , rows, j, valueIndex, aggregator, valueBuffer);
                    valueMatrix.set(i, j, aggregatedValue);
                }
            }

            columnNames = new String[columns.length];
            for (int i = 0; i < columnNames.length; i++)
                columnNames[i] = heatmap  .getColumns().getLabel(i);

            rowNames = aggregationTitles;
        }

        return new Heatmap(new DoubleMatrix("Data Annotation", columnNames, rowNames, valueMatrix));
    }

    private void getIndecesByAnnotation()
    {
        IMatrixView mv = heatmap  ;
        IAnnotations am = heatmapDim.getAnnotations();
        String pattern = dataSourceAnnotationPage.getSelectedPattern();
        header.setAnnotationPattern(pattern);
        final AggregatedHeatmapHeaderWizard wiz = this;

        final ClusteringData data = applyToRows ? new AnnPatColumnClusteringData(mv, am, pattern) : new AnnPatRowClusteringData(mv, am, pattern);

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    ClusteringMethod clusteringMethod = new AnnPatClusteringMethod();

                    ClusteringResults results = clusteringMethod.cluster(data, monitor);
                    wiz.setAggregationTitles(results.getClusterTitles());
                    wiz.setDataIndicesToAggregateByTitle(results.getDataIndicesByClusterTitle());

                    headerValueHeatmap = aggregateToHeatmap();
                    header.setHeaderHeatmap(headerValueHeatmap);

                    //generate Label to id map
                    Map<String, Integer> map = generateMap(headerValueHeatmap);
                    header.setLabelIndexMap(map);

                } catch (Throwable ex)
                {
                    monitor.exception(ex);
                }
            }
        });
    }

    @NotNull
    private Heatmap annotationToHeatmap() throws Exception
    {

        String[] columnNames;
        String[] rowNames;
        DoubleMatrix2D valueMatrix;

        if (applyToRows)
        {

            IAnnotations annotations = heatmap.getRows().getAnnotations();

            int[] rows = heatmap  .getRows().getVisible();
            valueMatrix = DoubleFactory2D.dense.make(rows.length, 1, 0.0);

            DoubleTranslator doubleTranslator = new DoubleTranslator();
            rowNames = new String[rows.length];
            boolean containsNumericValue = false;

            for (int i = 0; i < rows.length; i++)
            {
                String rowLabel = heatmap.getRows().getLabel(i);
                rowNames[i] = rowLabel;
                Double v = Double.NaN;
                if (annotations.hasIdentifier(rowLabel))
                {
                    String value = annotations.getAnnotation(rowLabel, dataSourceAnnotationPage.getSelectedAnnotation());
                    v = doubleTranslator.stringToValue(value, false);
                }
                valueMatrix.set(i, 0, v);
                if (!containsNumericValue && !v.isNaN())
                {
                    containsNumericValue = true;
                }
            }

            if (!containsNumericValue)
            {
                throw new Exception("No numeric values parsed");
            }

            columnNames = new String[1];
            columnNames[0] = dataSourceAnnotationPage.getSelectedAnnotation();

        }
        else
        {

            IAnnotations annotations = heatmap.getColumns().getAnnotations();

            int[] columns = heatmap  .getColumns().getVisible();
            valueMatrix = DoubleFactory2D.dense.make(1, columns.length, 0.0);

            columnNames = new String[columns.length];
            for (int i = 0; i < columnNames.length; i++)
            {
                String colLabel = heatmap  .getColumns().getLabel(i);
                columnNames[i] = colLabel;
                Double v = Double.NaN;
                if (annotations.hasIdentifier(colLabel))
                {
                    v = Double.parseDouble(annotations.getAnnotation(colLabel, dataSourceAnnotationPage.getSelectedAnnotation()));
                }
                valueMatrix.set(0, i, v);
            }

            rowNames = new String[1];
            rowNames[0] = dataSourceAnnotationPage.getSelectedAnnotation();

        }


        return new Heatmap(new DoubleMatrix("Data Annotation", columnNames, rowNames, valueMatrix));
    }

    void setAggregationTitles(String[] aggregationTitles)
    {
        this.aggregationTitles = aggregationTitles;
    }

    void setDataIndicesToAggregateByTitle(Map<String, int[]> dataIndicesToAggregateByTitle)
    {
        this.dataIndicesToAggregateByTitle = dataIndicesToAggregateByTitle;
    }

    @Override
    public boolean canFinish()
    {
        return currentPage != dataSourceAggregationPage;
    }

    @Override
    public void performFinish()
    {
        if (!editionMode || header.isLabelVisible() != this.labelVisible)
        {
            int elements = this.applyToRows ? header.getHeaderHeatmap().getColumns().getVisible().length : header.getHeaderHeatmap().getRows().getVisible().length;
            int minLabelLenght = header.getLargestLabelLength() * elements;
            if (header.isLabelVisible())
            {
                if (HeatmapDataHeatmapHeader.LabelPositionEnum.inside == header.getLabelPosition())
                {
                    header.setSize(minLabelLenght + 5 * elements);
                }
                else
                {
                    header.setSize(minLabelLenght + 14 * elements);
                }

            }
            else
            {
                header.setSize(14 * elements);
            }
        }
    }

    @Override
    public void pageLeft(IWizardPage currentPage)
    {
        super.pageLeft(currentPage);

        if (currentPage != dataSourceAggregationPage || editionMode)
        {
            return;
        }

    }


    public HeatmapDataHeatmapHeader getHeader()
    {
        return header;
    }

    public void setEditionMode(boolean editionMode)
    {
        this.editionMode = editionMode;
    }


    private double aggregateValue(@NotNull IMatrixView matrixView, @NotNull int[] selectedIndices, int idx, int valueIndex, @NotNull IAggregator aggregator, double[] valueBuffer)
    {

        for (int i = 0; i < selectedIndices.length; i++)
        {
            Object valueObject;
            if (applyToRows)
            {
                valueObject = matrixView.getCellValue(idx, selectedIndices[i], valueIndex);
            }
            else
            {
                valueObject = matrixView.getCellValue(selectedIndices[i], idx, valueIndex);
            }
            valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
        }

        return aggregator.aggregate(valueBuffer);
    }

    public void setDataSource(DataSourceEnum dataSource)
    {
        this.dataSource = dataSource;
    }


}
