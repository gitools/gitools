package es.imim.bg.ztools.ui.views.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.imim.bg.ztools.model.elements.ElementFacade;
import es.imim.bg.ztools.model.elements.ElementProperty;
import es.imim.bg.ztools.ui.actions.FileActionSet;
import es.imim.bg.ztools.ui.actions.MenuActionSet;
import es.imim.bg.ztools.ui.model.IModel;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.panels.celldeco.ITableDecorator;
import es.imim.bg.ztools.ui.panels.celldeco.ScaleCellDecorator;
import es.imim.bg.ztools.ui.panels.celldeco.TextCellDecorator;
import es.imim.bg.ztools.ui.panels.table.TablePanel;
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
	private JPanel mainPanel;

	private TableViewLayout layout;
	
	protected boolean blockSelectionUpdate;

	private PropertyChangeListener decorationContextListener;

	public TableView(final ITable table) {
		
		this.table = table;
	
		this.layout = TableViewLayout.LEFT;
		
		this.blockSelectionUpdate = false;
		
		createComponents();

		decorationContextListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				tablePanel.refresh();
			}
		};
		
		table.getCellDecoratorContext().addPropertyChangeListener(decorationContextListener);
		
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
		else if (ITable.SELECTED_LEAD_CHANGED.equals(propertyName)) {
			refreshCellDetails();
		}
		else if (ITable.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			tablePanel.refresh();
		}
		else if (ITable.CELL_VALUE_CHANGED.equals(propertyName)) {
			tablePanel.refresh();
		}
		else if (ITable.CELL_DECORATION_CONTEXT_CHANGED.equals(propertyName)) {
			if (oldValue != null)
				((IModel) oldValue).removePropertyChangeListener(decorationContextListener);
			
			((IModel) newValue).addPropertyChangeListener(decorationContextListener);
		}
	}

	private void refreshCellDetails() {
		String html = "";
		
		int row = table.getSelectionLeadRow();
		int column = table.getSelectionLeadColumn();
		int columnCount = table.getColumnCount();
		
		if (column >= 0 && column < columnCount && row >= 0) {
			final String colName = table.getColumn(column).toString();
			final String rowName = table.getRow(row).toString();
			ElementFacade cellsFacade = table.getCellsFacade();
			Object element = table.getCell(row, column);
			
			StringBuilder sb = new StringBuilder();
			
			// Render parameters & values
			sb.append("<p><b>Column</b><br>");
			sb.append(colName).append("</p>");
			sb.append("<p><b>Row</b><br>");
			sb.append(rowName).append("</p>");
			
			for (int i = 0; i < cellsFacade.getPropertyCount(); i++) {
				ElementProperty prop = cellsFacade.getProperty(i);
				
				final String paramName = prop.getName();
				
				final String value = 
					cellsFacade.getValue(element, i).toString(); 
				
				sb.append("<p><b>");
				sb.append(paramName);
				sb.append("</b><br>");
				sb.append(value);
				sb.append("</p>");
			}
			
			html = sb.toString();
		}
		
		infoPane.setText(html);
	}
	
	private void createComponents() {
		
		ITableDecorator[] availableDecorators = 
			new ITableDecorator[] {
				new ScaleCellDecorator(table),
				new TextCellDecorator(table)
		};
		
		/* Configuration panel */

		configPanel = new TableViewConfigPanel(table, availableDecorators);
		
		/*final JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(configPanel);*/
		
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
		//infoPane.setAutoscrolls(false);
		infoScrollPane = new JScrollPane(infoPane);
		infoScrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 8, 8));		
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(configPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		
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
			splitPane.add(mainPanel);
		}
		else {
			splitPane.add(mainPanel);
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
