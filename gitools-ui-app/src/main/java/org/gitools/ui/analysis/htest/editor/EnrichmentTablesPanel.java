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
package org.gitools.ui.analysis.htest.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.ModuleMap;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.gitools.utils.color.utils.ColorUtils;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.impl.PValueColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EnrichmentTablesPanel extends AbstractTablesPanel<EnrichmentAnalysis>
{

    private static Logger log = LoggerFactory.getLogger(EnrichmentTablesPanel.class);

    private static final String DATA_TEMPLATE = "/vm/analysis/enrichment/tables_data.vm";
    private static final String RESULTS_TEMPLATE = "/vm/analysis/enrichment/tables_results.vm";

    protected Map<String, Integer> dataRowIndices;
    protected Map<String, Integer> dataColIndices;

    protected ModuleMap mmap;

    @Nullable
    protected IColorScale dataScale;

    public EnrichmentTablesPanel(@NotNull EnrichmentAnalysis analysis, Heatmap heatmap)
    {
        super(analysis, heatmap);

        IMatrix data = analysis.getData().get();
        final int numRows = data.getRowCount();
        final int numCols = data.getColumnCount();

        // index data labels

        dataRowIndices = new HashMap<String, Integer>();
        for (int i = 0; i < numRows; i++)
            dataRowIndices.put(data.getRowLabel(i), i);
        dataColIndices = new HashMap<String, Integer>();
        for (int i = 0; i < numCols; i++)
            dataColIndices.put(data.getColumnLabel(i), i);

        // remap module map to the data

        String[] labels = new String[numRows];
        for (int i = 0; i < labels.length; i++)
            labels[i] = data.getRowLabel(i);

        this.mmap = analysis.getModuleMap().get().remap(labels);

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
                    createDataCellModel(context, mv, row, col, data, mmap);
                }
                else if (row == -1 && col != -1) // column
                {
                    createDataColumnModel(context, mv, col, data, mmap);
                }
                else if (row != -1 && col == -1) // row
                {
                    createDataRowModel(context, mv, row, data, mmap);
                }
                else
                {
                    createDataAllModel(context, mv, data, mmap);
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
    private List<VelocityContext> createDataElements(
            @NotNull IMatrixView mv, int row, int col, @NotNull final IMatrix data, @NotNull ModuleMap mmap)
    {

        final int valueIndex = 0;
        final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
                data.getCellAdapter().getProperty(valueIndex).getValueClass());

        List<VelocityContext> elements = new ArrayList<VelocityContext>();

        final int dcol = dataColIndices.get(mv.getColumnLabel(col));

        String mname = mv.getRowLabel(row);
        int[] indices = mmap.getItemIndices(mname);

        Integer[] iix = new Integer[indices.length];
        for (int i = 0; i < indices.length; i++)
            iix[i] = Integer.valueOf(indices[i]);

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
            if (data.getCell(mri, dcol) != null)
            {
                double value = valueCast.getDoubleValue(
                        data.getCellValue(mri, dcol, valueIndex));

                VelocityContext e = new VelocityContext();
                e.put("name", data.getRowLabel(mri));
                e.put("value", fmt.format(value));
                e.put("color", ColorUtils.colorToRGBHtml(dataScale.valueColor(value)));
                elements.add(e);
            }
        }

        return elements;
    }

    @NotNull
    private VelocityContext createDataTableElements(@NotNull IMatrixView mv, int row, int col, @NotNull IMatrix data, @NotNull ModuleMap mmap)
    {

        List<VelocityContext> elements =
                createDataElements(mv, row, col, data, mmap);

        VelocityContext table = new VelocityContext();
        table.put("name", mv.getRowLabel(row));
        table.put("elements", elements);

        return table;
    }

    @NotNull
    private VelocityContext createDataCellModel(@NotNull VelocityContext context,
                                                @NotNull IMatrixView mv, int row, int col, @NotNull IMatrix data, @NotNull ModuleMap mmap)
    {

        VelocityContext table = createDataTableElements(mv, row, col, data, mmap);
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
    private VelocityContext createDataColumnModel(@NotNull VelocityContext context,
                                                  @NotNull IMatrixView mv, int col, IMatrix data, ModuleMap mmap)
    {

        List<VelocityContext> elements = new ArrayList<VelocityContext>();

        int numRows = mv.getRowCount();

        for (int ri = 0; ri < numRows; ri++)
        {
            VelocityContext e = new VelocityContext();
            e.put("name", mv.getRowLabel(ri));
            elements.add(e);
        }

        VelocityContext table = new VelocityContext();
        table.put("hideValues", true);
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
                                               @NotNull IMatrixView mv, int row, @NotNull IMatrix data, @NotNull ModuleMap mmap)
    {

        List<VelocityContext> elements = new ArrayList<VelocityContext>();

        String mname = mv.getRowLabel(row);
        int[] iix = mmap.getItemIndices(mname);

        for (int ri = 0; ri < iix.length; ri++)
        {
            int mri = iix[ri];

            VelocityContext e = new VelocityContext();
            e.put("name", data.getRowLabel(mri));
            elements.add(e);
        }

        VelocityContext table = new VelocityContext();
        //table.put("name", mv.getRowLabel(row));
        table.put("hideValues", true);
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
                                               IMatrixView mv, IMatrix data, ModuleMap mmap)
    {

        return context;
    }

    @NotNull
    private VelocityContext createHeader(@NotNull IMatrixView mv)
    {
        List<String> headerIds = new ArrayList<String>();
        List<String> headerNames = new ArrayList<String>();
        List<Boolean> isColor = new ArrayList<Boolean>();
        for (IElementAttribute a : mv.getCellAttributes())
        {
            String id = a.getId();
            if (id.equals("distribution"))
            {
                continue;
            }
            else if (id.contains("p-value"))
            {
                headerIds.add(a.getId() + "-color");
                headerNames.add("");
                isColor.add(true);
            }
            headerIds.add(a.getId());
            headerNames.add(a.getName());
            isColor.add(false);
        }

        VelocityContext header = new VelocityContext();
        header.put("ids", headerIds);
        header.put("names", headerNames);
        header.put("isColor", isColor);

        return header;
    }

    @NotNull
    private VelocityContext createResultsElement(@NotNull IMatrixView mv, int row, int col)
    {

        GenericFormatter fmt = new GenericFormatter();

        PValueColorScale pscale = new PValueColorScale();

        VelocityContext e = new VelocityContext();

        for (IElementAttribute a : mv.getCellAttributes())
        {
            String id = a.getId();
            Object value = mv.getCellValue(row, col, id);
            if (id.equals("distribution"))
            {
                continue;
            }
            else if (id.contains("p-value"))
            {
                double pvalue = MatrixUtils.doubleValue(value);
                e.put(id, fmt.pvalue(pvalue));
                e.put(id + "-color", ColorUtils.colorToRGBHtml(pscale.valueColor(pvalue)));
            }
            else
            {
                e.put(id, fmt.format(value));
            }
        }

        return e;
    }

    @NotNull
    private VelocityContext createResultsCellModel(@NotNull VelocityContext context, @NotNull IMatrixView mv, int row, int col)
    {
        VelocityContext e = createResultsElement(mv, row, col);
        e.put("name", mv.getRowLabel(row));

        List<VelocityContext> elements = new ArrayList<VelocityContext>();
        elements.add(e);

        VelocityContext header = createHeader(mv);

        VelocityContext table = new VelocityContext();
        table.put("name", mv.getRowLabel(row));
        table.put("vaCount", ((List) header.get("ids")).size());
        table.put("elements", elements);
        table.put("header", header);

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
    private VelocityContext createResultsColumnModel(@NotNull VelocityContext context, @NotNull IMatrixView mv, int col)
    {

        List<VelocityContext> elements = new ArrayList<VelocityContext>();
        for (int ri = 0; ri < mv.getRowCount(); ri++)
        {
            VelocityContext e = createResultsElement(mv, ri, col);
            e.put("name", mv.getRowLabel(ri));
            elements.add(e);
        }

        VelocityContext header = createHeader(mv);

        VelocityContext table = new VelocityContext();
        table.put("vaCount", ((List) header.get("ids")).size());
        table.put("elements", elements);
        table.put("header", header);

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

        VelocityContext header = createHeader(mv);

        VelocityContext table = new VelocityContext();
        table.put("vaCount", ((List) header.get("ids")).size());
        table.put("elements", elements);
        table.put("header", header);

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

}
