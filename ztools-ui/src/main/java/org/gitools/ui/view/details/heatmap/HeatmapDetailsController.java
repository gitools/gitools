/*
 *  Copyright 2009 cperez.
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

package org.gitools.ui.view.details.heatmap;

import edu.upf.bg.GenericFormatter;
import edu.upf.bg.colorscale.PValueColorScale;
import edu.upf.bg.colorscale.ZScoreColorScale;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.gitools.model.decorator.impl.AnnotationHeaderDecorator;
import org.gitools.model.figure.HeatmapFigure;
import org.gitools.model.matrix.AnnotationMatrix;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.model.matrix.element.IElementProperty;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.CombinationResult;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;
import org.gitools.ui.panels.TemplatePane;
import org.gitools.ui.view.details.DetailsController;

public class HeatmapDetailsController extends DetailsController {

	private static final String defaultTemplateName = "/vm/details/noselection.vm";
	private static final String headerTemplateName = "/vm/details/header.vm";
	
	private TemplatePane templatePanel;

	protected TemplatePane getTemplatePanel() {
		if (templatePanel == null) {
			Properties props = new Properties();
			props.put(VelocityEngine.VM_LIBRARY, "/vm/details/common.vm");
			templatePanel = new TemplatePane(props);
		}
		return templatePanel;
	}

	@Override
	public JComponent getDetailsComponent(Object ctx) {
		TemplatePane panel = getTemplatePanel();

		final HeatmapFigure heatmap = (HeatmapFigure) ctx;
		final IMatrixView matrixView = heatmap.getMatrixView();
		int row = matrixView.getSelectionLeadRow();
		int rowCount = matrixView.getRowCount();
		int column = matrixView.getSelectionLeadColumn();
		int columnCount = matrixView.getColumnCount();

		VelocityContext context = new VelocityContext();
		context.put("fmt", new GenericFormatter());
		
		String templateName = defaultTemplateName;

		if (column >= 0 && column < columnCount && row >= 0 && row < rowCount) {
			//final IElementAdapter columnAdapter = matrixView.getColumnAdapter();
			final Object columnElement = matrixView.getColumnLabel(column);

			//final IElementAdapter rowAdapter = matrixView.getRowAdapter();
			final Object rowElement = matrixView.getRowLabel(row);

			final IElementAdapter cellAdapter = matrixView.getCellAdapter();
			final Object cellElement = matrixView.getCell(row, column);

			templateName = getTemplateNameFromObject(cellElement);

			if (templateName != null) {
				context.put("zscoreScale", new ZScoreColorScale()); //FIXME
				context.put("pvalueScale", new PValueColorScale()); //FIXME

				//context.put("columnAdapter", columnAdapter);
				context.put("columnElement", columnElement);

				//context.put("rowAdapter", rowAdapter);
				context.put("rowElement", rowElement);

				context.put("cellAdapter", cellAdapter);
				context.put("cellElement", cellElement);

				final List<IElementProperty> properties =
					cellAdapter.getProperties();

				final Map<String, Object> cellMap =
					new HashMap<String, Object>();

				for (int index = 0; index < properties.size(); index++) {
					final IElementProperty prop = properties.get(index);
					cellMap.put(prop.getId(),
							cellAdapter.getValue(cellElement, index));
				}

				context.put("cell", cellMap);
			}
		}
		else if (column == -1 || row == -1) {
			String name = "";
			Map<String, String> annotations = new HashMap<String, String>();
			Map<String, String> links = new HashMap<String, String>();

			if (column >= 0 && column < columnCount) {
				name = heatmap.getColumnLabel(column);
				String label = heatmap.getMatrixView().getColumnLabel(column);
				if (heatmap.getColumnDecorator() instanceof AnnotationHeaderDecorator) {
					AnnotationHeaderDecorator dec = (AnnotationHeaderDecorator) heatmap.getColumnDecorator();
					AnnotationMatrix ann = dec.getAnnotations();
					if (ann != null) {
						int j = ann.getRowIndex(label);
						if (j > 0) {
							for (int i = 0; i < ann.getColumnCount(); i++) {
								annotations.put(
										ann.getColumnLabel(i),
										ann.getCell(j, i));
							}
						}
					}
				}
			}
			else if (row >= 0 && row < rowCount) {
				name = heatmap.getRowLabel(row);
				String label = heatmap.getMatrixView().getRowLabel(row);
				if (heatmap.getRowDecorator() instanceof AnnotationHeaderDecorator) {
					AnnotationHeaderDecorator dec = (AnnotationHeaderDecorator) heatmap.getRowDecorator();
					AnnotationMatrix ann = dec.getAnnotations();
					if (ann != null) {
						int j = ann.getRowIndex(label);
						if (j > 0) {
							for (int i = 0; i < ann.getRowCount(); i++) {
								annotations.put(
										ann.getRowLabel(i),
										ann.getCell(j, i));
							}
						}
					}
				}
			}

			templateName = headerTemplateName;
						
			context.put("name", name);
			context.put("annotations", annotations);
			context.put("links", links);
		}

		try {
			panel.setTemplate(templateName);
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
		String templateName = "default.vm";
		if (object instanceof BinomialResult)
			templateName = "binomial.vm";
		else if (object instanceof FisherResult)
			templateName = "fisher.vm";
		else if (object instanceof ZScoreResult)
			templateName = "zscore.vm";
		else if (object instanceof CombinationResult)
			templateName = "combination.vm";
		else if (object instanceof CommonResult)
			templateName = "common.vm";

		return "/vm/details/" + templateName;
	}
}
