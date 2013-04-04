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
package org.gitools.ui.analysis.correlation.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.CorrelationColorScale;
import org.gitools.utils.colorscale.util.ColorConstants;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CorrelationTablesPanel extends AbstractTablesPanel<CorrelationAnalysis>
{

    private static Logger log = LoggerFactory.getLogger(CorrelationTablesPanel.class);

    private static final String DATA_TEMPLATE = "/vm/analysis/correlation/tables_data.vm";
    private static final String RESULTS_TEMPLATE = "/vm/analysis/correlation/tables_results.vm";

    protected Map<String, Integer> dataColIndices;

    @Nullable
    protected IColorScale dataScale;

    public CorrelationTablesPanel(@NotNull CorrelationAnalysis analysis, @NotNull Heatmap heatmap)
    {
        super(analysis, heatmap);

        // TODO transpose ???
        IMatrix data = analysis.getData().get();
        final int numRows = data.getRowCount();
        final int numCols = data.getColumnCount();

        // index data labels

        dataColIndices = new HashMap<String, Integer>();
        for (int i = 0; i < numCols; i++)
            dataColIndices.put(data.getColumnLabel(i), i);

        // Guess what kind of color scale to use

        dataScale = MatrixUtils.inferScale(data, 0);
    }

    @NotNull
    @Override
    protected VelocityContext createModel()
    {
        IMatrixView mv = heatmap.getMatrixView();
        int row = mv.getLeadSelectionRow();
        int col = mv.getLeadSelectionColumn();

        IMatrix data = analysis.getData().get();

        String template = DATA_TEMPLATE;
        VelocityContext context = new VelocityContext();

        switch (viewMode)
        {
            case DATA_VIEW_MODE:
                template = DATA_TEMPLATE;

                if (row != -1 && col != -1) // cell
                {
                    createDataCellModel(context, mv, row, col, data);
                }
                else if (row == -1 && col != -1) // column
                {
                    createDataColumnModel(context, mv, col, data);
                }
                else if (row != -1 && col == -1) // row
                {
                    createDataRowModel(context, mv, row, data);
                }
                else
                {
                    createDataAllModel(context, mv, data);
                }

                break;

            case RESULTS_VIEW_MODE:
                template = RESULTS_TEMPLATE;

                if (row != -1 && col != -1) // cell
                {
                    createResultsCellModel(context, mv, row, col);
                }
                else if (row == -1 && col != -1) // column
                {
                    createResultsColumnModel(context, mv, col);
                }
                else if (row != -1 && col == -1) // row
                {
                    createResultsRowModel(context, mv, row);
                }
                else
                {
                    createResultsAllModel(context, mv);
                }

                break;
        }

        context.put("__template__", template);
        return context;
    }

    @NotNull
    private List<VelocityContext> createDataCellElements(
            @NotNull IMatrixView mv, int row, int col, @NotNull final IMatrix data)
    {

        final int valueIndex = 0;
        final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
                data.getCellAdapter().getProperty(valueIndex).getValueClass());

        List<VelocityContext> elements = new ArrayList<VelocityContext>();

        final int dcol1 = dataColIndices.get(mv.getColumnLabel(col));
        final int dcol2 = dataColIndices.get(mv.getColumnLabel(row));

        Integer[] iix = new Integer[data.getRowCount()];
        for (int i = 0; i < iix.length; i++)
            iix[i] = i;

        Arrays.sort(iix, new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                double v11 = valueCast.getDoubleValue(
                        data.getCellValue(o1, dcol1, valueIndex));
                double v12 = valueCast.getDoubleValue(
                        data.getCellValue(o2, dcol1, valueIndex));
                double v21 = valueCast.getDoubleValue(
                        data.getCellValue(o1, dcol2, valueIndex));
                double v22 = valueCast.getDoubleValue(
                        data.getCellValue(o2, dcol2, valueIndex));
                return (int) Math.signum((v21 + v22) - (v11 + v12));
            }
        });

        GenericFormatter fmt = new GenericFormatter();

        for (int ri = 0; ri < iix.length; ri++)
        {
            int mri = iix[ri];

            double v1 = Double.NaN;
            Color c1 = ColorConstants.emptyColor;

            if (data.getCell(mri, dcol1) != null)
            {
                v1 = valueCast.getDoubleValue(
                        data.getCellValue(mri, dcol1, valueIndex));
                c1 = dataScale.valueColor(v1);
            }

            double v2 = Double.NaN;
            Color c2 = ColorConstants.emptyColor;

            if (data.getCell(mri, dcol2) != null)
            {
                v2 = valueCast.getDoubleValue(
                        data.getCellValue(mri, dcol2, valueIndex));
                c2 = dataScale.valueColor(v2);
            }

            if (!Double.isNaN(v1) && !Double.isNaN(v2))
            {
                VelocityContext e = new VelocityContext();
                e.put("name", data.getRowLabel(mri));
                e.put("value1", fmt.format(v1));
                e.put("color1", ColorUtils.colorToRGBHtml(c1));
                e.put("value2", fmt.format(v2));
                e.put("color2", ColorUtils.colorToRGBHtml(c2));
                elements.add(e);
            }
        }

        return elements;
    }

    @NotNull
    private List<VelocityContext> createDataColumnElements(
            IMatrixView mv, String colName, @NotNull final IMatrix data)
    {

        final int valueIndex = 0;
        final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
                data.getCellAdapter().getProperty(valueIndex).getValueClass());

        List<VelocityContext> elements = new ArrayList<VelocityContext>();

        final int dcol = dataColIndices.get(colName);

        Integer[] iix = new Integer[data.getRowCount()];
        for (int i = 0; i < iix.length; i++)
            iix[i] = i;

        Arrays.sort(iix, new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                double v1 = valueCast.getDoubleValue(
                        data.getCellValue(o1, dcol, valueIndex));
                double v2 = valueCast.getDoubleValue(
                        data.getCellValue(o2, dcol, valueIndex));
                return (int) Math.signum(v2 - v1);
            }
        });

        GenericFormatter fmt = new GenericFormatter();

        for (int ri = 0; ri < iix.length; ri++)
        {
            int mri = iix[ri];

            double v1 = Double.NaN;
            Color c1 = ColorConstants.emptyColor;

            if (data.getCell(mri, dcol) != null)
            {
                v1 = valueCast.getDoubleValue(
                        data.getCellValue(mri, dcol, valueIndex));
                c1 = dataScale.valueColor(v1);
            }

            if (!Double.isNaN(v1))
            {
                VelocityContext e = new VelocityContext();
                e.put("name", data.getRowLabel(mri));
                e.put("value1", fmt.format(v1));
                e.put("color1", ColorUtils.colorToRGBHtml(c1));
                elements.add(e);
            }
        }

        return elements;
    }

    @NotNull
    private VelocityContext createDataCellModel(@NotNull VelocityContext context,
                                                @NotNull IMatrixView mv, int row, int col, @NotNull IMatrix data)
    {

        List<VelocityContext> elements =
                createDataCellElements(mv, row, col, data);

        VelocityContext table = new VelocityContext();
        table.put("column1", truncateString(mv.getColumnLabel(col), 20));
        table.put("column2", truncateString(mv.getRowLabel(row), 20));
        table.put("elements", elements);

        List<VelocityContext> tables = new ArrayList<VelocityContext>();
        tables.add(table);

        VelocityContext section = new VelocityContext();
        section.put("name", mv.getColumnLabel(col) + " &lt;--&gt; " + mv.getRowLabel(row));
        section.put("tables", tables);

        List<VelocityContext> sections = new ArrayList<VelocityContext>();
        sections.add(section);

        context.put("sections", sections);
        return context;
    }

    @NotNull
    private VelocityContext createDataColumnModel(@NotNull VelocityContext context,
                                                  @NotNull IMatrixView mv, int col, @NotNull IMatrix data)
    {

        List<VelocityContext> elements =
                createDataColumnElements(mv, mv.getColumnLabel(col), data);

        VelocityContext table = new VelocityContext();
        table.put("column1", truncateString(mv.getColumnLabel(col), 20));
        table.put("hideColumn2", true);
        table.put("elements", elements);

        List<VelocityContext> tables = new ArrayList<VelocityContext>();
        tables.add(table);

        VelocityContext section = new VelocityContext();
        section.put("name", mv.getColumnLabel(col));
        section.put("tables", tables);

        List<VelocityContext> sections = new ArrayList<VelocityContext>();
        sections.add(section);

        context.put("sections", sections);
        return context;
    }

    @NotNull
    private VelocityContext createDataRowModel(@NotNull VelocityContext context,
                                               @NotNull IMatrixView mv, int row, @NotNull IMatrix data)
    {

        List<VelocityContext> elements =
                createDataColumnElements(mv, mv.getColumnLabel(row), data);

        VelocityContext table = new VelocityContext();
        table.put("hideColumn2", true);
        table.put("elements", elements);

        List<VelocityContext> tables = new ArrayList<VelocityContext>();
        tables.add(table);

        VelocityContext section = new VelocityContext();
        section.put("name", mv.getRowLabel(row));
        section.put("tables", tables);

        List<VelocityContext> sections = new ArrayList<VelocityContext>();
        sections.add(section);

        context.put("sections", sections);
        return context;
    }

    private VelocityContext createDataAllModel(VelocityContext context,
                                               IMatrixView mv, IMatrix data)
    {

        return context;
    }

    @NotNull
    private VelocityContext createResultsElement(@NotNull IMatrixView mv, int row, int col)
    {

        GenericFormatter fmt = new GenericFormatter();

        if (col < row)
        {
            int tmp = col;
            col = row;
            row = tmp;
        }

        int n = MatrixUtils.intValue(mv.getCellValue(row, col, "n"));
        double score = MatrixUtils.doubleValue(mv.getCellValue(row, col, "score"));
        double se = MatrixUtils.doubleValue(mv.getCellValue(row, col, "se"));

        CorrelationColorScale scale = new CorrelationColorScale();

        VelocityContext e = new VelocityContext();

        e.put("n", n);
        e.put("score", fmt.format(score));
        e.put("score_color", ColorUtils.colorToRGBHtml(scale.valueColor(score)));
        e.put("se", fmt.format(se));

        return e;
    }

    @NotNull
    private VelocityContext createResultsCellModel(@NotNull VelocityContext context, @NotNull IMatrixView mv, int row, int col)
    {
        VelocityContext e = createResultsElement(mv, row, col);
        e.put("name", mv.getRowLabel(row));

        List<VelocityContext> elements = new ArrayList<VelocityContext>();
        elements.add(e);

        VelocityContext table = new VelocityContext();
        table.put("elements", elements);

        List<VelocityContext> tables = new ArrayList<VelocityContext>();
        tables.add(table);

        VelocityContext section = new VelocityContext();
        section.put("name", mv.getColumnLabel(col) + " &lt;--&gt; " + mv.getRowLabel(row));
        section.put("tables", tables);

        List<VelocityContext> sections = new ArrayList<VelocityContext>();
        sections.add(section);

        context.put("sections", sections);
        return context;
    }

    @NotNull
    private VelocityContext createResultsColumnModel(@NotNull VelocityContext context, @NotNull IMatrixView mv, int col)
    {

        List<VelocityContext> elements = new ArrayList<VelocityContext>();
        for (int ri = 0; ri < mv.getRowCount(); ri++)
        {
            VelocityContext e = createResultsElement(mv, ri, col);
            e.put("name", mv.getRowLabel(ri));
            elements.add(e);
        }

        VelocityContext table = new VelocityContext();
        table.put("elements", elements);

        List<VelocityContext> tables = new ArrayList<VelocityContext>();
        tables.add(table);

        VelocityContext section = new VelocityContext();
        section.put("name", mv.getColumnLabel(col));
        section.put("tables", tables);

        List<VelocityContext> sections = new ArrayList<VelocityContext>();
        sections.add(section);

        context.put("sections", sections);
        return context;
    }

    @NotNull
    private VelocityContext createResultsRowModel(@NotNull VelocityContext context, @NotNull IMatrixView mv, int row)
    {
        List<VelocityContext> elements = new ArrayList<VelocityContext>();
        for (int ci = 0; ci < mv.getColumnCount(); ci++)
        {
            VelocityContext e = createResultsElement(mv, row, ci);
            e.put("name", mv.getColumnLabel(ci));
            elements.add(e);
        }

        VelocityContext table = new VelocityContext();
        table.put("elements", elements);

        List<VelocityContext> tables = new ArrayList<VelocityContext>();
        tables.add(table);

        VelocityContext section = new VelocityContext();
        section.put("name", mv.getRowLabel(row));
        section.put("tables", tables);

        List<VelocityContext> sections = new ArrayList<VelocityContext>();
        sections.add(section);

        context.put("sections", sections);
        return context;
    }

    private VelocityContext createResultsAllModel(VelocityContext context, IMatrixView mv)
    {
        return context;
    }

    private String truncateString(@NotNull String s, int len)
    {
        return s.substring(0, Math.min(s.length(), len));
    }
}
