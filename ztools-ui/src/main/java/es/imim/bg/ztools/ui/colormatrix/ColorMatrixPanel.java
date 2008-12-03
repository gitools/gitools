package es.imim.bg.ztools.ui.colormatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import es.imim.bg.ztools.ui.actions.edit.ChangeSelectionModeAction;
import es.imim.bg.ztools.ui.model.SelectionMode;

public class ColorMatrixPanel extends JPanel {

	private static final long serialVersionUID = 1122420366217373359L;

	public interface ColorMatrixListener {
		void selectionChanged();
	}
	
	private class ColorMatrixModelAddapter implements TableModel {

		private ColorMatrixModel model;
		
		public ColorMatrixModelAddapter(ColorMatrixModel model) {
			this.model = model;
		}
		
		public int getRowCount() {
			return model.getRowCount();
		}
		
		public int getColumnCount() {
			return model.getColumnCount() + 1;
		}

		public String getColumnName(int col) {
			return col < model.getColumnCount() ? 
					model.getColumnName(col) : " ";
		}

		public Object getValueAt(int row, int col) {
			return col < model.getColumnCount() ?
					model.getValue(row, col) :
					model.getRowName(row);
		}
		
		public void setValueAt(Object value, int row, int col) {
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public void addTableModelListener(TableModelListener arg0) {
		}
		
		public void removeTableModelListener(TableModelListener arg0) {
		}
		
		public Class<?> getColumnClass(int col) {
			return col < model.getColumnCount() ? 
					Double.class : String.class;
		}
	}
	
	public class TableCellRendererAdapter implements TableCellRenderer {
		
		private ColorMatrixCellDecorator decorator;
		
		private DefaultTableCellRenderer tableRenderer = 
			new DefaultTableCellRenderer();
		
		public TableCellRendererAdapter(ColorMatrixCellDecorator decorator) {
			this.decorator = decorator;
		}
		
		public Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {
			
			JLabel label = (JLabel) tableRenderer
					.getTableCellRendererComponent(
							table, value, isSelected, hasFocus, row, column);
			
			configureRenderer(tableRenderer, value);
			
			if (isSelected)
				label.setBackground(label.getBackground().darker());
			
			return label;
		}

		private void configureRenderer(
				JLabel label,
				Object value) {

			CellDecoration decoration = new CellDecoration();
			decorator.decorate(decoration, (Double) value);
			label.setText(decoration.getText());
			label.setToolTipText(decoration.getToolTip());
			label.setForeground(decoration.getFgColor());
			label.setBackground(decoration.getBgColor());

			switch (decoration.textAlign) {
			case left: label.setHorizontalAlignment(SwingConstants.LEFT); break;
			case right: label.setHorizontalAlignment(SwingConstants.RIGHT); break;
			case center: label.setHorizontalAlignment(SwingConstants.CENTER); break;
			}
			
		}
	}

	public class RotatedTableCellRenderer
			extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 8878769979396041532L;

		protected static final double radianAngle = (-90.0 / 180.0) * Math.PI;
		
		protected boolean highlightSelected;
		
		protected boolean isSelected;
		
		public RotatedTableCellRenderer(boolean highlightSelected) {
			this.highlightSelected = highlightSelected;
		}

		public Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {
			
			this.setText(value.toString());
			
			int[] selColumns = table.getSelectedColumns();
			Arrays.sort(selColumns);
			int i = Arrays.binarySearch(selColumns, column);
			
			//System.out.println(column + " : " + i + " : cols:" + Arrays.toString(selColumns));
			
			this.isSelected = i >= 0;
			
			return this;
		}

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			final int w = this.getWidth();
			final int h = this.getHeight();
			
			g2.setClip(0, 0, w, h);
			g2.setFont(this.getFont());
			
			if (highlightSelected && isSelected)
				g2.setBackground(Color.ORANGE);
			
			g2.clearRect(0, 0, w, h);
			
			g2.setColor(Color.GRAY);
			g2.drawRect(0, 0, w, h);
			
