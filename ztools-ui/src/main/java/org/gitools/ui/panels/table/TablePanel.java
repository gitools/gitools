package org.gitools.ui.panels.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.gitools.ui.model.SelectionMode;

import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;

public class TablePanel extends JPanel {

	private static final long serialVersionUID = 1122420366217373359L;

	private static final int rowLabelsWidth = 400;

	protected int[] oldRowSelection = new int[0];//to detect selection changes

	private JTable table;
	
	private SelectionMode selMode;
	
	private int columnsHeight;
	private int columnsWidth;
	private int rowsHeight;
	
	private int selectedLeadColumn;
	private int selectedLeadRow;
	
	private ITable model;
	
	public TablePanel() {
		
		this.selMode = SelectionMode.cells;
		
		this.columnsHeight = 200;
		this.columnsWidth = 18;
		this.rowsHeight = 18;
	
		this.selectedLeadColumn = this.selectedLeadRow = -1;
	
		//this.listeners = new ArrayList<ColorMatrixListener>(1);
		
		createComponents();
	}
	
	//private List<ColorMatrixListener> listeners;
	
	private class HeaderMouseListener extends MouseInputAdapter {

		protected int startColumn;
		protected int endColumn;

		@Override
		public void mousePressed(MouseEvent e) {

			int pressedColumn = table.columnAtPoint(e.getPoint());

			int lastColumn = table.getColumnCount() - 1;

			if (lastColumn == pressedColumn) {
				selectAll();
				// for (ColorMatrixListener listener : listeners)
				// listener.selectionAll();
			} 
			else {
				setSelectionMode(SelectionMode.columns);

				startColumn = pressedColumn;
				int[] selectedColumns;

				if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK) {
					int nb = getSelectedColumns().length + 1;
					selectedColumns = new int[nb];
					System.arraycopy(getSelectedColumns(), 0, selectedColumns, 0, nb - 1);
					selectedColumns[nb - 1] = pressedColumn;
				} else
					selectedColumns = new int[] { pressedColumn };

				setSelectedCells(selectedColumns, new int[0]);
				setLeadSelection(-1, pressedColumn);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			endColumn = table.columnAtPoint(e.getPoint());
			setLeadSelection(-1, endColumn);

			int numCols = Math.abs(endColumn - startColumn) + 1;
			
			int[] newSelectedColumns = new int[numCols];

			int start = (endColumn < startColumn) ? endColumn : startColumn;
			int stop = (endColumn > startColumn) ? endColumn : startColumn;
			int counter = 0;
			for (int i = start; i <= stop; i++) {
				newSelectedColumns[counter] = i;
				counter++;
			}

			int[] selectedColumns;
			if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK)
				selectedColumns = mergeArrays(getSelectedColumns(),	newSelectedColumns);
			else
				selectedColumns = newSelectedColumns;

