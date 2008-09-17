package es.imim.bg.ztools.zcalc.ui.panels.results;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.zcalc.ui.colormatrix.CellDecorationConfig;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrix;
import es.imim.bg.ztools.zcalc.ui.colormatrix.CellDecoration;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixCellDecorator;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixModel;
import es.imim.bg.ztools.zcalc.ui.colormatrix.DefaultColorMatrixCellDecorator;
import es.imim.bg.ztools.zcalc.ui.panels.results.ResultsConfigPanel.ResultsConfigPanelListener;
import es.imim.bg.ztools.zcalc.ui.panels.results.ResultsToolsPanel.ResultsToolsListener;

public class ResultsPanel extends JPanel {

	private static final long serialVersionUID = -540561086703759209L;

	private static final String defaultParamName = "right-p-value";

	private static final int defaultColorColumnsWidth = 30;
	private static final int defaultValueColumnsWidth = 90;

	private Results results;

	private ResultsToolsPanel toolsPanel;
	private ResultsConfigPanel configPanel;
	
	//private JTable paramValuesTable;
	
	private ColorMatrix colorMatrix;
	private DefaultColorMatrixCellDecorator cellDecorator;
	
	//private int selColIndex;
	//private int selRowIndex;
	
	public ResultsPanel(Results results) {
		
		this.results = results;
		
		//this.selColIndex = this.selRowIndex = -1;
		
		createComponents();
	}

	private void createComponents() {
		
		/* North panel */

		toolsPanel = new ResultsToolsPanel();
		toolsPanel.addListener(new ResultsToolsListener() {
			@Override
			public void selModeChanged() {
				colorMatrix.setSelectionMode(
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
				// TODO Auto-generated method stub
				
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
				// TODO Auto-generated method stub
				
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
				results.getParamNames(),
				results.getParamIndex(defaultParamName));
		
		configPanel.addListener(new ResultsConfigPanelListener() {
			@Override
			public void paramChanged() {
				cellDecorator.setConfig(
						configPanel.getCurrentDecorationConfig());
				
				refreshColorMatrixWidth();
				colorMatrix.refresh();
			}

			@Override
			public void showModeChanged() {
				cellDecorator.setConfig(
						configPanel.getCurrentDecorationConfig());
				
				refreshColorMatrixWidth();
				colorMatrix.refresh();
			}
			
			@Override
			public void formatChanged() {
				colorMatrix.refresh();
			}

			@Override
			public void justificationChanged() {
				colorMatrix.refresh();
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
		
		colorMatrix = new ColorMatrix();
		colorMatrix.setModel(new ColorMatrixModel() {
			@Override
			public int getColumnCount() {
				return results.getData().columns();
			}
			@Override
			public String getColumnName(int column) {
				return results.getColNames()[column];
			}
			@Override
			public int getRowCount() {
				return results.getData().rows();
			}
			@Override
			public String getRowName(int row) {
				return results.getRowNames()[row];
			}
			@Override
			public Double getValue(int row, int column) {
				return results.getDataValue(column, row, 
						configPanel.getSelectedParamIndex());
			}
		});
		
		cellDecorator = new DefaultColorMatrixCellDecorator();
		
		colorMatrix.setCellDecorator(cellDecorator);

		final ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//selRowIndex = colorMatrix.getTableSelectionModel().getMinSelectionIndex();
				//selColIndex = colorMatrix.getColumnSelectionModel().getMinSelectionIndex();
				//paramValuesTable.repaint();
				colorMatrix.refresh();
				//System.err.println(selRowIndex + ", " + selColIndex);
			}
		};
		
		colorMatrix.getTableSelectionModel().addListSelectionListener(listener);
		colorMatrix.getColumnSelectionModel().addListSelectionListener(listener);
		
		configPanel.refresh();
		refreshColorMatrixWidth();
		
		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(northPanel, BorderLayout.NORTH);
		centerPanel.add(colorMatrix, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		//add(leftPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
	}

	private void refreshColorMatrixWidth() {
		CellDecorationConfig config = 
			configPanel.getCurrentDecorationConfig();
		
		colorMatrix.setColumnsWidth(
				config.showColors ? 
						defaultColorColumnsWidth 
						: defaultValueColumnsWidth);
	}
}
