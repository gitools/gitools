package org.gitools.ui.editor.analysis;

import org.gitools.ui.editor.MultiEditor;
import org.gitools.ui.editor.table.MatrixEditor;

import org.gitools.model.Analysis;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.MatrixView;
import org.gitools.model.matrix.adapter.DataMatrixTableContentsAdapter;
import org.gitools.model.matrix.adapter.ResultsMatrixTableContentsAdapter;

public class AnalysisEditor extends MultiEditor {

	private static final long serialVersionUID = 5866176431409745805L;

	protected Analysis analysis;
	
	private AnalysisDetailsEditor detailsView;
	private MatrixEditor dataView;
	private MatrixEditor resultsView;
	
	public AnalysisEditor(Analysis analysis) {
		super();
		
		this.analysis = analysis;
		
		createViews();
	}
	
	private void createViews() {
		// create details panel
		
		detailsView = new AnalysisDetailsEditor(analysis);
		
		// create data view
		
		IMatrixView dataTable = new MatrixView(
				new DataMatrixTableContentsAdapter(
						analysis.getDataTable()));
		
		ElementDecorator dataDecorator = 
			ElementDecoratorFactory.create(
					ElementDecoratorNames.BINARY, 
					dataTable.getCellAdapter());
		
		dataView = new MatrixEditor(new MatrixFigure(dataTable, dataDecorator));
		
		// create results view
		
		IMatrixView resultsTable = new MatrixView(
				new ResultsMatrixTableContentsAdapter(
						analysis.getResults()));
		
		ElementDecorator resultsDecorator = 
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE, 
					resultsTable.getCellAdapter());
		
		resultsView = new MatrixEditor(new MatrixFigure(resultsTable, resultsDecorator));
		
		addView(detailsView, "Description");
		addView(dataView, "Data");
		addView(resultsView, "Results");
	}
}
