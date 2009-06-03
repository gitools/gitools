package es.imim.bg.ztools.ui.views.analysis;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.Table;
import es.imim.bg.ztools.table.adapter.DataMatrixTableContentsAdapter;
import es.imim.bg.ztools.table.adapter.ResultsMatrixTableContentsAdapter;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.decorator.ElementDecoratorFactory;
import es.imim.bg.ztools.table.decorator.ElementDecoratorNames;
import es.imim.bg.ztools.ui.model.TableViewModel;
import es.imim.bg.ztools.ui.views.TabbedView;
import es.imim.bg.ztools.ui.views.table.TableView;

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
		
		dataView = new TableView(new TableViewModel(dataTable, dataDecorator));
		
		// create results view
		
		ITable resultsTable = new Table(
				new ResultsMatrixTableContentsAdapter(
						analysis.getResults()));
		
		ElementDecorator resultsDecorator = 
			ElementDecoratorFactory.create(
					ElementDecoratorNames.PVALUE, 
					resultsTable.getCellAdapter());
		
		resultsView = new TableView(new TableViewModel(resultsTable, resultsDecorator));
		
		addView(detailsView, "Description");
		addView(dataView, "Data");
		addView(resultsView, "Results");
	}
}
