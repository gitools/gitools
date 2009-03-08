package es.imim.bg.ztools.ui.views.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.test.results.BinomialResult;
import es.imim.bg.ztools.test.results.CommonResult;
import es.imim.bg.ztools.test.results.FisherResult;
import es.imim.bg.ztools.test.results.ZScoreResult;
import es.imim.bg.ztools.ui.actions.FileActionSet;
import es.imim.bg.ztools.ui.actions.MenuActionSet;
import es.imim.bg.ztools.ui.model.IModel;
import es.imim.bg.ztools.ui.model.celldeco.ITableDecorator;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.panels.celldeco.ScaleCellDecorator;
import es.imim.bg.ztools.ui.panels.table.TablePanel;
import es.imim.bg.ztools.ui.views.AbstractView;

public class TableView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	private static final int defaultDividerLocation = 280;

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

	protected VelocityEngine velocityEngine;
	protected Class<?> templateClass;
	protected Template velocityTemplate;
	
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
		
		velocityEngine = new VelocityEngine();
		
		velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
		velocityEngine.setProperty(
				"class." + VelocityEngine.RESOURCE_LOADER + ".class", 
				ClasspathResourceLoader.class.getName());
		
		velocityEngine.setProperty(VelocityEngine.COUNTER_NAME, "forIndex");
		velocityEngine.setProperty(VelocityEngine.COUNTER_INITIAL_VALUE, "0");

		try {
			velocityEngine.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
		else if (ITable.VISIBLE_ROWS_CHANGED.equalsIgnoreCase(propertyName)) {
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
		
		int row = table.getSelectionLeadRow();
		int rowCount = table.getRowCount();
		int column = table.getSelectionLeadColumn();
		int columnCount = table.getColumnCount();
		
		StringWriter sw = new StringWriter();
		
		if (column >= 0 && column < columnCount && row >= 0 && row < rowCount) {
			final IElementAdapter columnAdapter = table.getColumnAdapter();
			final Object columnElement = table.getColumn(column);
			
			final IElementAdapter rowAdapter = table.getRowAdapter();
			final Object rowElement = table.getRow(row);
			
			final IElementAdapter cellAdapter = table.getCellAdapter();
			final Object cellElement = table.getCell(row, column);
			
			if (!cellElement.getClass().equals(templateClass)) {
				templateClass = null;
				try {
					velocityTemplate = getTemplateFromObject(cellElement);
					templateClass = cellElement.getClass();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (velocityTemplate != null) {
				VelocityContext context = new VelocityContext();
				
				context.put("columnAdapter", columnAdapter);
				context.put("columnElement", columnElement);
				
				context.put("rowAdapter", rowAdapter);
				context.put("rowElement", rowElement);
				
				context.put("cellAdapter", cellAdapter);
				context.put("cellElement", cellElement);
				
				final List<IElementProperty> properties = 
					cellAdapter.getProperties();
				
				final Map<String, Object> cellMap = 
					new HashMap<String, Object>();
				
				for (int index = 0; index < properties.size(); index++) {
					final IElementProperty prop = properties.get(index);
					cellMap.put(prop.getId(), 
							cellAdapter.getValue(cellElement, index));
				}
				
				context.put("cell", cellMap);
				
				try {
					velocityTemplate.merge(context, sw);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else if (rowCount == 0)
			sw.append("<p>No cells displayed</p>");
		
		infoPane.setText(sw.toString());

	}
	
	private Template getTemplateFromObject(Object object) 
			throws ResourceNotFoundException, ParseErrorException, Exception {
		
		String templateName = "default.vm";
		if (object instanceof BinomialResult)
			templateName = "binomial.vm";
		else if (object instanceof FisherResult)
			templateName = "fisher.vm";
		else if (object instanceof ZScoreResult)
			templateName = "zscore.vm";
		else if (object instanceof CommonResult)
			templateName = "common.vm";
		
		return velocityEngine.getTemplate("/vm/details/" + templateName);
	}

	private void createComponents() {
		
		ITableDecorator[] availableDecorators = 
			new ITableDecorator[] {
				new ScaleCellDecorator(table)/*,
				new TextCellDecorator(table)*/
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
		splitPane.setDividerLocation(defaultDividerLocation);
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
