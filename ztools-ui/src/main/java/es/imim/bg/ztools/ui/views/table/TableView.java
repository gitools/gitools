package es.imim.bg.ztools.ui.views.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.ui.actions.FileActionSet;
import es.imim.bg.ztools.ui.actions.MenuActionSet;
import es.imim.bg.ztools.ui.colormatrix.TablePanel;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.views.AbstractView;

public class TableView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	public enum TableViewLayout {
		LEFT, RIGHT, TOP, BOTTOM
	}
	private ITable table;
	
	private TableViewConfigPanel configPanel;
	
	private JTextPane infoPane;
	private JScrollPane infoScrollPane;
	private TablePanel tablePanel;

	private TableViewLayout layout;
	
	protected boolean blockSelectionUpdate;

	public TableView(final ITable table) {
		
		this.table = table;
	
		this.layout = TableViewLayout.LEFT;
		
		this.blockSelectionUpdate = false;
		
		createComponents();
		
		table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				tableModelPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		});
	}

	protected void tableModelPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
		/*if (ITable.CELL_DECORATION_PROPERTY.equals(propertyName)) {
			cellDecorator.setConfig(
					table.getCellDecoration(
							table.getCurrentProperty()));
			
			refreshColorMatrixWidth();
			colorMatrixPanel.refresh();
		}*/
		/*else if (ITable.SELECTION_MODE_PROPERTY.equals(propertyName)) {
			SelectionMode mode = (SelectionMode) newValue;
			colorMatrixPanel.setSelectionMode(mode);
			colorMatrixPanel.refresh();
			refreshActions();
		}*/
		if (ITable.SELECTION_CHANGED.equals(propertyName)
			|| ITable.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			
			if (!blockSelectionUpdate) {
				blockSelectionUpdate = true;
				if (ITable.VISIBLE_COLUMNS_CHANGED.equals(propertyName))
					tablePanel.refreshColumns();
				
				tablePanel.setSelectedColumns(table.getSelectedColumns());
				tablePanel.setSelectedRows(table.getSelectedRows());
				tablePanel.refresh();
				blockSelectionUpdate = false;
			}
		}
		else if (ITable.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			tablePanel.refresh();
		}
		else if (ITable.CELL_VALUE_CHANGED.equals(propertyName)) {
			tablePanel.refresh();
		}
	}

	private void createComponents() {
		
		ITableDecorator[] availableDecorators = 
			new ITableDecorator[] {
				new ScaleCellDecorator(table.getCellsFacade())
		};
		
		/* Configuration panel */

		configPanel = new TableViewConfigPanel(table, availableDecorators);
		
		final JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(configPanel);
		
		configPanel.refresh();
		
		/* Color matrix */
		
		tablePanel = new TablePanel();
		tablePanel.setModel(table);
		
		tablePanel.setCellDecorator(
				configPanel.getCellDecorator());
		
		ListSelectionListener selListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && !blockSelectionUpdate) {
					blockSelectionUpdate = true;
					
					table.setSelectedRows(
							tablePanel.getSelectedRows());
					table.setSelectedColumns(
							tablePanel.getSelectedColumns());
					
					int colIndex = tablePanel.getSelectedLeadColumn();
					int rowIndex = tablePanel.getSelectedLeadRow();
					
					table.setLeadSelection(rowIndex, colIndex);
					
					blockSelectionUpdate = false;
				}
			}
		};
		
		tablePanel.getTableSelectionModel().addListSelectionListener(selListener);
		tablePanel.getColumnSelectionModel().addListSelectionListener(selListener);

		refreshColorMatrixWidth();
		
		/* Details panel */
		
		infoPane = new JTextPane();
		infoPane.setBackground(Color.WHITE);
		infoPane.setContentType("text/html");
		infoScrollPane = new JScrollPane(infoPane);
		infoScrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 8, 8));		
		
		final JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(northPanel, BorderLayout.NORTH);
		centerPanel.add(tablePanel, BorderLayout.CENTER);
		centerPanel.add(infoScrollPane, BorderLayout.WEST);
		
		configureLayout();
	}
	
	private void configureLayout() {
		int splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
		boolean leftOrTop = true;
		switch(layout) {
		case LEFT:
		case RIGHT:
			splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
			break;
		case TOP:
		case BOTTOM:
			splitOrientation = JSplitPane.VERTICAL_SPLIT;
			break;
		}
		switch(layout) {
		case LEFT:
		case TOP: leftOrTop =true; break;
		case RIGHT:
		case BOTTOM: leftOrTop = false; break;
		}
		
		final JSplitPane splitPane = new JSplitPane(splitOrientation);
		if (leftOrTop) {
			splitPane.add(infoScrollPane);
			splitPane.add(tablePanel);
		}
		else {
			splitPane.add(tablePanel);
			splitPane.add(infoScrollPane);
		}
		splitPane.setDividerLocation(220);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	private void refreshColorMatrixWidth() {
		/*CellDecorationConfig config = 
			table.getCellDecoration(
					table.getCurrentProperty());
		
		colorMatrixPanel.setColumnsWidth(
				config.showColors ? 
						defaultColorColumnsWidth 
						: defaultValueColumnsWidth);*/
		
		tablePanel.setColumnsWidth(
				configPanel.getCellDecorator()
					.getPreferredWidth());
	}
	
	public TablePanel getColorMatrixPanel() {
		return tablePanel;
	}
	
	public ITable getTable() {
		return table;
	}
	
	public void setTable(ITable tableModel) {
		this.table = tableModel;
		refresh();
	}

	@Override
	public Object getModel() {
		return table;
	}

	@Override
	public void refresh() {
		tablePanel.refresh();
	}
	
	@Override
	public void refreshActions() {
		MenuActionSet.editActionSet.setTreeEnabled(true);
		MenuActionSet.tableActionSet.setTreeEnabled(true);
		MenuActionSet.mtcActionSet.setTreeEnabled(true);
		
		FileActionSet.closeAction.setEnabled(true);
		FileActionSet.exportActionSet.setTreeEnabled(true);
		
		MenuActionSet.mtcActionSet.setTreeEnabled(true);
	}
}
