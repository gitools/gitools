package es.imim.bg.ztools.ui.views.table;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import es.imim.bg.GenericFormatter;
import es.imim.bg.colorscale.PValueColorScale;
import es.imim.bg.colorscale.ZScoreColorScale;
import es.imim.bg.ztools.model.IModel;
import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.test.results.BinomialResult;
import es.imim.bg.ztools.test.results.CombinationResult;
import es.imim.bg.ztools.test.results.CommonResult;
import es.imim.bg.ztools.test.results.FisherResult;
import es.imim.bg.ztools.test.results.ZScoreResult;
import es.imim.bg.ztools.ui.actions.FileActionSet;
import es.imim.bg.ztools.ui.actions.MenuActionSet;
import es.imim.bg.ztools.ui.model.TableViewModel;
import es.imim.bg.ztools.ui.panels.TemplatePane;
import es.imim.bg.ztools.ui.panels.table.TablePanel;
import es.imim.bg.ztools.ui.views.AbstractView;

public class TableView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	private static final int defaultDividerLocation = 280;

	private static final String defaultTemplateName = "/vm/details/noselection.vm";

	public enum TableViewLayout {
		LEFT, RIGHT, TOP, BOTTOM
	}
	
	private TableViewModel model;
	
	private TableViewConfigPanel configPanel;
	
	private TemplatePane templatePane;
	private TablePanel tablePanel;
	private JPanel mainPanel;

	private TableViewLayout layout;
	
	protected boolean blockSelectionUpdate;

	private PropertyChangeListener modelListener;

	public TableView(final ITable table) {
		
		this.model = new TableViewModel(table);
	
		this.layout = TableViewLayout.LEFT;
		
		this.blockSelectionUpdate = false;
		
		createComponents();
		
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				modelPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		});
		
		model.getDecorator().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				modelPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		});
		
		table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				tablePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		});
	}

	protected void modelPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
		if (TableViewModel.DECORATOR.equals(propertyName))
			tablePanel.setCellDecorator(model.getDecorator());
		
		tablePanel.refresh();
	}
	
	protected void tablePropertyChange(
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
				
				tablePanel.setSelectedColumns(getTable().getSelectedColumns());
				tablePanel.setSelectedRows(getTable().getSelectedRows());
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
				((IModel) oldValue).removePropertyChangeListener(modelListener);
			
			((IModel) newValue).addPropertyChangeListener(modelListener);
		}
	}

	private void refreshCellDetails() {
		
		int row = getTable().getSelectionLeadRow();
		int rowCount = getTable().getRowCount();
		int column = getTable().getSelectionLeadColumn();
		int columnCount = getTable().getColumnCount();
		
		VelocityContext context = new VelocityContext();
		String templateName = defaultTemplateName;
		
		if (column >= 0 && column < columnCount && row >= 0 && row < rowCount) {
			final IElementAdapter columnAdapter = getTable().getColumnAdapter();
			final Object columnElement = getTable().getColumn(column);
			
			final IElementAdapter rowAdapter = getTable().getRowAdapter();
			final Object rowElement = getTable().getRow(row);
			
			final IElementAdapter cellAdapter = getTable().getCellAdapter();
			final Object cellElement = getTable().getCell(row, column);
			
			templateName = getTemplateNameFromObject(cellElement);

			if (templateName != null) {				
				context.put("fmt", new GenericFormatter());
				
				context.put("zscoreScale", new ZScoreColorScale()); //FIXME
				context.put("pvalueScale", new PValueColorScale()); //FIXME
				
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
			}
		}
		/*else if (column < 0) {
			System.out.println("row:" + row);
		}
		else if (row < 0) {
			System.out.println("col:" + column);
		}*/
		
		try {
			templatePane.setTemplate(templateName);
			templatePane.setContext(context);
			templatePane.render();
		}
		catch (Exception e) {
			e.printStackTrace(); //FIXME
		}
	}
	
	private String getTemplateNameFromObject(Object object) {
		String templateName = "default.vm";
		if (object instanceof BinomialResult)
			templateName = "binomial.vm";
		else if (object instanceof FisherResult)
			templateName = "fisher.vm";
		else if (object instanceof ZScoreResult)
			templateName = "zscore.vm";
		else if (object instanceof CombinationResult)
			templateName = "combination.vm";
		else if (object instanceof CommonResult)
			templateName = "common.vm";
		
		return "/vm/details/" + templateName;
	}

	private void createComponents() {
		
		/*ITableDecorator[] availableDecorators = 
			new ITableDecorator[] {
				new ScaleCellDecorator(getTable())*//*,
				new TextCellDecorator(getTable())*/
		//};
		
		/* Configuration panel */

		configPanel = new TableViewConfigPanel(model);
		
		/*final JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(configPanel);*/
		
		configPanel.refresh();
		
		/* Color matrix */
		
		tablePanel = new TablePanel();
		tablePanel.setModel(getTable());
		
		tablePanel.setCellDecorator(model.getDecorator());
		
		ListSelectionListener selListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && !blockSelectionUpdate) {
					blockSelectionUpdate = true;
					
					getTable().setSelectedRows(
							tablePanel.getSelectedRows());
					getTable().setSelectedColumns(
							tablePanel.getSelectedColumns());
					
					int colIndex = tablePanel.getSelectedLeadColumn();
					int rowIndex = tablePanel.getSelectedLeadRow();
					
					getTable().setLeadSelection(rowIndex, colIndex);
					
					blockSelectionUpdate = false;
				}
			}
		};
		
		tablePanel.getTableSelectionModel().addListSelectionListener(selListener);
		tablePanel.getColumnSelectionModel().addListSelectionListener(selListener);

		refreshColorMatrixWidth();
		
		/* Details panel */
		
		Properties props = new Properties();
		props.put(VelocityEngine.VM_LIBRARY, "/vm/details/common.vm");
		templatePane = new TemplatePane(props);
		try {
			templatePane.setTemplate(defaultTemplateName);
			templatePane.render();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*infoPane = new JTextPane();
		infoPane.setBackground(Color.WHITE);
		infoPane.setContentType("text/html");
		//infoPane.setAutoscrolls(false);
		infoScrollPane = new JScrollPane(infoPane);
		infoScrollPane.setBorder(
				BorderFactory.createEmptyBorder(8, 8, 8, 8));*/		
		
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
			//splitPane.add(infoScrollPane);
			splitPane.add(templatePane);
			splitPane.add(mainPanel);
		}
		else {
			splitPane.add(mainPanel);
			//splitPane.add(infoScrollPane);
			splitPane.add(templatePane);
		}
		splitPane.setDividerLocation(defaultDividerLocation);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}

	private void refreshColorMatrixWidth() {
		/*CellDecorationConfig config = 
			getTable().getCellDecoration(
					getTable().getCurrentProperty());
		
		colorMatrixPanel.setColumnsWidth(
				config.showColors ? 
						defaultColorColumnsWidth 
						: defaultValueColumnsWidth);*/
		
		/*tablePanel.setColumnsWidth(
				configPanel.getCellDecorator()
					.getPreferredWidth());*/
	}

	protected ITable getTable() {
		return model.getTable();
	}
	
	@Deprecated //When getModel return TableViewModel
	public void setTable(ITable tableModel) {
		this.model.setTable(tableModel);
		refresh();
	}

	@Override
	public Object getModel() {
		return model.getTable(); //TODO: return TableViewModel
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
