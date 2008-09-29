package es.imim.bg.ztools.ui.views.results;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.Actions;
import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixModel;
import es.imim.bg.ztools.ui.colormatrix.DefaultColorMatrixCellDecorator;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.results.ResultsConfigPanel.ResultsConfigPanelListener;
import es.imim.bg.ztools.ui.views.results.ResultsToolsPanel.ResultsToolsListener;

public class ResultsView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	private static final String defaultParamName = "right-p-value";

	private static final int defaultColorColumnsWidth = 30;
	private static final int defaultValueColumnsWidth = 90;

	//private Results results;
	private ResultsModel resultsModel;
	
	private ResultsToolsPanel toolsPanel;
	private ResultsConfigPanel configPanel;
	
	private ColorMatrixPanel colorMatrixPanel;
	private DefaultColorMatrixCellDecorator cellDecorator;

	public ResultsView(ResultsModel resultsModel) {
		
		this.resultsModel = resultsModel;
			
		createComponents();
	}

	private void createComponents() {
		
		/* North panel */

		toolsPanel = new ResultsToolsPanel();
		toolsPanel.addListener(new ResultsToolsListener() {
			@Override
			public void selModeChanged() {
				colorMatrixPanel.setSelectionMode(
						toolsPanel.getSelMode());
			}
		});
		
		configPanel = new ResultsConfigPanel(
				resultsModel.getParamNames(),
				resultsModel.getParamIndex(defaultParamName));
		
		configPanel.addListener(new ResultsConfigPanelListener() {
			@Override
			public void paramChanged() {
				cellDecorator.setConfig(
						configPanel.getCurrentDecorationConfig());
				
				refreshColorMatrixWidth();
				colorMatrixPanel.refresh();
			}

			@Override
			public void showModeChanged() {
				cellDecorator.setConfig(
						configPanel.getCurrentDecorationConfig());
				
				refreshColorMatrixWidth();
				colorMatrixPanel.refresh();
			}
			
			@Override
			public void formatChanged() {
				colorMatrixPanel.refresh();
			}

			@Override
			public void justificationChanged() {
				colorMatrixPanel.refresh();
			}
		});
		
		final JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(toolsPanel);
		northPanel.add(configPanel);
		
		/* Color matrix */
		
		colorMatrixPanel = new ColorMatrixPanel();
		colorMatrixPanel.setModel(new ColorMatrixModel() {
			@Override
			public int getColumnCount() {
				return resultsModel.getColumnCount();
			}
			@Override
			public String getColumnName(int column) {
				return resultsModel.getColumnName(column);
			}
			@Override
			public int getRowCount() {
				return resultsModel.getRowCount();
			}
			@Override
			public String getRowName(int row) {
				return resultsModel.getRowName(row);
			}
			@Override
			public Double getValue(int row, int column) {
				return resultsModel.getValue(column, row, 
						configPanel.getSelectedParamIndex());
			}
		});
		
		cellDecorator = new DefaultColorMatrixCellDecorator();
		
		colorMatrixPanel.setCellDecorator(cellDecorator);
		
		colorMatrixPanel.getTableSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				resultsModel.setSelectedRows(
						colorMatrixPanel.getSelectedRows());
			}
		});
		colorMatrixPanel.getColumnSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				resultsModel.setSelectedColumns(
						colorMatrixPanel.getSelectedColumns());
			}
		});
		
		toolsPanel.setSelMode(colorMatrixPanel.getSelectionMode());
		configPanel.refresh();
		refreshColorMatrixWidth();
		
		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(northPanel, BorderLayout.NORTH);
		centerPanel.add(colorMatrixPanel, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		//add(leftPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
	}

	private void refreshColorMatrixWidth() {
		CellDecorationConfig config = 
			configPanel.getCurrentDecorationConfig();
		
		colorMatrixPanel.setColumnsWidth(
				config.showColors ? 
						defaultColorColumnsWidth 
						: defaultValueColumnsWidth);
	}
	
	public ColorMatrixPanel getColorMatrixPanel() {
		return colorMatrixPanel;
	}
	
	public ResultsModel getResultsModel() {
		return resultsModel;
	}

	@Override
	public Object getModel() {
		return resultsModel;
	}

	@Override
	public void refresh() {
		colorMatrixPanel.refresh();
	}
	
	@Override
	public void enableActions() {
		Actions.selectAllAction.setEnabled(true);
		Actions.invertSelectionAction.setEnabled(true);
		Actions.unselectAllAction.setEnabled(true);
		//Actions.hideSelectedColumnsAction.setEnabled(true);
		Actions.sortSelectedColumnsAction.setEnabled(true);
		Actions.hideSelectedRowsAction.setEnabled(true);
		
		toolsPanel.refresh();
	}
	
	@Override
	public void disableActions() {
		Actions.selectAllAction.setEnabled(false);
		Actions.invertSelectionAction.setEnabled(false);
		Actions.unselectAllAction.setEnabled(false);
		//Actions.hideSelectedColumnsAction.setEnabled(false);
		Actions.sortSelectedColumnsAction.setEnabled(false);
		Actions.hideSelectedRowsAction.setEnabled(false);
	}

	public int getSelectedParamIndex() {
		return configPanel.getSelectedParamIndex();
	}
}