			setSelectedCells(selectedColumns, new int[0]);
		}
	}
	
	private class CellMouseListener extends MouseInputAdapter {
			
		protected int startRow;
		protected int endRow;
		protected int startColumn;
		protected int endColumn;
		
		@Override
		public void mousePressed(MouseEvent e) {
			int pressedColumn = table.columnAtPoint(e.getPoint());
			
			int lastcol = table.getColumnCount() - 1;
			if (lastcol == pressedColumn) {
				setSelectionMode(SelectionMode.rows);
				startRow = table.rowAtPoint(e.getPoint());
				startColumn = pressedColumn;
				setLeadSelection(startRow, -1);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			int lastColumn = table.getColumnCount() - 1;
			if (lastColumn == table.columnAtPoint(e.getPoint())) {
				setSelectedColumns(new int[0]);
				oldRowSelection = getSelectedRows();
			}
			else
				setLead(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int pressedColumn = table.columnAtPoint(e.getPoint());
			
			int lastColumn = table.getColumnCount() - 1;
			if (lastColumn == pressedColumn) {
				
				endRow = table.rowAtPoint(e.getPoint());
				endColumn = pressedColumn;
				setLeadSelection(endRow, -1);

				int rows = Math.abs(endRow-startRow) + 1;
				
				int[] newSelectedRows = new int[rows];
				
				int start = (endRow < startRow) ? endRow : startRow;
				int stop = (endRow > startRow) ? endRow : startRow;
				int counter = 0;
				for (int i = start; i <= stop ; i++){
					newSelectedRows[counter] = i;
					counter++;
				}
				
				int[] selectedRows;
				
				if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK)
					selectedRows = mergeArrays(getSelectedRows(), newSelectedRows);					
				else
					selectedRows = newSelectedRows;
							
				setSelectedCells(new int[0], selectedRows);
				oldRowSelection = getSelectedRows();
			}
		}
		
		private void setLead(MouseEvent e){
			setSelectionMode(SelectionMode.cells);
			
			int row = table.rowAtPoint(e.getPoint());
			int col = table.columnAtPoint(e.getPoint());
			
			setSelectedCells(new int[] { col }, new int[] { row });
			setLeadSelection(row, col);
		}
	}

	private void createComponents() {
		
		table = new JTable();
		
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
		
		HeaderMouseListener hml = new HeaderMouseListener();
		table.getTableHeader().addMouseMotionListener(hml);
		table.getTableHeader().addMouseListener(hml);
		
		//table.getTableHeader().setResizingAllowed(false);
		
		CellMouseListener cml = new CellMouseListener();
		table.addMouseListener(cml);
		table.addMouseMotionListener(cml);
		
		table.getTableHeader().setPreferredSize(new Dimension(columnsWidth, columnsHeight));
		refreshTableColumnsWidth();
		
		refreshSelectionMode();
		
		final JScrollPane scroll = new JScrollPane(table);
		table.getTableHeader().setOpaque(true);
		
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}
	
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
	}
	
	private void refreshTableColumnsWidth() {
		TableColumnModel colModel = table.getColumnModel();
		final int lastColumn = colModel.getColumnCount() - 1;
		if (lastColumn >= 0) {
			for (int i = 0; i < lastColumn; i++) {
				TableColumn col = colModel.getColumn(i);
				col.setResizable(false);
				col.setPreferredWidth(columnsWidth);
			}
			TableColumn col = colModel.getColumn(lastColumn);
			//col.setResizable(false);
			col.setMinWidth(rowLabelsWidth);
		}
	}
	
	private void refreshTableColumnsRenderer() {
		TableColumnModel colModel = table.getColumnModel();
		final int lastColumn = colModel.getColumnCount() - 1;
		if (lastColumn >= 0) {
			for (int i = 0; i < lastColumn; i++) {
				TableColumn col = colModel.getColumn(i);
				col.setHeaderRenderer(
						new RotatedLabelTableCellRenderer(
								selMode == SelectionMode.columns));
			}
			TableColumn col = colModel.getColumn(lastColumn);
			col.setHeaderRenderer(new DefaultTableCellRenderer());
		}
	}
	
	public void refresh() {
		table.repaint();
		table.getTableHeader().repaint();
	}
	
	public void refreshColumns() {
		setModel(model);
	}

	public void setModel(ITable model) {
		this.model = model;
		table.setModel(new TableModelAdapter(model));

		refreshTableColumnsRenderer();
		refreshTableColumnsWidth();
		table.setRowHeight(rowsHeight);
		
		table.repaint();
	}

	public void setCellDecorator(ElementDecorator decorator) {
		
		final IElementAdapter cellAdapter = model.getCellAdapter();
		
		table.setDefaultRenderer(
				cellAdapter.getElementClass(), 
				new LabelTableCellRenderer(decorator));
		
		table.repaint();
	}
	
	public int getColumnsWidth() {
		return columnsWidth;
	}
	
	public void setColumnsWidth(int columnsWidth) {
		this.columnsWidth = columnsWidth;
		refreshTableColumnsWidth();
	}
	
	public int getRowsHeight() {
		return rowsHeight;
	}
	
	public void setRowsHeight(int rowsHeight) {
		this.rowsHeight = rowsHeight;
		table.setRowHeight(rowsHeight);
	}
	
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
		
		oldRowSelection = table.getSelectedRows();
	}
	
	private int[] mergeArrays(int[] a1, int[] a2){
		//FIXME: Improve efficency (don't use maps and sets!!)
		
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		
		for (Integer i: a1)
			hm.put(i.toString(), i);
		for (Integer i: a2)
			hm.put(i.toString(), i);
		
	    Set<Map.Entry<String, Integer>> set = hm.entrySet();
	    
	    int[] merged = new int[set.size()];
	    int counter = 0;
		for (Map.Entry<String, Integer> me : set) {
			merged[counter] = me.getValue();
			counter++;
		}
		
		Arrays.sort(merged);
		
		return merged;
	}
	
	public boolean inArray(int[] array, int needle) {
		for (int i = 0; i < array.length; i++)
			if (array[i] == needle) 
				return true;
		return false;
	}
	
	/*private void printArray(int[] selectedRows) {
		System.out.println("-------------");
		for (int idx : selectedRows)
			System.out.println(idx);
	}*/	
}