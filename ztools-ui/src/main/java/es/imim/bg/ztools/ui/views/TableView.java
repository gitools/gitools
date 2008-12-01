package es.imim.bg.ztools.ui.views;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.actions.EditActionSet;
import es.imim.bg.ztools.ui.actions.FileActionSet;
import es.imim.bg.ztools.ui.actions.TableActionSet;
import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixModel;
import es.imim.bg.ztools.ui.colormatrix.DefaultColorMatrixCellDecorator;
import es.imim.bg.ztools.ui.colormatrix.ColorMatrixPanel.ColorMatrixListener;
import es.imim.bg.ztools.ui.model.ITableModel;
import es.imim.bg.ztools.ui.model.SelectionMode;

public class TableView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	private static final int defaultColorColumnsWidth = 30;
	private static final int defaultValueColumnsWidth = 90;

	private ITableModel tableModel;
	
	private TableViewConfigPanel configPanel;
	
	private ColorMatrixPanel colorMatrixPanel;
	private DefaultColorMatrixCellDecorator cellDecorator;
	
	protected boolean blockSelectionUpdate;

	public TableView(final ITableModel tableModel) {
		
		this.tableModel = tableModel;
		
		this.blockSelectionUpdate = false;
		
		createComponents();
		
		tableModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				tableModelPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		});
	}

	protected void tableModelPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
		if (ITableModel.CELL_DECORATION_PROPERTY.equals(propertyName)) {
			cellDecorator.setConfig(
					tableModel.getCellDecoration());
			
			refreshColorMatrixWidth();
			colorMatrixPanel.refresh();
		}
		else if (ITableModel.SELECTION_MODE_PROPERTY.equals(propertyName)) {
			SelectionMode mode = (SelectionMode) newValue;
			colorMatrixPanel.setSelectionMode(mode);
			colorMatrixPanel.refresh();
			refreshActions();
		}
		else if (ITableModel.SELECTION_PROPERTY.equals(propertyName)
				|| ITableModel.ROWS_CHANGED_PROPERTY.equals(propertyName)
				|| ITableModel.COLS_CHANGED_PROPERTY.equals(propertyName)) {
			
			if (!blockSelectionUpdate) {
				blockSelectionUpdate = true;
				if (ITableModel.COLS_CHANGED_PROPERTY.equals(propertyName))
					colorMatrixPanel.refreshColumns();
				colorMatrixPanel.setSelectedColumns(tableModel.getSelectedColumns());
				colorMatrixPanel.setSelectedRows(tableModel.getSelectedRows());
				colorMatrixPanel.refresh();
				blockSelectionUpdate = false;
			}
		}
		else if (ITableModel.MATRIX_CHANGED_PROPERTY.equals(propertyName)) {
			colorMatrixPanel.refresh();
		}
	}

	private void createComponents() {
		
		/* North panel */

		configPanel = new TableViewConfigPanel(tableModel);
		
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
		
		ListSelectionListener selListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && !blockSelectionUpdate) {
					blockSelectionUpdate = true;
					
					tableModel.setSelection(
							colorMatrixPanel.getSelectedColumns(),
							colorMatrixPanel.getSelectedRows());
					
					int colIndex = colorMatrixPanel.getSelectedLeadColumn();
					int rowIndex = colorMatrixPanel.getSelectedLeadRow();
					
					tableModel.setLeadSelection(colIndex, rowIndex);
					
					blockSelectionUpdate = false;
				}
			}
		};
		
		colorMatrixPanel.getTableSelectionModel().addListSelectionListener(selListener);
		colorMatrixPanel.getColumnSelectionModel().addListSelectionListener(selListener);
		
		/*colorMatrixPanel.addListener(new ColorMatrixListener() {
			@Override
			public void selectionChanged() {
				if (!e.getValueIsAdjusting() && !blockSelectionUpdate) {
					blockSelectionUpdate = true;
					int colIndex = colorMatrixPanel.getSelectedLeadColumn();
					int rowIndex = colorMatrixPanel.getSelectedLeadRow();
					
					tableModel.setLeadSelection(colIndex, rowIndex);
					blockSelectionUpdate = false;
				}
			}
		});*/

		configPanel.refresh();
		refreshColorMatrixWidth();
		
		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(northPanel, BorderLayout.NORTH);
		centerPanel.add(colorMatrixPanel, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
	}

	private void refreshColorMatrixWidth() {
		CellDecorationConfig config = 
			tableModel.getCellDecoration();
		
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
	public void refreshActions() {
		SelectionMode selMode = tableModel.getSelectionMode();
		
		EditActionSet.selectAllAction.setEnabled(
				selMode != SelectionMode.cells);
		
		EditActionSet.invertSelectionAction.setEnabled(
				selMode != SelectionMode.cells);
		
		EditActionSet.unselectAllAction.setEnabled(
				selMode != SelectionMode.cells);
		
		EditActionSet.columnSelectionModeAction.setEnabled(
				selMode != SelectionMode.columns);
		
		EditActionSet.rowSelectionModeAction.setEnabled(
				selMode != SelectionMode.rows);
		
		EditActionSet.cellSelectionModeAction.setEnabled(
				selMode != SelectionMode.cells);
		
		TableActionSet.hideSelectedColumnsAction.setEnabled(
				selMode == SelectionMode.columns);
		
		TableActionSet.sortSelectedColumnsAction.setEnabled(
				selMode == SelectionMode.columns);
		
		TableActionSet.hideSelectedRowsAction.setEnabled(
				selMode == SelectionMode.rows);
		
		TableActionSet.moveRowsUpAction.setEnabled(
				selMode == SelectionMode.rows);
		
		TableActionSet.moveRowsDownAction.setEnabled(
				selMode == SelectionMode.rows);
		
		TableActionSet.moveColsLeftAction.setEnabled(
				selMode == SelectionMode.columns);
		
		TableActionSet.moveColsRightAction.setEnabled(
				selMode == SelectionMode.columns);
		
		FileActionSet.closeAction.setEnabled(true);
		
		TableActionSet.mtcBonferroniAction.setEnabled(true);
		TableActionSet.mtcBenjaminiHochbergFdrAction.setEnabled(true);
	}
}
