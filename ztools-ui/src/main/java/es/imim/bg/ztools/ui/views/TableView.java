package es.imim.bg.ztools.ui.views;

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
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel.ColorMatrixListener;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.views.TableViewConfigPanel.TableViewConfigPanelListener;

public class TableView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	//private static final String defaultParamName = "right-p-value";

	private static final int defaultColorColumnsWidth = 30;
	private static final int defaultValueColumnsWidth = 90;

	//private Results results;
	//private ResultsModel resultsModel;
	private ITableModel tableModel;
	
	private TableViewConfigPanel configPanel;
	
	private ColorMatrixPanel colorMatrixPanel;
	private DefaultColorMatrixCellDecorator cellDecorator;

	public TableView(ITableModel tableModel/*ResultsModel resultsModel*/) {
		
		//this.resultsModel = resultsModel;
		this.tableModel = tableModel;
			
		createComponents();
		
		//resultsModel.addPropertyChangeListener(new PropertyChangeListener() {
		/*tableModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ResultsModel.SELECTION_MODE_PROPERTY)) {
					SelectionMode mode = (SelectionMode) evt.getNewValue();
					colorMatrixPanel.setSelectionMode(mode);
					colorMatrixPanel.refresh();
				}
			}
		});*/
	}

	private void createComponents() {
		
		/* North panel */

		configPanel = new TableViewConfigPanel();
		
		configPanel.addListener(new TableViewConfigPanelListener() {
			/*@Override
			public void paramChanged() {
				cellDecorator.setConfig(
						configPanel.getCurrentDecorationConfig());
				
				refreshColorMatrixWidth();
				colorMatrixPanel.refresh();
			}*/

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
		northPanel.add(configPanel);
		
		/* Color matrix */
		
		colorMatrixPanel = new ColorMatrixPanel();
		colorMatrixPanel.setModel(new ColorMatrixModel() {
			@Override
			public int getColumnCount() {
				return tableModel.getColumnCount();
			}
			@Override
			public String getColumnName(int column) {
				return tableModel.getColumnName(column);
			}
			@Override
			public int getRowCount() {
				return tableModel.getRowCount();
			}
			@Override
			public String getRowName(int row) {
				return tableModel.getRowName(row);
			}
			@Override
			public Double getValue(int row, int column) {
				return tableModel.getValue(column, row/*, 
						configPanel.getSelectedParamIndex()*/);
			}
		});
		
		cellDecorator = new DefaultColorMatrixCellDecorator();
		
		colorMatrixPanel.setCellDecorator(cellDecorator);
		
		colorMatrixPanel.getTableSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tableModel.setSelectedRows(
						colorMatrixPanel.getSelectedRows());
			}
		});
		colorMatrixPanel.getColumnSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				tableModel.setSelectedColumns(
						colorMatrixPanel.getSelectedColumns());
			}
		});
		
		colorMatrixPanel.addListener(new ColorMatrixListener() {
			@Override
			public void selectionChanged() {
				int colIndex = colorMatrixPanel.getSelectedLeadColumn();
				int rowIndex = colorMatrixPanel.getSelectedLeadRow();
				
				tableModel.setLeadSelection(colIndex, rowIndex);
			}
		});

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
	
	public ITableModel getTableModel() {
		return tableModel;
	}
	
	public void setTableModel(ITableModel tableModel) {
		this.tableModel = tableModel;
		refresh();
	}

	@Override
	public Object getModel() {
		return tableModel;
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
		Actions.columnSelectionModeAction.setEnabled(true);
		Actions.rowSelectionModeAction.setEnabled(true);
		Actions.cellSelectionModeAction.setEnabled(true);
		//Actions.hideSelectedColumnsAction.setEnabled(true);
		Actions.sortSelectedColumnsAction.setEnabled(true);
		Actions.hideSelectedRowsAction.setEnabled(true);
	}
	
	@Override
	public void disableActions() {
		Actions.selectAllAction.setEnabled(false);
		Actions.invertSelectionAction.setEnabled(false);
		Actions.unselectAllAction.setEnabled(false);
		Actions.columnSelectionModeAction.setEnabled(false);
		Actions.rowSelectionModeAction.setEnabled(false);
		Actions.cellSelectionModeAction.setEnabled(false);
		//Actions.hideSelectedColumnsAction.setEnabled(false);
		Actions.sortSelectedColumnsAction.setEnabled(false);
		Actions.hideSelectedRowsAction.setEnabled(false);
	}

	/*public int getSelectedParamIndex() {
		return configPanel.getSelectedParamIndex();
	}*/
}
