package org.gitools.ui.panels.matrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.matrix.IMatrixView;
import org.gitools.model.matrix.element.IElementAdapter;
import org.gitools.ui.model.SelectionMode;

public class MatrixPanel extends JPanel {

	private static final long serialVersionUID = 1122420366217373359L;

	private JTable table;
	
	private SelectionMode selMode;
	
	private int columnsWidth;
	private int rowsHeight;
	
	private int columnsHeaderHeight;
	private int rowsHeaderWidth;
	
	private int selectedLeadColumn;
	private int selectedLeadRow;
	
	private IMatrixView model;

	private HeaderDecorator columnsDecorator;
	private HeaderDecorator rowsDecorator;
	
	public MatrixPanel() {
		
		this.selMode = SelectionMode.cells;
		
		this.columnsWidth = 18;
		this.rowsHeight = 18;
	
		this.columnsHeaderHeight = 200;
		this.rowsHeaderWidth = 400;
		
		this.selectedLeadColumn = this.selectedLeadRow = -1;
		
		createComponents();
	}
	
	private void createComponents() {
		
		table = new JTable();
		final JScrollPane scroll = new JScrollPane(table);
		
		table.setGridColor(Color.WHITE);
		table.setFillsViewportHeight(true);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionBackground(Color.ORANGE);
		
		final ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					selectedLeadRow = table.getSelectionModel().getLeadSelectionIndex();
					selectedLeadColumn = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
									
					refresh();
				}
			}
		};
		
		table.getSelectionModel().addListSelectionListener(listener);
		table.getColumnModel().getSelectionModel().addListSelectionListener(listener);
		
		ColumnsMouseListener hml = new ColumnsMouseListener(this);
		table.getTableHeader().addMouseMotionListener(hml);
		table.getTableHeader().addMouseListener(hml);
		
		CellMouseListener cml = new CellMouseListener(this);
		table.addMouseMotionListener(cml);
		table.addMouseListener(cml);
		
		//table.getTableHeader().setResizingAllowed(false);
		
		refreshTableColumnsWidth();
		
		refreshTableColumnsHeight();
		
		refreshSelectionMode();
		
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}

	// ------------------------------------------------------------------------
	// refresh
	
	private void refreshSelectionMode() {
		int[] selCols = getSelectedColumns();
		int[] selRows = getSelectedRows();
		
		if (selCols.length > 0 && selRows.length == 0)
			selMode = SelectionMode.columns;
		else if (selCols.length == 0 &&	selRows.length > 0)
			selMode = SelectionMode.rows;
		else
			selMode = SelectionMode.cells;
		
		switch(selMode) {
		case columns:
			table.setRowSelectionAllowed(false);
			table.setColumnSelectionAllowed(true);
			break;
		case rows:
			table.setRowSelectionAllowed(true);
			table.setColumnSelectionAllowed(false);
			break;
		case cells:
			table.setRowSelectionAllowed(false);
			table.setColumnSelectionAllowed(false);
			break;
		}
		
		refreshTableColumnsRenderer();
		refreshTableRowsRenderer();
	}
	
	private void refreshTableColumnsWidth() {
		TableColumnModel colModel = table.getColumnModel();
		final int lastColumn = colModel.getColumnCount() - 1;
		if (lastColumn >= 0) {
			for (int i = 0; i < lastColumn; i++) {
				TableColumn col = colModel.getColumn(i);
				col.setResizable(false);
				col.setMinWidth(columnsWidth);
				col.setPreferredWidth(columnsWidth);
			}
			TableColumn col = colModel.getColumn(lastColumn);
			col.setResizable(false);
			col.setMinWidth(rowsHeaderWidth);
			col.setPreferredWidth(rowsHeaderWidth);
		}
	}
	
	private void refreshTableColumnsHeight() {
		// This is a workaround for bug in Swing:
		// http://bugs.sun.com/view_bug.do?bug_id=4473075
		int w = columnsWidth * 1000 + rowsHeaderWidth;
		table.getTableHeader().setPreferredSize(
				new Dimension(w, columnsHeaderHeight));
		table.getTableHeader().revalidate();
	}
	
	private void refreshTableColumnsRenderer() {
		TableColumnModel colModel = table.getColumnModel();
		final int lastColumn = colModel.getColumnCount() - 1;
		if (lastColumn >= 0) {
			for (int i = 0; i < lastColumn; i++) {
				TableColumn col = colModel.getColumn(i);
				final ColumnsMatrixRenderer renderer = 
					new ColumnsMatrixRenderer(
						columnsDecorator,
						selMode == SelectionMode.columns);
				renderer.setGridColor(table.getGridColor());
				renderer.setShowGrid(isShowGrid());
				col.setHeaderRenderer(renderer);
			}
			TableColumn col = colModel.getColumn(lastColumn);
			col.setHeaderRenderer(new DefaultTableCellRenderer());
		}
	}
	
	private void refreshTableRowsRenderer() {
		if (model != null) {
			//final IElementAdapter adapter = model.getRowAdapter();
			
			table.setDefaultRenderer(
					//adapter.getElementClass(),
					String.class,
					new RowsMatrixRenderer(
							rowsDecorator,
							selMode == SelectionMode.rows));
			
			table.repaint();
		}
	}
	
	public void refresh() {
		table.repaint();
		table.getTableHeader().repaint();
	}
	
	public void refreshColumns() {
		setModel(model);
	}

	public void setModel(IMatrixView model) {
		this.model = model;
		table.setModel(new MatrixModelAdapter(model));

		refreshTableColumnsRenderer();
		refreshTableColumnsWidth();
		table.setRowHeight(rowsHeight);
		
		table.repaint();
	}
	
	// ---------------------------------------------------------------------------
	// decorators
	
	public HeaderDecorator getRowDecorator() {
		return rowsDecorator;
	}
	
	public void setRowDecorator(HeaderDecorator decorator) {
		this.rowsDecorator = decorator;
		refreshTableRowsRenderer();
	}
	
	public HeaderDecorator getColumnDecorator() {
		return columnsDecorator;
	}
	
	public void setColumnDecorator(HeaderDecorator decorator) {
		this.columnsDecorator = decorator;
		refreshTableColumnsRenderer();
	}
	
	public void setCellDecorator(ElementDecorator decorator) {
		
		final IElementAdapter cellAdapter = model.getCellAdapter();
		
		table.setDefaultRenderer(
				cellAdapter.getElementClass(), 
				new CellMatrixRenderer(decorator));
		
		table.repaint();
	}
	
	// -----------------------------------------------------------------------
	// sizes
	
	public int getColumnsWidth() {
		return columnsWidth;
	}
	
	public int getRowsHeight() {
		return rowsHeight;
	}
	
	public void setColumnsWidth(int columnsWidth) {
		this.columnsWidth = columnsWidth;
		refreshTableColumnsWidth();
	}
	
	public int getColumnsHeaderHeight() {
		return columnsHeaderHeight;
	}
	
	public void setColumnsHeaderHeight(int columnsHeight) {
		this.columnsHeaderHeight = columnsHeight;
		refreshTableColumnsHeight();		
	}
	
	public int getRowsHeaderWidth() {
		return rowsHeaderWidth;
	}
	
	public void setRowsHeaderWidth(int rowsHeaderWidth) {
		this.rowsHeaderWidth = rowsHeaderWidth;
		refreshTableColumnsWidth();
	}
	
	public void setRowsHeight(int rowsHeight) {
		this.rowsHeight = rowsHeight;
		table.setRowHeight(rowsHeight);
	}
	
	// --------------------------------------------------------------------------
	// selection
	
	public ListSelectionModel getTableSelectionModel() {
		return table.getSelectionModel();
	}
	
	public ListSelectionModel getColumnSelectionModel() {
		return table.getColumnModel().getSelectionModel();	
	}
	
	public SelectionMode getSelectionMode() {
		return selMode;
	}
	
	public void setSelectionMode(SelectionMode mode) {
		this.selMode = mode;
		refreshSelectionMode();
	}

	public int getSelectedLeadColumn() {
		return selectedLeadColumn;
	}
	
	public int getSelectedLeadRow() {
		return selectedLeadRow;
	}

	public void setLeadSelection(int row, int col) {
		final ListSelectionModel colSelModel = 
			table.getColumnModel().getSelectionModel();
		
		final ListSelectionModel rowSelModel = 
			table.getSelectionModel();
		
		//System.out.println("setLeadSelection");
		
		colSelModel.setValueIsAdjusting(true);
		rowSelModel.setValueIsAdjusting(true);
		
		if (row == -1)
			rowSelModel.setAnchorSelectionIndex(row);
		table.getSelectionModel().setLeadSelectionIndex(row);

		if (col >= -1) {
			if (col == -1)
				colSelModel.setAnchorSelectionIndex(col);
			colSelModel.setLeadSelectionIndex(col);
		}
		else 
			colSelModel.clearSelection();

		
		//System.out.println("setLeadSelection(" + row + ", " + col + ")");
		
		colSelModel.setValueIsAdjusting(false);
		rowSelModel.setValueIsAdjusting(false);
	}
	
	/*public void addListener(ColorMatrixListener listener) {
		listeners.add(listener);
	}*/

	public void setSelectedCells(int[] selectedColumns, int[] selectedRows){
		final ListSelectionModel colSelModel = 
			table.getColumnModel().getSelectionModel();
		
		final ListSelectionModel rowSelModel = 
			table.getSelectionModel();
		
		//System.out.println("setSelectedCells");
		
		colSelModel.setValueIsAdjusting(true);
		rowSelModel.setValueIsAdjusting(true);
		
		updateSelection(
				false,
				colSelModel,
				selectedColumns);
		
		updateSelection(
				false,
				rowSelModel, 
				selectedRows);
		
		colSelModel.setValueIsAdjusting(false);
		rowSelModel.setValueIsAdjusting(false);
		
		refreshSelectionMode();
	}
	
	public int[] getSelectedColumns() {
		return table.getSelectedColumns();
	}
		
	public void setSelectedColumns(int[] selectedColumns) {
		//System.out.println("setSelectedColumns");
		
		updateSelection(
				true,
				table.getColumnModel().getSelectionModel(),
				selectedColumns);
		
		refreshSelectionMode();
	}
	
	public int[] getSelectedRows() {
		return table.getSelectedRows();
	}

	public void setSelectedRows(int[] selectedRows) {
		//System.out.println("setSelectedRows");
		
		updateSelection(
				true,
				table.getSelectionModel(), 
				selectedRows);
		
		refreshSelectionMode();
	}
	
	private void updateSelection(
			boolean useValueAdjusting, 
			ListSelectionModel selModel, 
			int[] newList) {
		
		if (useValueAdjusting)
			selModel.setValueIsAdjusting(true);
		
		selModel.clearSelection();
		for (int idx : newList)
			selModel.addSelectionInterval(idx, idx);
		
		if (useValueAdjusting)
			selModel.setValueIsAdjusting(false);
	}

	public void clearSelection() {
		table.clearSelection();
	}

	public void selectAll() {
		clearSelection();
		//setSelectionMode(SelectionMode.rows);

		int lastRowIndex = table.getRowCount() - 1;
		table.getSelectionModel()
			.addSelectionInterval(0, lastRowIndex);
		
		int lastColIndex = table.getColumnCount() - 2;
		table.getColumnModel()
			.getSelectionModel()
			.addSelectionInterval(0, lastColIndex);	
		
		//oldRowSelection = table.getSelectedRows();
	}

	// ------------------------------------------------------------------------------
	// grid
	
	public void setShowGrid(boolean showGrid) {
		table.setShowGrid(showGrid);
		
		int intercellSpacing = showGrid ? 1 : 0;
		table.setIntercellSpacing(new Dimension(intercellSpacing, intercellSpacing));
	}
	
	public boolean isShowGrid() {
		return table.getShowHorizontalLines() 
			|| table.getShowVerticalLines();
	}

	public void setGridColor(Color gridColor) {
		table.setGridColor(gridColor);
		refreshTableColumnsRenderer();
	}
	
	public Color getGridColor() {
		return table.getGridColor();
	}

	public void setCellSize(int width, int height) {
		columnsWidth = width;
		rowsHeight = height;
		refreshTableColumnsWidth();
		table.setRowHeight(rowsHeight);
	}

	public int columnAtPoint(Point point) {
		return table.columnAtPoint(point);
	}

	public int rowAtPoint(Point point) {
		return table.rowAtPoint(point);
	}
	
	public int getColumnCount() {
		return table.getColumnCount();
	}	
	
	/*private void printArray(int[] selectedRows) {
		System.out.println("-------------");
		for (int idx : selectedRows)
			System.out.println(idx);
	}*/	
}
