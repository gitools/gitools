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

package org.gitools.ui.analysis.combination.editor;

import edu.upf.bg.colorscale.impl.PValueColorScale;
import edu.upf.bg.colorscale.impl.ZScoreColorScale;
import edu.upf.bg.colorscale.util.ColorUtils;
import edu.upf.bg.formatter.GenericFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.model.ModuleMap;
import org.gitools.ui.analysis.editor.AbstractTablesPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombinationTablesPanel extends  AbstractTablesPanel<CombinationAnalysis> {

	private static Logger log = LoggerFactory.getLogger(CombinationTablesPanel.class);

	private static final String DATA_TEMPLATE = "/vm/analysis/combination/tables_data.vm";
	private static final String RESULTS_TEMPLATE = "/vm/analysis/combination/tables_results.vm";

	protected Map<String, Integer> dataRowIndices;
	protected Map<String, Integer> dataColIndices;

	protected ModuleMap gmap;

	public CombinationTablesPanel(CombinationAnalysis analysis, Heatmap heatmap) {
		super(analysis, heatmap);

		// TODO transposeData !!!
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

		String combOf = analysis.isTransposeData() ? "rows" : "columns";

		gmap = analysis.getGroupsMap();
		if (gmap != null)
			gmap = gmap.remap(labels);
		else {
			IMatrixView mv = heatmap.getMatrixView();
			String[] groups = new String[mv.getColumnCount()];
			for (int i = 0; i < groups.length; i++)
				groups[i] = mv.getColumnLabel(i);

			gmap = new ModuleMap();
			gmap.setModuleNames(groups);
			gmap.setItemNames(labels);
			
			int[] indices = new int[numCols];
			for (int i = 0; i < indices.length; i++)
				indices[i] = i;
			
			for (int i = 0; i < groups.length; i++)
				gmap.setItemIndices(i, indices);
		}
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
					context.put("columns",
							createDataCellModel(mv, row, col, data, gmap));

				else if (row == -1 && col != -1) // column
					context.put("columns",
							createDataColumnModel(mv, col, data, gmap));

				else if (row != -1 && col == -1) // row
					context.put("columns",
							createDataRowModel(mv, row, data, gmap));

				else
					context.put("columns",
							createDataAllModel(mv, data, gmap));
				break;

			case RESULTS_VIEW_MODE:
				template = RESULTS_TEMPLATE;

				if (row != -1 && col != -1) // cell
					context.put("columns",
							createResultsCellModel(mv, row, col));

				else if (row == -1 && col != -1) // column
					context.put("columns",
							createResultsColumnModel(mv, col));

				else if (row != -1 && col == -1) // row
					context.put("columns",
							createResultsRowModel(mv, row));

				else
					context.put("columns",
							createResultsAllModel(mv));

				break;
		}

		context.put("__template__", template);
		return context;
	}

	// Data model

	private List<VelocityContext> createDataElements(IMatrixView mv, int row, int col, IMatrix data, ModuleMap gmap) {
		int sizeIndex = -1;
		String sizeAttrName = analysis.getSizeAttrName();
		/*if (sizeAttrName == null || sizeAttrName.isEmpty())
			sizeIndex = analysis.getSizeAttrIndex();*/
		if (sizeAttrName != null && !sizeAttrName.isEmpty())
			sizeIndex = data.getCellAdapter().getPropertyIndex(sizeAttrName);

		int pvalueIndex = 0;
		String pvalueAttrName = analysis.getPvalueAttrName();
		/*if (pvalueAttrName == null || pvalueAttrName.isEmpty())
			pvalueIndex = analysis.getPvalueAttrIndex();*/
		if (pvalueAttrName != null && !pvalueAttrName.isEmpty())
			pvalueIndex = data.getCellAdapter().getPropertyIndex(pvalueAttrName);

		MatrixUtils.DoubleCast sizeCast = null;

		if (sizeIndex >= 0)
			sizeCast = MatrixUtils.createDoubleCast(
				data.getCellAdapter().getProperty(sizeIndex).getValueClass());

		MatrixUtils.DoubleCast pvalueCast = MatrixUtils.createDoubleCast(
				data.getCellAdapter().getProperty(pvalueIndex).getValueClass());

		List<VelocityContext> elements = new ArrayList<VelocityContext>();

		int numCols = data.getColumnCount();
		String gname = mv.getColumnLabel(col);
		int[] cindices = gmap != null ? gmap.getItemIndices(gname) : new int[numCols];
		if (gmap == null)
			for (int i = 0; i < numCols; i++)
				cindices[i] = i;

		GenericFormatter fmt = new GenericFormatter();
		PValueColorScale scale = new PValueColorScale();

		final int drow = dataRowIndices.get(mv.getRowLabel(row));

		double sumSize = 0;
		for (int ci = 0; ci < cindices.length; ci++) {
			int mci = cindices[ci];
			if (data.getCell(drow, mci) != null) {
				double size = sizeIndex < 0 ? 1
						: sizeCast.getDoubleValue(
							data.getCellValue(drow, mci, sizeIndex));

				sumSize += size;

				double pvalue = pvalueCast.getDoubleValue(
						data.getCellValue(drow, mci, pvalueIndex));

				VelocityContext e = new VelocityContext();
				e.put("name", data.getColumnLabel(mci));
				e.put("n", (int) size);
				e.put("pvalue", fmt.pvalue(pvalue));
				e.put("pvalue_color", ColorUtils.colorToRGBHtml(scale.valueColor(pvalue)));
				elements.add(e);
			}
		}

		for (VelocityContext e : elements)
			e.put("weight", fmt.format(((Integer) e.get("n")) / sumSize));

		return elements;
	}

	private VelocityContext createDataElementsCombination(IMatrixView mv, int row, int col, IMatrix data, ModuleMap gmap) {

		GenericFormatter fmt = new GenericFormatter();

		List<VelocityContext> elements =
				createDataElements(mv, row, col, data, gmap);

		int n = MatrixUtils.intValue(mv.getCellValue(row, col, "N"));
		double pvalue = MatrixUtils.doubleValue(
				mv.getCellValue(row, col, "p-value"));

		PValueColorScale scale = new PValueColorScale();

		VelocityContext combination = new VelocityContext();
		combination.put("name", mv.getRowLabel(row));
		combination.put("n", n);
		combination.put("pvalue", fmt.pvalue(pvalue));
		combination.put("pvalue_color", ColorUtils.colorToRGBHtml(scale.valueColor(pvalue)));
		combination.put("elements", elements);

		return combination;
	}

	private List<VelocityContext> createDataCellModel(IMatrixView mv, int row, int col, IMatrix data, ModuleMap gmap) {
		VelocityContext combination = createDataElementsCombination(mv, row, col, data, gmap);
		List<VelocityContext> combinations = new ArrayList<VelocityContext>();
		combinations.add(combination);
		VelocityContext column = new VelocityContext();
		column.put("name", mv.getColumnLabel(col));
		column.put("combinations", combinations);
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		columns.add(column);
		return columns;
	}

	private List<VelocityContext> createDataColumnModel(IMatrixView mv, int col, IMatrix data, ModuleMap gmap) {
		List<VelocityContext> combinations = new ArrayList<VelocityContext>();
		for (int ri = 0; ri < mv.getRowCount(); ri++) {
			VelocityContext combination = createDataElementsCombination(mv, ri, col, data, gmap);
			combinations.add(combination);
		}
		VelocityContext column = new VelocityContext();
		column.put("name", mv.getColumnLabel(col));
		column.put("combinations", combinations);
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		columns.add(column);
		return columns;
	}

	private List<VelocityContext> createDataRowModel(IMatrixView mv, int row, IMatrix data, ModuleMap gmap) {
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		for (int ci = 0; ci < mv.getColumnCount(); ci++) {
			VelocityContext combination = createDataElementsCombination(mv, row, ci, data, gmap);
			List<VelocityContext> combinations = new ArrayList<VelocityContext>();
			combinations.add(combination);

			VelocityContext column = new VelocityContext();
			column.put("name", mv.getColumnLabel(ci));
			column.put("combinations", combinations);
			columns.add(column);
		}
		return columns;
	}

	private List<VelocityContext> createDataAllModel(IMatrixView mv, IMatrix data, ModuleMap gmap) {
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		for (int ci = 0; ci < mv.getColumnCount(); ci++) {
			List<VelocityContext> combinations = new ArrayList<VelocityContext>();
			for (int ri = 0; ri < mv.getRowCount(); ri++) {
				VelocityContext combination = createDataElementsCombination(mv, ri, ci, data, gmap);
				combinations.add(combination);
			}

			VelocityContext column = new VelocityContext();
			column.put("name", mv.getColumnLabel(ci));
			column.put("combinations", combinations);
			columns.add(column);
		}
		return columns;
	}

	// Results model

	private VelocityContext createResultsCombinationModel(IMatrixView mv, int row, int col) {

		GenericFormatter fmt = new GenericFormatter();

		int n = MatrixUtils.intValue(
				mv.getCellValue(row, col, "n"));
		double zscore = MatrixUtils.doubleValue(
				mv.getCellValue(row, col, "z-score"));
		double pvalue = MatrixUtils.doubleValue(
				mv.getCellValue(row, col, "p-value"));

		PValueColorScale pscale = new PValueColorScale();
		ZScoreColorScale zscale = new ZScoreColorScale();

		VelocityContext combination = new VelocityContext();
		combination.put("n", n);
		combination.put("zscore", fmt.format(zscore));
		combination.put("zscore_color", ColorUtils.colorToRGBHtml(zscale.valueColor(zscore)));
		combination.put("pvalue", fmt.pvalue(pvalue));
		combination.put("pvalue_color", ColorUtils.colorToRGBHtml(pscale.valueColor(pvalue)));

		return combination;
	}

	private List<VelocityContext> createResultsCellModel(IMatrixView mv, int row, int col) {
		List<VelocityContext> combinations = new ArrayList<VelocityContext>();
		VelocityContext combination = createResultsCombinationModel(mv, row, col);
		combination.put("name", mv.getRowLabel(row));
		combinations.add(combination);

		VelocityContext column = new VelocityContext();
		column.put("name", mv.getColumnLabel(col));
		column.put("combinations", combinations);
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		columns.add(column);
		return columns;
	}

	private List<VelocityContext> createResultsColumnModel(IMatrixView mv, int col) {
		List<VelocityContext> combinations = new ArrayList<VelocityContext>();
		for (int ri = 0; ri < mv.getRowCount(); ri++) {
			VelocityContext combination = createResultsCombinationModel(mv, ri, col);
			combination.put("name", mv.getRowLabel(ri));
			combinations.add(combination);
		}

		VelocityContext column = new VelocityContext();
		column.put("name", mv.getColumnLabel(col));
		column.put("combinations", combinations);
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		columns.add(column);
		return columns;
	}

	private List<VelocityContext> createResultsRowModel(IMatrixView mv, int row) {
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		for (int ci = 0; ci < mv.getColumnCount(); ci++) {
			List<VelocityContext> combinations = new ArrayList<VelocityContext>();
			VelocityContext combination = createResultsCombinationModel(mv, row, ci);
			combination.put("name", mv.getColumnLabel(ci));
			combinations.add(combination);
			
			VelocityContext column = new VelocityContext();
			column.put("name", mv.getRowLabel(ci));
			column.put("combinations", combinations);
			columns.add(column);
		}
		return columns;
	}

	private List<VelocityContext> createResultsAllModel(IMatrixView mv) {
		List<VelocityContext> columns = new ArrayList<VelocityContext>();
		for (int ci = 0; ci < mv.getColumnCount(); ci++) {
			List<VelocityContext> combinations = new ArrayList<VelocityContext>();
			for (int ri = 0; ri < mv.getRowCount(); ri++) {
				VelocityContext combination = createResultsCombinationModel(mv, ri, ci);
				combination.put("name", mv.getRowLabel(ri));
				combinations.add(combination);
			}
			VelocityContext column = new VelocityContext();
			column.put("name", mv.getColumnLabel(ci));
			column.put("combinations", combinations);
			columns.add(column);
		}
		return columns;
	}
}
