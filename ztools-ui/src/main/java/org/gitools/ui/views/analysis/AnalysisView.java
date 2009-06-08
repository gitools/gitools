package org.gitools.ui.views.analysis;

import org.gitools.ui.views.TabbedView;
import org.gitools.ui.views.table.TableView;

import org.gitools.model.Analysis;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.table.ITable;
import org.gitools.model.table.Table;
import org.gitools.model.table.adapter.DataMatrixTableContentsAdapter;
import org.gitools.model.table.adapter.ResultsMatrixTableContentsAdapter;
import org.gitools.model.table.decorator.ElementDecorator;
import org.gitools.model.table.decorator.ElementDecoratorFactory;
import org.gitools.model.table.decorator.ElementDecoratorNames;

public class AnalysisView extends TabbedView {

	private static final long serialVersionUID = 5866176431409745805L;

	protected Analysis analysis;
	
	private AnalysisDetailsView detailsView;
	private TableView dataView;
	private TableView resultsView;
	
	public AnalysisView(Analysis analysis) {
		super();
		
		this.analysis = analysis;
		
		createViews();
	}
	
	private void createViews() {
		// create details panel
		
		detailsView = new AnalysisDetailsView(analysis);
		
		// create data view
		
		ITable dataTable = new Table(
				new DataMatrixTableContentsAdapter(
						analysis.getDataTable()));
		
		ElementDecorator dataDecorator = 
			ElementDecoratorFactory.create(
					ElementDecoratorNames.BINARY, 
					dataTable.getCellAdapter());
		
		dataView = new TableView(new MatrixFigure(dataTable, dataDecorator));
		
		// create results view
		
		ITable resultsTable = new Table(
				new ResultsMatrixTableContentsAdapter(
						analysis.getResults()));
		
		ElementDecorator resultsDecorator = 
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE, 
					resultsTable.getCellAdapter());
		
		resultsView = new TableView(new MatrixFigure(resultsTable, resultsDecorator));
		
		addView(detailsView, "Description");
		addView(dataView, "Data");
		addView(resultsView, "Results");
	}
}
