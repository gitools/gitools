package org.gitools.ui.views.table;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.velocity.VelocityContext;
import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.FileActionSet;
import org.gitools.ui.actions.MenuActionSet;
import org.gitools.ui.actions.TableActionSet;
import org.gitools.ui.panels.TemplatePane;
import org.gitools.ui.panels.table.TablePanel;
import org.gitools.ui.views.AbstractView;

import edu.upf.bg.GenericFormatter;
import edu.upf.bg.colorscale.PValueColorScale;
import edu.upf.bg.colorscale.ZScoreColorScale;
import org.gitools.model.IModel;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.table.IMatrixView;
import org.gitools.model.table.decorator.ElementDecorator;
import org.gitools.model.table.element.IElementAdapter;
import org.gitools.model.table.element.IElementProperty;
import org.gitools.stats.test.results.BinomialResult;
import org.gitools.stats.test.results.CombinationResult;
import org.gitools.stats.test.results.CommonResult;
import org.gitools.stats.test.results.FisherResult;
import org.gitools.stats.test.results.ZScoreResult;

public class TableView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	private static final String defaultTemplateName = "/vm/details/noselection.vm";
	
	private MatrixFigure model;
	
	private TableViewConfigPanel configPanel;
	
	private TablePanel tablePanel;
	
	protected boolean blockSelectionUpdate;

	private PropertyChangeListener modelListener;
	private PropertyChangeListener decoratorListener;

	public TableView(MatrixFigure model) {
		
		this.model = model;
		
		final IMatrixView matrixView = model.getTable();
	
		this.blockSelectionUpdate = false;
		
		createComponents();
		
		modelListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				modelPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		};
		
		decoratorListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				decoratorPropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		};
		
		model.addPropertyChangeListener(modelListener);
		
		model.getDecorator().addPropertyChangeListener(decoratorListener);
		
		matrixView.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				tablePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			}
		});
	}

	protected void modelPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
		if (MatrixFigure.DECORATOR.equals(propertyName)) {
			final ElementDecorator prevDecorator = (ElementDecorator) oldValue;
			prevDecorator.removePropertyChangeListener(decoratorListener);
			final ElementDecorator nextDecorator = (ElementDecorator) newValue;
			nextDecorator.addPropertyChangeListener(decoratorListener);
			tablePanel.setCellDecorator(model.getDecorator());
		}
		
		tablePanel.refresh();
	}
	
	protected void decoratorPropertyChange(
			String propertyName, Object oldValue, Object newValue) {
		
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
		if (IMatrixView.SELECTION_CHANGED.equals(propertyName)
			|| IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			
			if (!blockSelectionUpdate) {
				blockSelectionUpdate = true;
				if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName))
					tablePanel.refreshColumns();
				
				//System.out.println("Start selection change:");
				tablePanel.setSelectedCells(
						getTable().getSelectedColumns(),
						getTable().getSelectedRows());
				tablePanel.refresh();
				//System.out.println("End selection change.");
				
				blockSelectionUpdate = false;
			}
		}
		else if (IMatrixView.SELECTED_LEAD_CHANGED.equals(propertyName)) {
			refreshCellDetails();
		}
		else if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(propertyName)) {
			tablePanel.refresh();
		}
		else if (IMatrixView.VISIBLE_ROWS_CHANGED.equalsIgnoreCase(propertyName)) {
			tablePanel.refresh();
		}
		else if (IMatrixView.CELL_VALUE_CHANGED.equals(propertyName)) {
			tablePanel.refresh();
		}
		else if (IMatrixView.CELL_DECORATION_CONTEXT_CHANGED.equals(propertyName)) {
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
		
		//System.out.println("refreshCellDetails(" + row + ", " + column + ")");
		
		try {
			final TemplatePane templatePane = AppFrame.instance().getDetailsPane();
			
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
				ListSelectionModel src = (ListSelectionModel) e.getSource();
				src.hashCode();
				
				if (!e.getValueIsAdjusting() && !blockSelectionUpdate) {
					blockSelectionUpdate = true;
					
					//System.out.println("Selection listener.");
					
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
		
		/*Properties props = new Properties();
		props.put(VelocityEngine.VM_LIBRARY, "/vm/details/common.vm");
		templatePane = new TemplatePane(props);
		try {
			templatePane.setTemplate(defaultTemplateName);
			templatePane.render();
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
		
		setLayout(new BorderLayout());
		add(configPanel, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
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

	protected IMatrixView getTable() {
		return model.getTable();
	}
	
	@Deprecated //When getModel return TableViewModel
	public void setTable(IMatrixView tableModel) {
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
		TableActionSet.fastSortRowsAction.setEnabled(true);
		
		FileActionSet.closeAction.setEnabled(true);
		FileActionSet.exportActionSet.setTreeEnabled(true);
		
		MenuActionSet.mtcActionSet.setTreeEnabled(true);
	}
}
