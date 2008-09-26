package es.imim.bg.ztools.ui.panels.results;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixModel;
import es.imim.bg.ztools.ui.colormatrix.DefaultColorMatrixCellDecorator;
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.panels.results.ResultsConfigPanel.ResultsConfigPanelListener;
import es.imim.bg.ztools.ui.panels.results.ResultsToolsPanel.ResultsToolsListener;

public class ResultsPanel extends JPanel {

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
	
	//private int selColIndex;
	//private int selRowIndex;
	
	public ResultsPanel(ResultsModel resultsModel) {
		
		this.resultsModel = resultsModel;
		
		//this.selColIndex = this.selRowIndex = -1;
		
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

			@Override
			public void selectAll() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void unselectAll() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void invertSelection() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void hideColumns() {
				resultsModel.removeColumns(
						colorMatrixPanel.getSelectedColumns());
			}

			@Override
			public void moveColumnsLeft() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void moveColumnsRight() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void sortColumns() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void hideRows() {
				resultsModel.removeRows(
						colorMatrixPanel.getSelectedRows());
				colorMatrixPanel.clearSelection();
				colorMatrixPanel.refresh();
			}

			@Override
			public void moveRowsUp() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void moveRowsDown() {
				// TODO Auto-generated method stub
				
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
		
		/* Left panel */
		
		/*
		paramValuesTable = new JTable();
		paramValuesTable.setFillsViewportHeight(true);
		paramValuesTable.setModel(new TableModel() {
			
			@Override public int getColumnCount() { return 2; }

			@Override public int getRowCount() { return results.getParamNames().length; }
			
			@Override public String getColumnName(int columnIndex) {
				switch(columnIndex) {
				case 0: return "name";
				case 1: return "value";
				}
				return null;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0: 
					return results.getParamNames()[rowIndex];
				case 1: 
					return selColIndex != -1 && selRowIndex != -1 ? 
							results.getDataValue(selColIndex, selRowIndex, rowIndex)
							: "";
				}
				return null;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) { return String.class; }
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

			@Override 
			public void addTableModelListener(TableModelListener l) { }
			
			@Override
			public void removeTableModelListener(TableModelListener l) { }

			@Override
			public void setValueAt(Object value, int rowIndex, int columnIndex) { }
			
		});
		
		final JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		
		final JScrollPane scrPanel = new JScrollPane(paramValuesTable);
		leftPanel.add(scrPanel, BorderLayout.CENTER);
		*/
		
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
}