			AffineTransform at = new AffineTransform();
			at.setToTranslation(this.getWidth(), this.getHeight());
			g2.transform(at);
			at.setToRotation(radianAngle);
			g2.transform(at);
			
			Rectangle2D r = g2.getFontMetrics().getStringBounds(this.getText(), g2);
			float textHeight = (float) r.getHeight();

			g2.setColor(Color.BLACK);
			g2.drawString(this.getText(), 4.0f, -(w + 8 - textHeight) / 2);
		}
	}
	
	
	private class HeaderMouseListener extends MouseInputAdapter{
		
		protected int colfrom;
		protected int colto;

			@Override
			public void mousePressed(MouseEvent e) {
				
				
				
				int lastcol = table.getColumnCount()-1;
				if (lastcol == table.columnAtPoint(e.getPoint())){
					
					setSelectionMode(SelectionMode.cells);
					selectAll();
					
				}else{
				
										
					setSelectionMode(SelectionMode.columns);
		
					int col = colfrom = table.columnAtPoint(e.getPoint());
					int[] selectedColumns;
					
					if (getCtrlDown()){
						int nb = getSelectedColumns().length + 1;
						selectedColumns = new int[nb];
						System.arraycopy(getSelectedColumns(), 0, selectedColumns, 0, nb-1);
						selectedColumns[nb-1] = col;
					}else{
						selectedColumns = new int[1];
						selectedColumns[0] = col;
					}
					
					setSelectedColumns(selectedColumns);
					
/*					int rows = table.getRowCount();
					int[] selectedRows = new int[rows];
					int start = 0;
					int stop = rows-1;
					int counter = 0;
					for (int i = start; i <= stop ; i++){
						selectedRows[counter] = i;
						counter++;
					}
					setSelectedRows(selectedRows);*/
				}
			}
			
			
			@Override
			public void mouseDragged(MouseEvent e) {
				colto = table.columnAtPoint(e.getPoint());
							
				int cols = Math.abs(colto-colfrom) + 1;
				int[] selectedColumns;
				int[] newSelectedColumns = new int[cols];
				
				int start = (colto < colfrom) ? colto : colfrom;
				int stop = (colto > colfrom) ? colto : colfrom;
				int counter = 0;
				for (int i = start; i <= stop ; i++){
					newSelectedColumns[counter] = i;
					counter++;
				}
					
			
				if (getCtrlDown()){		
					selectedColumns = mergeArrays(getSelectedColumns(), newSelectedColumns);					
				}else{
					selectedColumns = newSelectedColumns;
				}
				

				
				setSelectedColumns(selectedColumns);
				
			/*	System.out.print("cols: ");
				for (int i: selectedColumns){
					System.out.print(i + ", ");
				}
				System.out.print("\n");*/
	
			}
		
	}
	
	private class CellMouseListener extends MouseInputAdapter{
			
		protected int rowfrom;
		protected int rowto;
		protected int colfrom;
		protected int colto;
		
		@Override
		public void mousePressed(MouseEvent e) {

			int lastcol = table.getColumnCount() - 1;
			if (lastcol == table.columnAtPoint(e.getPoint())) {
				setSelectionMode(SelectionMode.rows);
			} else {
					setLead(e);
			}
			rowfrom = table.rowAtPoint(e.getPoint());
			colfrom = table.columnAtPoint(e.getPoint());
		}
		
		private void setLead(MouseEvent e){
			setSelectionMode(SelectionMode.cells);
			
			int[] selectedRow = new int[1];
			selectedRow[0] = table.rowAtPoint(e.getPoint());
			setSelectedRows(selectedRow);
			
			int[] selectedCol = new int[1];
			selectedCol[0] = table.columnAtPoint(e.getPoint());
			setSelectedColumns(selectedCol);
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e){				
	
			int lastcol = table.getColumnCount() - 1;
			if (lastcol == table.columnAtPoint(e.getPoint())) {
				
				rowto = table.rowAtPoint(e.getPoint());
				colto = table.columnAtPoint(e.getPoint());

				int rows = Math.abs(rowto-rowfrom) + 1;
				int[] selectedRows = new int[rows];
				int cols = Math.abs(colto-colfrom) + 1;
				int[] selectedColumns = new int[cols];
		
				int start = (rowto < rowfrom) ? rowto : rowfrom;
				int stop = (rowto > rowfrom) ? rowto : rowfrom;
				int counter = 0;
				for (int i = start; i <= stop ; i++){
					selectedRows[counter] = i;
					counter++;
				}
				
				start = (colto < colfrom) ? colto : colfrom;
				stop = (colto > colfrom) ? colto : colfrom;
				counter = 0;
				for (int i = start; i <= stop ; i++){
					selectedColumns[counter] = i;
					counter++;
				}
				
				setSelectedColumns(selectedColumns);
				setSelectedRows(selectedRows);
			}else{
				setLead(e);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e){	
			int lastcol = table.getColumnCount() - 1;
			if (lastcol != table.columnAtPoint(e.getPoint())) {
				setLead(e);
			}
		}
	}
	
	
	private JTable table;
	
	private SelectionMode selMode;
	
	private int columnsHeight;
	private int columnsWidth;
	private int rowsHeight;
	
	private int selectedLeadColumn;
	private int selectedLeadRow;
	
	private ColorMatrixModel model;
	
	private List<ColorMatrixListener> listeners;
	

	
	public ColorMatrixPanel() {
	
		this.selMode = SelectionMode.cells;
		
		this.columnsHeight = 160;
		this.columnsWidth = 30;
		this.rowsHeight = 30;
	
		this.selectedLeadColumn = this.selectedLeadRow = -1;
	
		this.listeners = new ArrayList<ColorMatrixListener>(1);
		
		createComponents();
	}
	
	private boolean ctrlDown;
	
	public boolean getCtrlDown(){
		return ctrlDown;
	}
	
	private void setCtrlDown(boolean b){
		//System.out.println("Set control to " + b);
		ctrlDown = b;
	}
	
	private int[] mergeArrays(int[] a1, int[] a2){
		//merges two int-arrays and doesn't allow duplicate values	
		
		Arrays.sort(a1);
		Arrays.sort(a2);
		int[] merged = new int[a1.length + a2.length];
		//System.out.println(merged.length);
		int a1_c = 0;
		int a2_c = 0;
		int merged_c = 0;
		int value = -1;
		
		/*for (int i : a1){
			System.out.println("a1 " + i);
		}
		
		for (int i : a2){
			System.out.println("a2 " + i);
		}*/
		
		while (merged_c < merged.length-1){
			int oldvalue = value;
						
			
			if(a1_c < a1.length && a1[a1_c] < a2[a2_c]){
				value =	a1[a1_c];
				a1_c++;
			}else if(a2_c < a2.length && a1[a1_c] > a2[a2_c]){
				value =	a1[a2_c];
				a2_c++;
			}else if(a1_c < a1.length && a2_c < a2.length && a1[a1_c] == a2[a2_c]){
				value =	a1[a1_c];
				if(a1_c < a1.length-1) a1_c++;
				if(a2_c < a2.length-1) a2_c++;
			}else{
				value = (a1[a1_c] < a2[a2_c]) ? a2[a2_c] : a1[a1_c];
			}
			if (value > oldvalue){
				//System.out.println("added" + value);
				merged[merged_c] = value;
			}
			merged_c++;
			//System.out.println("a1_c:" + a1_c + ", a2_c:" + a2_c + ", merged_c:" + merged_c);
		}
		return merged;
	}
	
	
	private void createComponents() {
		
		table = new JTable();
		
		table.setFillsViewportHeight(true);
		
		
		HeaderMouseListener hml = new HeaderMouseListener();
		table.getTableHeader().setPreferredSize(new Dimension(columnsWidth, columnsHeight));
		table.getTableHeader().addMouseMotionListener(hml);
		table.getTableHeader().addMouseListener(hml);
		
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
					
					for (ColorMatrixListener listener : listeners)
						listener.selectionChanged();
	
					refresh();
				}
			}
		};
		
		table.getSelectionModel().addListSelectionListener(listener);
		table.getColumnModel().getSelectionModel().addListSelectionListener(listener);
		
		CellMouseListener cml = new CellMouseListener();
		table.addMouseListener(cml);
		
		refreshSelectionMode();
			
		table.addKeyListener(new KeyListener(){
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown()){
					setCtrlDown(true);
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!e.isControlDown()){
					setCtrlDown(false);
				}				
			}
			
			
		});
		
		final JScrollPane scroll = new JScrollPane(table);
		
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}
	
	private void refreshSelectionMode() {
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
		for (int i = 0; i < lastColumn; i++) {
			TableColumn col = colModel.getColumn(i);
			col.setPreferredWidth(columnsWidth);
		}
		TableColumn col = colModel.getColumn(lastColumn);
		col.setResizable(true);
		col.setMinWidth(400);
	}
	
	private void refreshTableColumnsRenderer() {
		TableColumnModel colModel = table.getColumnModel();
		final int lastColumn = colModel.getColumnCount();
		for (int i = 0; i < lastColumn; i++) {
			TableColumn col = colModel.getColumn(i);
			col.setHeaderRenderer(
					new RotatedTableCellRenderer(
							selMode == SelectionMode.columns));
		}
	}
	
	public void refresh() {
		table.repaint();
		table.getTableHeader().repaint();
	}
	
	public void refreshColumns() {
		setModel(model);
	}

	public void setModel(ColorMatrixModel model) {
		this.model = model;
		table.setModel(new ColorMatrixModelAddapter(model));

		refreshTableColumnsRenderer();
		refreshTableColumnsWidth();
		table.setRowHeight(rowsHeight);
		
		table.repaint();
	}

	public void setCellDecorator(ColorMatrixCellDecorator decorator) {
		table.setDefaultRenderer(Double.class, new TableCellRendererAdapter(decorator));
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
	
	public void addListener(ColorMatrixListener listener) {
		listeners.add(listener);
	}

	public int[] getSelectedColumns() {
		return table.getSelectedColumns();
	}
	
	public void setSelectedColumns(int[] selectedColumns) {
		updateSelection(
				table.getColumnModel().getSelectionModel(), 
				getSelectedRows(), 
				selectedColumns);
	}
	
	public int[] getSelectedRows() {
		return table.getSelectedRows();
	}

	public void setSelectedRows(int[] selectedRows) {
		updateSelection(
				table.getSelectionModel(), 
				getSelectedRows(), 
				selectedRows);
	}
	
	private void updateSelection(
			ListSelectionModel selModel, int[] oldList, int[] newList) {
		
		selModel.setValueIsAdjusting(true);
		selModel.clearSelection();
		for (int idx : newList)
			selModel.addSelectionInterval(idx, idx);
		selModel.setValueIsAdjusting(false);
		return;
		
		/*Arrays.sort(oldList);
		Arrays.sort(newList);
		
		int i = 0;
		int j = 0;
		
		while (i < oldList.length && j < newList.length) {
			int ival = oldList[i];
			int jval = newList[j];
			if (ival == jval) {
				i++;
				j++;
			}
			else if (ival < jval) {
				selModel.removeSelectionInterval(ival, ival);
				i++;
			}
			else {
				selModel.addSelectionInterval(jval, jval);
				j++;
			}
		}
		
		while (i < oldList.length) {
			int ival = oldList[i++];
			selModel.removeSelectionInterval(ival, ival);
		}
		
		while (j < newList.length) {
			int jval = newList[j++];
			selModel.addSelectionInterval(jval, jval);
		}*/
	}

	public void clearSelection() {
		table.clearSelection();
	}

	public void selectAll() {
		int lastRowIndex = table.getRowCount() - 1;
		table.getSelectionModel()
			.addSelectionInterval(0, lastRowIndex);
		
		int lastColIndex = table.getColumnCount() - 2;
		table.getColumnModel()
			.getSelectionModel()
			.addSelectionInterval(0, lastColIndex);
	}
}
