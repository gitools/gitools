/*
 *  Copyright 2010 chris.
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

package org.gitools.ui.analysis.htest.editor;

import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.impl.PValueColorScale;
import edu.upf.bg.colorscale.util.ColorUtils;
import edu.upf.bg.formatter.GenericFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.model.ModuleMap;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OncodriveTablesPanel extends AbstractTablesPanel<OncodriveAnalysis> {

	private static Logger log = LoggerFactory.getLogger(EnrichmentTablesPanel.class);

	private static final String DATA_TEMPLATE = "/vm/analysis/oncodrive/tables_data.vm";
	private static final String RESULTS_TEMPLATE = "/vm/analysis/oncodrive/tables_results.vm";

	protected final int dataValueIndex = 0;

	protected Map<String, Integer> dataRowIndices;
	protected Map<String, Integer> dataColIndices;

	protected ModuleMap mmap;

	protected IColorScale dataScale;

	public OncodriveTablesPanel(OncodriveAnalysis analysis, Heatmap heatmap) {
		super(analysis, heatmap);

		IMatrix data = analysis.getData();
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

		String[] labels = new String[numCols];
		for (int i = 0; i < labels.length; i++)
			labels[i] = data.getColumnLabel(i);

		mmap = analysis.getModuleMap();
		if (mmap != null)
			mmap = mmap.remap(labels);
		else
			mmap = new ModuleMap("All data columns", labels);

		// Guess what kind of color scale to use

		dataScale = MatrixUtils.inferScale(data, 0);
	}

	@Override
	protected VelocityContext createModel() {
		IMatrixView mv = heatmap.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		IMatrix data = analysis.getData();

		String template = DATA_TEMPLATE;
		VelocityContext context = new VelocityContext();

		switch (viewMode) {
			case DATA_VIEW_MODE:
				template = DATA_TEMPLATE;

				if (row != -1 && col != -1) // cell
					createDataCellModel(context, mv, row, col, data, mmap);
				else if (row == -1 && col != -1) // column
					createDataColumnModel(context, mv, col, data, mmap);
				else if (row != -1 && col == -1) // row
					createDataRowModel(context, mv, row, data, mmap);
				else
					createDataAllModel(context, mv, data, mmap);

				break;

			case RESULTS_VIEW_MODE:
				template = RESULTS_TEMPLATE;

				if (row != -1 && col != -1) // cell
					createResultsCellModel(context, mv, row, col);
				else if (row == -1 && col != -1) // column
					createResultsColumnModel(context, mv, col);
				else if (row != -1 && col == -1) // row
					createResultsRowModel(context, mv, row);
				else
					createResultsAllModel(context, mv);

				break;
		}

		context.put("__template__", template);
		return context;
	}

	private List<VelocityContext> createDataElements(
			IMatrixView mv, int row, int col, final IMatrix data, ModuleMap mmap) {

		final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
				data.getCellAdapter().getProperty(dataValueIndex).getValueClass());

		List<VelocityContext> elements = new ArrayList<VelocityContext>();

		String mname = mv.getColumnLabel(col);
		int[] indices = mmap.getItemIndices(mname);

		final int drow = dataRowIndices.get(mv.getRowLabel(row));

		Integer[] iix = new Integer[indices.length];
		for (int i = 0; i < indices.length; i++)
			iix[i] = Integer.valueOf(indices[i]);

		Arrays.sort(iix, new Comparator<Integer>() {
			@Override public int compare(Integer o1, Integer o2) {
				double v1 = valueCast.getDoubleValue(
						data.getCellValue(drow, o1, dataValueIndex));
				double v2 = valueCast.getDoubleValue(
						data.getCellValue(drow, o2, dataValueIndex));
				return (int) Math.signum(v2 - v1);
			}
		});

		GenericFormatter fmt = new GenericFormatter();

		for (int ci = 0; ci < iix.length; ci++) {
			int mci = iix[ci];
			if (data.getCell(drow, mci) != null) {
				double value = valueCast.getDoubleValue(
						data.getCellValue(drow, mci, dataValueIndex));

				VelocityContext e = new VelocityContext();
				e.put("name", data.getColumnLabel(mci));
				e.put("value", fmt.format(value));
				e.put("color", ColorUtils.colorToRGBHtml(dataScale.valueColor(value)));
				elements.add(e);
			}
		}

		return elements;
	}

	private VelocityContext createDataTableElements(IMatrixView mv, int row, int col, IMatrix data, ModuleMap mmap) {

		List<VelocityContext> elements =
				createDataElements(mv, row, col, data, mmap);

		VelocityContext table = new VelocityContext();
		table.put("name", mv.getRowLabel(row));
		table.put("elements", elements);

		return table;
	}

	private VelocityContext createDataCellModel(VelocityContext context,
			IMatrixView mv, int row, int col, IMatrix data, ModuleMap mmap) {

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

	private VelocityContext createDataColumnModel(VelocityContext context,
			IMatrixView mv, int col, IMatrix data, ModuleMap mmap) {

		List<VelocityContext> elements = new ArrayList<VelocityContext>();

		String mname = mv.getColumnLabel(col);
		int[] iix = mmap.getItemIndices(mname);

		for (int ci = 0; ci < iix.length; ci++) {
			int mci = iix[ci];

			VelocityContext e = new VelocityContext();
			e.put("name", data.getColumnLabel(mci));
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

	private VelocityContext createDataRowModel(VelocityContext context,
			IMatrixView mv, int row, IMatrix data, ModuleMap mmap) {

		List<VelocityContext> tables = new ArrayList<VelocityContext>();

		for (int col = 0; col < mv.getColumnCount(); col++) {
			List<VelocityContext> elements = new ArrayList<VelocityContext>();

			String mname = mv.getColumnLabel(col);
			int[] iix = mmap.getItemIndices(mname);

			for (int ci = 0; ci < iix.length; ci++) {
				int mci = iix[ci];

				VelocityContext e = new VelocityContext();
				e.put("name", data.getColumnLabel(mci));
				elements.add(e);
			}

			VelocityContext table = new VelocityContext();
			table.put("hideValues", true);
			table.put("elements", elements);

			tables.add(table);
		}

		VelocityContext section = new VelocityContext();
		section.put("name", mv.getRowLabel(row));
		section.put("tables", tables);

		List<VelocityContext> sections = new ArrayList<VelocityContext>();
		sections.add(section);

		context.put("sections", sections);
		return context;
	}

	private VelocityContext createDataAllModel(VelocityContext context,
			IMatrixView mv, IMatrix data, ModuleMap mmap) {

		return context;
	}

	private VelocityContext createResultsElement(IMatrixView mv, int row, int col) {

		GenericFormatter fmt = new GenericFormatter();

		String pvalueAttrName = "right-p-value";
		String cpvalueAttrName = "corrected-right-p-value";

		int pvalueIndex = mv.getCellAdapter().getPropertyIndex(pvalueAttrName);
		double pvalue = MatrixUtils.doubleValue(mv.getCellValue(row, col, pvalueIndex));

		int cpvalueIndex = mv.getCellAdapter().getPropertyIndex(cpvalueAttrName);
		double cpvalue = MatrixUtils.doubleValue(mv.getCellValue(row, col, cpvalueIndex));

		PValueColorScale pscale = new PValueColorScale();

		VelocityContext e = new VelocityContext();

		e.put("pvalue", fmt.pvalue(pvalue));
		e.put("pvalue_color", ColorUtils.colorToRGBHtml(pscale.valueColor(pvalue)));
		e.put("cpvalue", fmt.pvalue(cpvalue));
		e.put("cpvalue_color", ColorUtils.colorToRGBHtml(pscale.valueColor(cpvalue)));

		return e;
	}

	private VelocityContext createResultsCellModel(VelocityContext context, IMatrixView mv, int row, int col) {
		VelocityContext e = createResultsElement(mv, row, col);
		e.put("name", mv.getRowLabel(row));

		List<VelocityContext> elements = new ArrayList<VelocityContext>();
		elements.add(e);

		VelocityContext table = new VelocityContext();
		table.put("name", mv.getRowLabel(row));
		table.put("vaCount", 0);
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

	private VelocityContext createResultsColumnModel(VelocityContext context, IMatrixView mv, int col) {

		List<VelocityContext> elements = new ArrayList<VelocityContext>();
		for (int ri = 0; ri < mv.getRowCount(); ri++) {
			VelocityContext e = createResultsElement(mv, ri, col);
			e.put("name", mv.getRowLabel(ri));
			elements.add(e);
		}

		VelocityContext table = new VelocityContext();
		table.put("vaCount", 0);
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

	private VelocityContext createResultsRowModel(VelocityContext context, IMatrixView mv, int row) {
		List<VelocityContext> elements = new ArrayList<VelocityContext>();
		for (int ci = 0; ci < mv.getColumnCount(); ci++) {
			VelocityContext e = createResultsElement(mv, row, ci);
			e.put("name", mv.getColumnLabel(ci));
			elements.add(e);
		}

		VelocityContext table = new VelocityContext();
		table.put("vaCount", 0);
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

	private VelocityContext createResultsAllModel(VelocityContext context, IMatrixView mv) {
		return context;
	}

}
