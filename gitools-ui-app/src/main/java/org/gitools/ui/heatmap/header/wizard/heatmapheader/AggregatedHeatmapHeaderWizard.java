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
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.heatmap.header.wizard.TextLabelsConfigPage;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

import java.util.HashMap;
import java.util.Map;

public class AggregatedHeatmapHeaderWizard extends AbstractWizard {

    public enum DataSourceEnum  {
        aggregatedData,
        annotation};

    private DataSourceEnum dataSource;

    private Heatmap heatmap;
    private Heatmap headerValueHeatmap;
    private boolean applyToRows;
    private boolean editionMode;
    private HeatmapDataHeatmapHeader header;


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

    }

    @Override
    public void addPages() {
        if (!editionMode) {
            if (dataSource == DataSourceEnum.aggregatedData) {
                dataSourceAggregationPage = new AggregationDataSourcePage(heatmap, applyToRows);
                addPage(dataSourceAggregationPage);
            }
            else if (dataSource == DataSourceEnum.annotation)  {
                HeatmapDim heatmapDim = applyToRows ? heatmap.getRowDim() : heatmap.getColumnDim();
                dataSourceAnnotationPage = new AnnotationSourcePage(heatmapDim);
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

            // generate heatmap
            headerValueHeatmap = aggregateToHeatmap();
            header.setHeaderHeatmap(headerValueHeatmap);

            //generate Label to id map
            Map<String, Integer> map = generateMap(headerValueHeatmap.getMatrixView());
            header.setLabelIndexMap(map);

            // generate title
            StringBuilder sb = new StringBuilder("Data: ");
            sb.append(dataSourceAggregationPage.getDataAggregator().toString());
            sb.append(" of ");
            sb.append(dataSourceAggregationPage.getSelectedDataValueName());
            header.setTitle(sb.toString());

            nextPage = super.getNextPage(page);
            
        } else if (page == this.dataSourceAnnotationPage) {
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
            header.setTitle("Data: " + dataSourceAnnotationPage.getAnnotation());

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

    private Heatmap annotationToHeatmap() {

        String[] columnNames;
        String[] rowNames;
        DoubleMatrix2D valueMatrix;

        if (applyToRows) {

            AnnotationMatrix annotations = heatmap.getRowDim().getAnnotations();
            int annColIdx = annotations.getColumnIndex(dataSourceAnnotationPage.getAnnotation());

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
            columnNames[0] = dataSourceAnnotationPage.getAnnotation();

        } else {

            AnnotationMatrix annotations = heatmap.getColumnDim().getAnnotations();
            int annColIdx = annotations.getColumnIndex(dataSourceAnnotationPage.getAnnotation());

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
            rowNames[0] =  dataSourceAnnotationPage.getAnnotation();

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

    private Heatmap aggregateToHeatmap() {


        int elementsToAggregate;
        String[] columnNames;
        String[] rowNames;
        IAggregator aggregator = dataSourceAggregationPage.getDataAggregator();
        boolean useAll = dataSourceAggregationPage.useAllColumnsOrRows();
        int valueIndex = dataSourceAggregationPage.getSelectedDataValueIndex();
        DoubleMatrix2D valueMatrix;


        if (applyToRows) {

            int[] columns = useAll ? heatmap.getMatrixView().getVisibleColumns() :
                    heatmap.getMatrixView().getSelectedColumns();
            int[] rows = heatmap.getMatrixView().getVisibleRows();


            elementsToAggregate = columns.length;
            valueMatrix = DoubleFactory2D.dense.make(rows.length, 1, 0.0);
            final double[] valueBuffer = new double[elementsToAggregate];

            for (int i = 0; i < rows.length; i++) {
                double aggregatedValue = aggregateValue(heatmap.getMatrixView(),columns,i,valueIndex,aggregator,valueBuffer);
                valueMatrix.set(i,0,aggregatedValue);
            }

            rowNames = new String[rows.length];
            for (int i = 0; i < rows.length; i++)
                rowNames[i] = heatmap.getMatrixView().getRowLabel(i);


            columnNames = new String[1];
            columnNames[0] = dataSourceAggregationPage.getDataAggregator().toString();


        } else {

            int[] rows = useAll ? heatmap.getMatrixView().getVisibleRows() :
                    heatmap.getMatrixView().getSelectedRows();
            int[] columns = heatmap.getMatrixView().getVisibleColumns();

            elementsToAggregate = rows.length;
            valueMatrix = DoubleFactory2D.dense.make(1, columns.length, 0.0);
            final double[] valueBuffer = new double[elementsToAggregate];
            for (int i = 0; i < columns.length; i++) {
                double aggregatedValue = aggregateValue(heatmap.getMatrixView(),rows,i,valueIndex,aggregator,valueBuffer);
                valueMatrix.set(0,i,aggregatedValue);
            }

            columnNames = new String[columns.length];
            for (int i = 0; i < columnNames.length; i++)
                columnNames[i] = heatmap.getMatrixView().getColumnLabel(i);

            rowNames = new String[1];
            rowNames[0] = dataSourceAggregationPage.getDataAggregator().toString();
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

    @Override
    public boolean canFinish() {
        return currentPage != dataSourceAggregationPage;
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
