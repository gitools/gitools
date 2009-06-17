package org.gitools.ui.editor.analysis;

import org.gitools.model.analysis.Analysis;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.ElementDecoratorFactory;
import org.gitools.model.decorator.ElementDecoratorNames;
import org.gitools.model.decorator.impl.AnnotationHeaderDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.MatrixView;
import org.gitools.ui.editor.MultiEditor;
import org.gitools.ui.editor.matrix.MatrixEditor;

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
		addView(detailsView, "Description");
		
		// create data view
		
		if (analysis.getDataTable() != null) {
			IMatrixView dataTable = new MatrixView(analysis.getDataTable());
			
			ElementDecorator dataRowDecorator = 
				ElementDecoratorFactory.create(
						ElementDecoratorNames.BINARY, 
						dataTable.getCellAdapter());
			
			dataView = new MatrixEditor(
					new MatrixFigure(dataTable, dataRowDecorator,
							new AnnotationHeaderDecorator(), new AnnotationHeaderDecorator()));
			
			addView(dataView, "Data");
		}
		
		// create results view
		
		IMatrixView resultsTable = new MatrixView(analysis.getResults());
		
		ElementDecorator resultsRowDecorator = 
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE, 
					resultsTable.getCellAdapter());
		
		resultsView = new MatrixEditor(
				new MatrixFigure(resultsTable, resultsRowDecorator,
						new AnnotationHeaderDecorator(), new AnnotationHeaderDecorator()));

		addView(resultsView, "Results");
	}
}
