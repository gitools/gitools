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

package org.gitools.ui.analysis.htest.editor._DEPRECATED;

import org.gitools.analysis.htest.HtestAnalysis;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapLabelsHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.ui.platform.editor.MultiEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;

@Deprecated
public class AnalysisEditor extends MultiEditor {

	private static final long serialVersionUID = 5866176431409745805L;

	protected HtestAnalysis analysis;
	
	private HtestAnalysisEditor detailsView;
	private HeatmapEditor dataView;
	private HeatmapEditor resultsView;
	
	public AnalysisEditor(HtestAnalysis analysis) {
		super();
		
		this.analysis = analysis;
		
		createViews();
	}
	
	private void createViews() {
		// create details panel
		
		detailsView = new HtestAnalysisEditor(analysis);
		addView(detailsView, "Description");
		
		// create data view
		
		if (analysis.getData() != null) {
			IMatrixView dataTable = new MatrixView(analysis.getData());
			
			ElementDecorator dataRowDecorator = 
				ElementDecoratorFactory.create(
						ElementDecoratorNames.BINARY, 
						dataTable.getCellAdapter());
			
			dataView = new HeatmapEditor(
					new Heatmap(dataTable, dataRowDecorator,
							new HeatmapLabelsHeader(), new HeatmapLabelsHeader()));
			
			addView(dataView, "Data");
		}
		
		// create results view
		
		IMatrixView resultsTable = new MatrixView(analysis.getResults());
		
		ElementDecorator resultsRowDecorator = 
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE, 
					resultsTable.getCellAdapter());
		
		resultsView = new HeatmapEditor(
				new Heatmap(resultsTable, resultsRowDecorator,
						new HeatmapLabelsHeader(), new HeatmapLabelsHeader()));

		addView(resultsView, "Results");
	}
}
