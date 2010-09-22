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

package org.gitools.ui.analysis.combination.editor;

import edu.upf.bg.colorscale.util.ColorUtils;
import edu.upf.bg.formatter.GenericFormatter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.combination.CombinationResults;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.ModuleMap;
import org.gitools.ui.platform.panel.TemplatePanel;

public class CombinationPanel extends JPanel implements PropertyChangeListener {

	private static final String CELL_TEMPLATE = "/vm/analysis/combination/report_cell.vm";
	private static final String ROW_TEMPLATE = "/vm/analysis/combination/report_row.vm";
	private static final String COL_TEMPLATE = "/vm/analysis/combination/report_column.vm";

	protected CombinationAnalysis analysis;
	
	protected Heatmap heatmap;

	private TemplatePanel templatePanel;
	
	public CombinationPanel(CombinationAnalysis analysis, Heatmap heatmap) {
		this.analysis = analysis;
		this.heatmap = heatmap;

		createComponents();

		heatmap.getMatrixView().addPropertyChangeListener(new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				CombinationPanel.this.propertyChange(evt); } });
	}

	private void createComponents() {
		templatePanel = new TemplatePanel();
		setLayout(new BorderLayout());
		add(templatePanel);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (evt.getSource() == heatmap.getMatrixView()) {
			if (MatrixView.SELECTED_LEAD_CHANGED.equals(name)) {
				updateContents();
			}
		}
	}

	private void updateContents() {
		IMatrixView mv = heatmap.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		IMatrix data = analysis.getData();
		ModuleMap gmap = analysis.getGroupsMap();

		VelocityContext context = new VelocityContext();

		GenericFormatter fmt = new GenericFormatter();

		try {
			if (row != -1 && col != -1) {
				templatePanel.setTemplate(CELL_TEMPLATE);

				context.put("combinedElements",
						createCellModel(mv, row, col, data, gmap));

				Map<String, Object> cv = new HashMap<String, Object>();
				cv.put("n", fmt.format("%d", mv.getCellValue(row, col, "n")));
				cv.put("pvalue", mv.getCellValue(row, col, "p-value"));
				context.put("cellValues", cv);

				templatePanel.setContext(context);
				templatePanel.render();
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private List<Map<String, Object>> createCellModel(IMatrixView mv, int row, int col, IMatrix data, ModuleMap gmap) {
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

		List<Map<String, Object>> elements = new ArrayList<Map<String, Object>>();

		int numCols = data.getColumnCount();
		String gname = mv.getColumnLabel(col);
		int[] cindices = gmap != null ? gmap.getItemIndices(gname) : new int[numCols];
		if (gmap == null)
			for (int i = 0; i < numCols; i++)
				cindices[i] = i;

		GenericFormatter fmt = new GenericFormatter();

		double sumSize = 0;
		for (int ci = 0; ci < cindices.length; ci++) {
			int mci = cindices[ci];
			if (data.getCell(row, mci) != null) {
				double size = sizeIndex < 0 ? 1
						: sizeCast.getDoubleValue(
							data.getCellValue(row, mci, sizeIndex));

				sumSize += size;

				double pvalue = pvalueCast.getDoubleValue(
						data.getCellValue(row, mci, pvalueIndex));

				Map<String, Object> e = new HashMap<String, Object>();
				e.put("name", data.getColumnLabel(mci));
				e.put("n", (int) size);
				e.put("pvalue", fmt.pvalue(pvalue));
				e.put( "color", ColorUtils.colorToRGBHtml(Color.WHITE));
				elements.add(e);
			}
		}

		for (Map<String, Object> e : elements)
			e.put("weight", fmt.format(((Integer) e.get("n")) / sumSize));

		return elements;
	}
}
