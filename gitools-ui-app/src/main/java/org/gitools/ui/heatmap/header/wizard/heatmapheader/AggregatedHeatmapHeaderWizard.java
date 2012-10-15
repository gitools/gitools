/*
 *  Copyright 2011 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.heatmap.header.wizard.heatmapheader;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import edu.upf.bg.aggregation.IAggregator;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.clustering.method.annotations.AnnPatColumnClusteringData;
import org.gitools.clustering.method.annotations.AnnPatRowClusteringData;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.heatmap.header.wizard.TextLabelsConfigPage;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

import java.util.HashMap;
import java.util.Map;

public class AggregatedHeatmapHeaderWizard extends AbstractWizard {

    public enum DataSourceEnum {
        aggregatedData,
        annotation
    };

    private DataSourceEnum dataSource;

    private Heatmap heatmap;
    private Heatmap headerValueHeatmap;
    private boolean applyToRows;
    private boolean editionMode;
    private boolean labelVisible;
    private HeatmapDataHeatmapHeader header;
    private HeatmapDim heatmapDim;
    private String[] aggregationTitles;
    private Map<String, int[]> dataIndicesToAggregateByTitle;

    // wizard pages
    private AggregationDataSourcePage dataSourceAggregationPage;
    private AnnotationSourcePage dataSourceAnnotationPage;
    private ColorScalePage colorScalePage;
    private HeatmapHeaderConfigPage configPage;
    private TextLabelsConfigPage textConfigPage;

    public AggregatedHeatmapHeaderWizard(Heatmap heatmap, HeatmapDataHeatmapHeader header, boolean applyToRows) {
        super();

        this.heatmap = heatmap;
        this.applyToRows = applyToRows;

        this.header = header;
        this.labelVisible = header.isLabelVisible();

    }

    @Override
    public void addPages() {
        if (!editionMode) {
            if (dataSource == DataSourceEnum.aggregatedData) {
                dataSourceAggregationPage = new AggregationDataSourcePage(heatmap, applyToRows);
                addPage(dataSourceAggregationPage);

                heatmapDim = applyToRows ? heatmap.getColumnDim() : heatmap.getRowDim();
                dataSourceAnnotationPage = new AnnotationSourcePage(heatmapDim, "The aggregation is calculated for each " +
                        "distinct value of the chosen annotation individually");
                addPage(dataSourceAnnotationPage);
            }
            else if (dataSource == DataSourceEnum.annotation)  {
                heatmapDim = applyToRows ? heatmap.getRowDim() : heatmap.getColumnDim();
                dataSourceAnnotationPage = new AnnotationSourcePage(heatmapDim, "The annotation column must not contain " +
                        "numeric values");
                addPage(dataSourceAnnotationPage);
            }
        }

        configPage = new HeatmapHeaderConfigPage(header);
        addPage(configPage);

        colorScalePage = new ColorScalePage(header);
        addPage(colorScalePage);

        textConfigPage = new TextLabelsConfigPage(header);
        addPage(textConfigPage);

    }

    public boolean isLastPage(IWizardPage page) {
        if (page == this.colorScalePage) {
            if (!header.isLabelVisible() && page == this.colorScalePage)
                return true;
            else
                return false;
        } else {
            return super.isLastPage(page);
        }
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage;
        if (page == this.dataSourceAggregationPage) {

            if (!this.dataSourceAggregationPage.aggregateAnnotationsSeparately()) {
                generateHeaderHeatmap();
                nextPage = configPage;
            } else {
                nextPage = super.getNextPage(page);
            }
            
        } else if(dataSource == DataSourceEnum.aggregatedData && page == this.dataSourceAnnotationPage) {
            generateHeaderHeatmap();
            nextPage = super.getNextPage(page);

        } else if (dataSource == DataSourceEnum.annotation && page == this.dataSourceAnnotationPage) {
            try {
                headerValueHeatmap = annotationToHeatmap();
            } catch (NumberFormatException  e) {
                dataSourceAnnotationPage.setMessage(MessageStatus.ERROR,"The selected annotation contains non-numeric values.");
                return page;
            }
            dataSourceAnnotationPage.setMessage(MessageStatus.INFO,dataSourceAnnotationPage.infoMessage);
            header.setHeaderHeatmap(headerValueHeatmap);

            //generate Label to id map
            Map<String, Integer> map = generateMap(headerValueHeatmap.getMatrixView());
            header.setLabelIndexMap(map);

            // generate title
            header.setTitle("Data: " + dataSourceAnnotationPage.getSelectedAnnotation());

            nextPage = super.getNextPage(page);
            
        } else if (page == this.configPage) {

            textConfigPage.setFgColorEnabled(header.isForceLabelColor() ||
                    header.getLabelPosition() != HeatmapDataHeatmapHeader.LabelPositionEnum.inside);
            nextPage = super.getNextPage(page);
            
        } else {
            nextPage = super.getNextPage(page);
        }
        return nextPage;
    }

    private void generateHeaderHeatmap() {
        boolean useAll = dataSourceAggregationPage.useAllColumnsOrRows();

        if (dataSourceAggregationPage.aggregateAnnotationsSeparately()) {
            getIndecesByAnnotation();
        } else {
            //get all indices
            setAggregationTitles(new String[]{"All Cols"});
            Map<String,int[]> indicesMap = new HashMap<String,int[]>();
            int[] aggregationGroups = new int[0];

            if (applyToRows) {
                if (useAll) {
                    aggregationGroups = new int[heatmap.getMatrixView().getColumnCount()];
                    for (int i = 0; i < heatmap.getMatrixView().getColumnCount(); i++)
                        aggregationGroups[i] = i;
                } else {
                    aggregationGroups = heatmap.getMatrixView().getSelectedColumns();
                }
            } else {
                if (useAll) {

                } else
                 if (useAll) {
                     aggregationGroups = new int[heatmap.getMatrixView().getRowCount()];
                     for (int i = 0; i < heatmap.getMatrixView().getRowCount(); i++)
                         aggregationGroups[i] = i;
                 } else {
                     aggregationGroups = heatmap.getMatrixView().getSelectedRows();
                 }
            }
            indicesMap.put(aggregationTitles[0],aggregationGroups);
            setDataIndicesToAggregateByTitle(indicesMap);
            headerValueHeatmap = aggregateToHeatmap();
            header.setHeaderHeatmap(headerValueHeatmap);
            //generate Label to id map
            Map<String, Integer> map = generateMap(headerValueHeatmap.getMatrixView());
            header.setLabelIndexMap(map);
        }

        // generate title
        StringBuilder sb = new StringBuilder("Data: ");
        sb.append(dataSourceAggregationPage.getDataAggregator().toString());
        sb.append(" of ");
        sb.append(dataSourceAggregationPage.getSelectedDataValueName());
        header.setTitle(sb.toString());
    }

    private Map<String, Integer> generateMap(IMatrixView data) {
        Map<String,Integer> map = new HashMap<String, Integer>();
        if (applyToRows) {
            for (int i : data.getVisibleRows())
                map.put(data.getRowLabel(i),i);
        } else {
            for (int i : data.getVisibleColumns())
                map.put(data.getColumnLabel(i),i);
        }
        return map;
    }


    private Heatmap aggregateToHeatmap() {


        int elementsToAggregate;
        String[] columnNames;
        String[] rowNames;
        IAggregator aggregator = dataSourceAggregationPage.getDataAggregator();
        int valueIndex = dataSourceAggregationPage.getSelectedDataValueIndex();
        DoubleMatrix2D valueMatrix;

        if (applyToRows) {

            int[] rows = heatmap.getMatrixView().getVisibleRows();
            valueMatrix = DoubleFactory2D.dense.make(rows.length, aggregationTitles.length, 0.0);

            for (int i = 0; i < aggregationTitles.length; i++) {
                int[] columns = dataIndicesToAggregateByTitle.get(aggregationTitles[i]);

                elementsToAggregate = columns.length;
                final double[] valueBuffer = new double[elementsToAggregate];
    
                for (int j = 0; j < rows.length; j++) {
                    double aggregatedValue = aggregateValue(heatmap.getMatrixView(),columns,j,valueIndex,aggregator,valueBuffer);
                    valueMatrix.set(j,i,aggregatedValue);
                }
            }

            rowNames = new String[rows.length];
            for (int i = 0; i < rows.length; i++)
                rowNames[i] = heatmap.getMatrixView().getRowLabel(i);

            columnNames = aggregationTitles;
                


        } else {

            int[] columns = heatmap.getMatrixView().getVisibleColumns();
            valueMatrix = DoubleFactory2D.dense.make(aggregationTitles.length, columns.length, 0.0);


            for (int i = 0; i < aggregationTitles.length; i++) {
                int[] rows = dataIndicesToAggregateByTitle.get(aggregationTitles[i]);

                elementsToAggregate = rows.length;
                final double[] valueBuffer = new double[elementsToAggregate];

                for (int j = 0; j < columns.length; j++) {
                    double aggregatedValue = aggregateValue(heatmap.getMatrixView(),rows,j,valueIndex,aggregator,valueBuffer);
                    valueMatrix.set(i,j,aggregatedValue);
                }
            }

            columnNames = new String[columns.length];
            for (int i = 0; i < columnNames.length; i++)
                columnNames[i] = heatmap.getMatrixView().getColumnLabel(i);

            rowNames = aggregationTitles;
        }

        return new Heatmap(
                new MatrixView(
                        new DoubleMatrix(
                                "Data Annotation",
                                columnNames,
                                rowNames,
                                valueMatrix)
                ));
    }

    private void getIndecesByAnnotation() {
        IMatrixView mv = heatmap.getMatrixView();
        AnnotationMatrix am = heatmapDim.getAnnotations();
        String pattern = dataSourceAnnotationPage.getSelectedPattern();
        header.setAnnotationPattern(pattern);
        final AggregatedHeatmapHeaderWizard wiz = this;

        final ClusteringData data = applyToRows ?
                new AnnPatColumnClusteringData(mv, am, pattern)
                : new AnnPatRowClusteringData(mv, am, pattern);

        JobThread.execute(AppFrame.instance(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    ClusteringMethod clusteringMethod = new AnnPatClusteringMethod();

                    ClusteringResults results =
                            clusteringMethod.cluster(data, monitor);
                    wiz.setAggregationTitles(results.getClusterTitles());
                    wiz.setDataIndicesToAggregateByTitle(results.getDataIndicesByClusterTitle());

                    headerValueHeatmap = aggregateToHeatmap();
                    header.setHeaderHeatmap(headerValueHeatmap);

                    //generate Label to id map
                    Map<String, Integer> map = generateMap(headerValueHeatmap.getMatrixView());
                    header.setLabelIndexMap(map);

                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });
    }

    private Heatmap annotationToHeatmap() {

        String[] columnNames;
        String[] rowNames;
        DoubleMatrix2D valueMatrix;

        if (applyToRows) {

            AnnotationMatrix annotations = heatmap.getRowDim().getAnnotations();
            int annColIdx = annotations.getColumnIndex(dataSourceAnnotationPage.getSelectedAnnotation());

            int[] rows = heatmap.getMatrixView().getVisibleRows();
            valueMatrix = DoubleFactory2D.dense.make(rows.length, 1, 0.0);

            rowNames = new String[rows.length];
            for (int i = 0; i < rows.length; i++) {
                String rowLabel = heatmap.getMatrixView().getRowLabel(i);
                rowNames[i] = rowLabel;
                int annRowIdx = -1;
                annRowIdx = annotations.getRowIndex(rowLabel);
                Double v = Double.NaN;
                if (annRowIdx >= 0) {
                    v = Double.parseDouble( annotations.getCell(annRowIdx,annColIdx) );
                }
                valueMatrix.set(i,0,v);
            }

            columnNames = new String[1];
            columnNames[0] = dataSourceAnnotationPage.getSelectedAnnotation();

        } else {

            AnnotationMatrix annotations = heatmap.getColumnDim().getAnnotations();
            int annColIdx = annotations.getColumnIndex(dataSourceAnnotationPage.getSelectedAnnotation());

            int[] columns = heatmap.getMatrixView().getVisibleColumns();
            valueMatrix = DoubleFactory2D.dense.make(1, columns.length, 0.0);

            columnNames = new String[columns.length];
            for (int i = 0; i < columnNames.length; i++) {
                String colLabel = heatmap.getMatrixView().getColumnLabel(i);
                columnNames[i] = colLabel;
                int annRowIdx = -1;
                annRowIdx = annotations.getRowIndex(colLabel);
                Double v = Double.NaN;
                if (annColIdx >= 0) {
                    v = Double.parseDouble(annotations.getCell(annRowIdx,annColIdx));
                }
                valueMatrix.set(0,i,v);
            }

            rowNames = new String[1];
            rowNames[0] =  dataSourceAnnotationPage.getSelectedAnnotation();

        }


        return new Heatmap(
                new MatrixView(
                        new DoubleMatrix(
                                "Data Annotation",
                                columnNames,
                                rowNames,
                                valueMatrix)
                ));
    }

    public void setAggregationTitles(String[] aggregationTitles) {
        this.aggregationTitles = aggregationTitles;
    }

    public void setDataIndicesToAggregateByTitle(Map<String, int[]> dataIndicesToAggregateByTitle) {
        this.dataIndicesToAggregateByTitle = dataIndicesToAggregateByTitle;
    }

    @Override
    public boolean canFinish() {
        return currentPage != dataSourceAggregationPage;
    }

    @Override
    public void performFinish() {
        if (!editionMode || header.isLabelVisible() != this.labelVisible) {
            int elements = this.applyToRows ?
                        header.getHeaderHeatmap().getMatrixView().getVisibleColumns().length :
                        header.getHeaderHeatmap().getMatrixView().getVisibleRows().length ;
            int minLabelLenght = header.getLargestLabelLength() * elements;
            if (header.isLabelVisible()) {
                if (HeatmapDataHeatmapHeader.LabelPositionEnum.inside == header.getLabelPosition())
                    header.setSize(minLabelLenght + 5 * elements);
                else
                    header.setSize(minLabelLenght + 14 * elements);

            } else 
                header.setSize(14 * elements);
        }
    }

    @Override
    public void pageLeft(IWizardPage currentPage) {
        super.pageLeft(currentPage);

        if (currentPage != dataSourceAggregationPage || editionMode)
            return;

    }



    public HeatmapDataHeatmapHeader getHeader() {
        return header;
    }

    public void setEditionMode(boolean editionMode) {
        this.editionMode = editionMode;
    }


    private double aggregateValue(
            IMatrixView matrixView,
            int[] selectedIndices,
            int idx,
            int valueIndex,
            IAggregator aggregator,
            double[] valueBuffer) {

        for (int i = 0; i < selectedIndices.length; i++) {
            Object valueObject;
            if (applyToRows)
                valueObject = matrixView.getCellValue(idx, selectedIndices[i], valueIndex);
            else
                valueObject = matrixView.getCellValue(selectedIndices[i], idx, valueIndex);
            valueBuffer[i] = MatrixUtils.doubleValue(valueObject);
        }

        return aggregator.aggregate(valueBuffer);
    }

    public void setDataSource (DataSourceEnum dataSource) {
        this.dataSource = dataSource;
    }


}
