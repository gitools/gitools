/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.ui.analysis.overlapping;

import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.impl.LinearColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.color.utils.ColorUtils;
import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.formatter.GenericFormatter;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OverlappingTablesPanel extends AbstractTablesPanel<OverlappingAnalysis> {

	private static Logger log = LoggerFactory.getLogger(OverlappingTablesPanel.class);

	private static final CutoffCmp PASS_CMP = new CutoffCmp("true", "true", "true") {
		@Override public boolean compare(double value, double cutoff) {
			return true; }
	};

	private static final String DATA_TEMPLATE = "/vm/analysis/overlapping/tables_data.vm";
	private static final String RESULTS_TEMPLATE = "/vm/analysis/overlapping/tables_results.vm";

	protected Map<String, Integer> dataColIndices;

	protected IColorScale dataScale;

	protected CutoffCmp cutoffCmp;
	protected double cutoffValue;
	
	public OverlappingTablesPanel(OverlappingAnalysis analysis, Heatmap heatmap) {
		super(analysis, heatmap);

		// TODO transpose ???
		IMatrix data = analysis.getData();
		final int numRows = data.getRowCount();
		final int numCols = data.getColumnCount();

		// index data labels

		dataColIndices = new HashMap<String, Integer>();
		for (int i = 0; i < numCols; i++)
			dataColIndices.put(data.getColumnLabel(i), i);

		// Guess what kind of color scale to use

		dataScale = MatrixUtils.inferScale(data, 0);

		if (analysis.isBinaryCutoffEnabled()) {
			cutoffCmp = analysis.getBinaryCutoffCmp();
			cutoffValue = analysis.getBinaryCutoffValue();
		}
		else
			cutoffCmp = PASS_CMP;
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
					createDataCellModel(context, mv, row, col, data);
				else if (row == -1 && col != -1) // column
					createDataColumnModel(context, mv, col, data);
				else if (row != -1 && col == -1) // row
					createDataRowModel(context, mv, row, data);
				else
					createDataAllModel(context, mv, data);

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

	private List<VelocityContext> createDataCellElements(
			IMatrixView mv, int row, int col, final IMatrix data) {

		final int valueIndex = 0;
		final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
				data.getCellAttributes().get(valueIndex).getValueClass());

		List<VelocityContext> elements = new ArrayList<VelocityContext>();

		final int dcol1 = dataColIndices.get(mv.getColumnLabel(col));
		final int dcol2 = dataColIndices.get(mv.getColumnLabel(row));

		Integer[] iix = new Integer[data.getRowCount()];
		for (int i = 0; i < iix.length; i++)
			iix[i] = i;

		Arrays.sort(iix, new Comparator<Integer>() {
			@Override public int compare(Integer o1, Integer o2) {
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

		for (int ri = 0; ri < iix.length; ri++) {
			int mri = iix[ri];

			double v1 = Double.NaN;
			boolean f1 = false;
			Color c1 = ColorConstants.emptyColor;

			if (data.getCell(mri, dcol1) != null) {
				v1 = valueCast.getDoubleValue(
						data.getCellValue(mri, dcol1, valueIndex));
				f1 = cutoffCmp.compare(v1, cutoffValue);
				c1 = dataScale.valueColor(v1);
			}

			double v2 = Double.NaN;
			boolean f2 = false;
			Color c2 = ColorConstants.emptyColor;

			if (data.getCell(mri, dcol2) != null) {
				v2 = valueCast.getDoubleValue(
						data.getCellValue(mri, dcol2, valueIndex));
				f2 = cutoffCmp.compare(v2, cutoffValue);
				c2 = dataScale.valueColor(v2);
			}

			if (!Double.isNaN(v1) && f1 && !Double.isNaN(v2) && f2) {
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

	private List<VelocityContext> createDataColumnElements(
			IMatrixView mv, String colName, final IMatrix data) {

		final int valueIndex = 0;
		final MatrixUtils.DoubleCast valueCast = MatrixUtils.createDoubleCast(
				data.getCellAdapter().getProperty(valueIndex).getValueClass());

		List<VelocityContext> elements = new ArrayList<VelocityContext>();

		final int dcol = dataColIndices.get(colName);

		Integer[] iix = new Integer[data.getRowCount()];
		for (int i = 0; i < iix.length; i++)
			iix[i] = i;

		Arrays.sort(iix, new Comparator<Integer>() {
			@Override public int compare(Integer o1, Integer o2) {
				double v1 = valueCast.getDoubleValue(
						data.getCellValue(o1, dcol, valueIndex));
				double v2 = valueCast.getDoubleValue(
						data.getCellValue(o2, dcol, valueIndex));
				return (int) Math.signum(v2 - v1);
			}
		});

		GenericFormatter fmt = new GenericFormatter();

		for (int ri = 0; ri < iix.length; ri++) {
			int mri = iix[ri];

			double v1 = Double.NaN;
			boolean f1 = false;
			Color c1 = ColorConstants.emptyColor;

			if (data.getCell(mri, dcol) != null) {
				v1 = valueCast.getDoubleValue(
						data.getCellValue(mri, dcol, valueIndex));
				f1 = cutoffCmp.compare(v1, cutoffValue);
				c1 = dataScale.valueColor(v1);
			}

			if (!Double.isNaN(v1) && f1) {
				VelocityContext e = new VelocityContext();
				e.put("name", data.getRowLabel(mri));
				e.put("value1", fmt.format(v1));
				e.put("color1", ColorUtils.colorToRGBHtml(c1));
				elements.add(e);
			}
		}

		return elements;
	}

	private VelocityContext createDataCellModel(VelocityContext context,
			IMatrixView mv, int row, int col, IMatrix data) {

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

	private VelocityContext createDataColumnModel(VelocityContext context,
			IMatrixView mv, int col, IMatrix data) {

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

	private VelocityContext createDataRowModel(VelocityContext context,
			IMatrixView mv, int row, IMatrix data) {

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
			IMatrixView mv, IMatrix data) {

		return context;
	}

	private VelocityContext createResultsElement(IMatrixView mv, int row, int col) {

		GenericFormatter fmt = new GenericFormatter();

		if (col < row) {
			int tmp = col;
			col = row;
			row = tmp;
		}

		//TODO
		/*int n = MatrixUtils.intValue(mv.getCellValue(row, col, "n"));
		double score = MatrixUtils.doubleValue(mv.getCellValue(row, col, "score"));
		double se = MatrixUtils.doubleValue(mv.getCellValue(row, col, "se"));*/

		LinearColorScale scale = new LinearColorScale();

		VelocityContext e = new VelocityContext();

		/*e.put("n", n);
		e.put("score", fmt.format(score));
		e.put("score_color", ColorUtils.colorToRGBHtml(scale.valueColor(score)));
		e.put("se", fmt.format(se));*/

		return e;
	}

	private VelocityContext createResultsCellModel(VelocityContext context, IMatrixView mv, int row, int col) {
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

	private VelocityContext createResultsColumnModel(VelocityContext context, IMatrixView mv, int col) {

		List<VelocityContext> elements = new ArrayList<VelocityContext>();
		for (int ri = 0; ri < mv.getRowCount(); ri++) {
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

	private VelocityContext createResultsRowModel(VelocityContext context, IMatrixView mv, int row) {
		List<VelocityContext> elements = new ArrayList<VelocityContext>();
		for (int ci = 0; ci < mv.getColumnCount(); ci++) {
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

	private VelocityContext createResultsAllModel(VelocityContext context, IMatrixView mv) {
		return context;
	}

	private String truncateString(String s, int len) {
		return s.substring(0, Math.min(s.length(), len));
	}
}
