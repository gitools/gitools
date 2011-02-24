/*
 *  Copyright 2009 Universitat Pompeu Fabra.
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

package org.gitools.ui.heatmap.panel.details;

import edu.upf.bg.formatter.GenericFormatter;
import edu.upf.bg.colorscale.impl.PValueColorScale;
import edu.upf.bg.colorscale.impl.ZScoreColorScale;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.combination.CombinationResult;
import org.gitools.analysis.correlation.CorrelationResult;
import org.gitools.analysis.overlapping.OverlappingResult;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapDim;
import org.gitools.heatmap.model.HeatmapLabelsHeader;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.gitools.ui.platform.panel.TemplatePanel;
import org.gitools.ui.view.entity.EntityController;

public class HeatmapDetailsController implements EntityController {

	private static final String defaultTemplateName = "/vm/details/noselection.vm";
	private static final String headerTemplateName = "/vm/details/header.vm";
	private static final String heatmapTemplateName = "/vm/details/heatmap.vm";
	
	private TemplatePanel templatePanel;

	protected TemplatePanel getTemplatePanel() {
		if (templatePanel == null) {
			Properties props = new Properties();
			//props.put(VelocityEngine.VM_LIBRARY, "/vm/details/common.vm");
			templatePanel = new TemplatePanel(props);
		}
		return templatePanel;
	}

	@Override
	public JComponent getComponent(Object ctx) {
		TemplatePanel panel = getTemplatePanel();

		final Heatmap heatmap = (Heatmap) ctx;
		final IMatrixView matrixView = heatmap.getMatrixView();
		int row = matrixView.getLeadSelectionRow();
		int rowCount = matrixView.getRowCount();
		int column = matrixView.getLeadSelectionColumn();
		int columnCount = matrixView.getColumnCount();

		VelocityContext context = new VelocityContext();
		context.put("fmt", new GenericFormatter());
		
		String templateName = defaultTemplateName;

		context.put("rowIndex", row + 1);
		context.put("columnIndex", column + 1);

		if (column >= 0 && column < columnCount && row >= 0 && row < rowCount) {
			//final IElementAdapter columnAdapter = matrixView.getColumnAdapter();
			final Object columnId = matrixView.getColumnLabel(column);
			final Object columnLabel = heatmap.getColumnLabel(column);

			//final IElementAdapter rowAdapter = matrixView.getRowAdapter();
			final Object rowId = matrixView.getRowLabel(row);
			final Object rowLabel = heatmap.getRowLabel(row);
			
			final IElementAdapter cellAdapter = matrixView.getCellAdapter();
			final Object cellElement = matrixView.getCell(row, column);

			templateName = getTemplateNameFromObject(cellElement);

			if (templateName != null) {
				context.put("zscoreScale", new ZScoreColorScale()); //FIXME
				context.put("pvalueScale", new PValueColorScale()); //FIXME

				//context.put("columnAdapter", columnAdapter);
				context.put("columnId", columnId);
				context.put("columnLabel", columnLabel);
				context.put("columnElement", columnLabel); //FIXME deprecated

				//context.put("rowAdapter", rowAdapter);
				context.put("rowId", rowId);
				context.put("rowLabel", rowLabel);
				context.put("rowElement", rowLabel); //FIXME deprecated

				context.put("cellAdapter", cellAdapter);
				context.put("cellElement", cellElement);

				final List<IElementAttribute> properties =
					cellAdapter.getProperties();

				final Map<String, Object> cellMap =	new HashMap<String, Object>();

				final Map<String, IElementAttribute> attrMap =
						new HashMap<String, IElementAttribute>();

				for (int index = 0; index < properties.size(); index++) {
					final IElementAttribute prop = properties.get(index);
					cellMap.put(prop.getId(),
							cellAdapter.getValue(cellElement, index));

					attrMap.put(prop.getId(), prop);
				}

				context.put("cell", cellMap);
				context.put("attr", attrMap);
			}
		}
		else if (column == -1 && row == -1) {
			templateName = heatmapTemplateName;
			context.put("title", heatmap.getTitle());
			context.put("notes", heatmap.getDescription());
			context.put("attributes", heatmap.getAttributes());
			context.put("numColumns", matrixView.getColumnCount());
			context.put("numRows", matrixView.getRowCount());
		}
		else if (column == -1 || row == -1) {
			String name = "";
			List<AnnotationMatrix.Annotation> annotations = new ArrayList<AnnotationMatrix.Annotation>(0);
			Map<String, String> links = new HashMap<String, String>();

			if (column >= 0 && column < columnCount) {
				HeatmapDim colDim = heatmap.getColumnDim();
				HeatmapLabelsHeader hdr = colDim.getLabelsHeader();
				context.put("linkName", hdr.getLinkName());
				context.put("linkUrl", heatmap.getColumnLinkUrl(column));

				name = heatmap.getColumnLabel(column);
				String label = heatmap.getMatrixView().getColumnLabel(column);
				AnnotationMatrix annMatrix = colDim.getAnnotations();
				if (annMatrix != null)
					annotations = annMatrix.getAnnotations(label);
			}
			else if (row >= 0 && row < rowCount) {
				HeatmapDim rowDim = heatmap.getRowDim();
				HeatmapLabelsHeader hdr = rowDim.getLabelsHeader();
				context.put("linkName", hdr.getLinkName());
				context.put("linkUrl", heatmap.getRowLinkUrl(row));

				name = heatmap.getRowLabel(row);
				String label = heatmap.getMatrixView().getRowLabel(row);
				AnnotationMatrix annMatrix = rowDim.getAnnotations();
				if (annMatrix != null)
					annotations = annMatrix.getAnnotations(label);
			}

			templateName = headerTemplateName;
						
			context.put("name", name);
			context.put("annotations", annotations);
		}

		try {
			panel.setTemplateFromResource(templateName);
			panel.setContext(context);
			panel.render();
		}
		catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			pw.close();
			return new JLabel(sw.toString());
		}

		return panel;
	}

	private String getTemplateNameFromObject(Object object) {
		String templateName = "generic.vm";
		if (object != null) {
			if (object instanceof BinomialResult)
				templateName = "binomial.vm";
			else if (object instanceof FisherResult)
				templateName = "fisher.vm";
			else if (object instanceof ZScoreResult)
				templateName = "zscore.vm";
			else if (object instanceof CorrelationResult)
				templateName = "correlation.vm";
			else if (object instanceof CombinationResult)
				templateName = "combination.vm";
			else if (object instanceof OverlappingResult)
				templateName = "overlapping.vm";
		}
		return "/vm/details/" + templateName;
	}
}
