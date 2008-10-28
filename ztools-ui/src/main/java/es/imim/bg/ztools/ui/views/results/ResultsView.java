package es.imim.bg.ztools.ui.views.results;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
import es.imim.bg.ztools.ui.model.ResultsModel;
import es.imim.bg.ztools.ui.model.SelectionMode;
import es.imim.bg.ztools.ui.views.AbstractView;
import es.imim.bg.ztools.ui.views.results.ResultsConfigPanel.ResultsConfigPanelListener;

public class ResultsView extends AbstractView {

	private static final long serialVersionUID = -540561086703759209L;

	private static final String defaultParamName = "right-p-value";

	private static final int defaultColorColumnsWidth = 30;
	private static final int defaultValueColumnsWidth = 90;

	//private Results results;
	private ResultsModel resultsModel;
	
	private ResultsConfigPanel configPanel;
	
	private ColorMatrixPanel colorMatrixPanel;
	private DefaultColorMatrixCellDecorator cellDecorator;

	public ResultsView(ResultsModel resultsModel) {
		
		this.resultsModel = resultsModel;
			
		createComponents();
		
		resultsModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(ResultsModel.SELECTION_MODE_PROPERTY)) {
					SelectionMode mode = (SelectionMode) evt.getNewValue();
					colorMatrixPanel.setSelectionMode(mode);
					colorMatrixPanel.refresh();
				}
			}
		});
	}

	private void createComponents() {
		
		/* North panel */

		configPanel = new ResultsConfigPanel(
				resultsModel.getParamNames(),
				resultsModel.getParamIndex(defaultParamName));
		
		configPanel.addListener(new ResultsConfigPanelListener() {
			@Override
			public void paramChanged() {
				cellDecorator.setConfig(
						configPanel.getCurrentDecorationConfig());
				
				refreshColorMatrixWidth();
				colorMatrixPanel.refresh();
			}

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
				return resultsModel.getColumnCount();
			}
			@Override
			public String getColumnName(int column) {
				return resultsModel.getColumnName(column);
			}
			@Override
			public int getRowCount() {
				return resultsModel.getRowCount();
			}
			@Override
			public String getRowName(int row) {
				return resultsModel.getRowName(row);
			}
			@Override
			public Double getValue(int row, int column) {
				return resultsModel.getValue(column, row, 
						configPanel.getSelectedParamIndex());
			}
		});
		
		cellDecorator = new DefaultColorMatrixCellDecorator();
		
		colorMatrixPanel.setCellDecorator(cellDecorator);
		
		colorMatrixPanel.getTableSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				resultsModel.setSelectedRows(
						colorMatrixPanel.getSelectedRows());
			}
		});
		colorMatrixPanel.getColumnSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				resultsModel.setSelectedColumns(
						colorMatrixPanel.getSelectedColumns());
			}
		});
		
		colorMatrixPanel.addListener(new ColorMatrixListener() {
			@Override
			public void selectionChanged() {
				int colIndex = colorMatrixPanel.getSelectedLeadColumn();
				int rowIndex = colorMatrixPanel.getSelectedLeadRow();
				
				setLeadSelection(colIndex, rowIndex);
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

	private void setLeadSelection(int colIndex, int rowIndex) {
		if (colIndex < 0 || rowIndex < 0)
			resultsModel.setHtmlInfo("");
		else {
			final String colName = 
				resultsModel.getColumnName(colIndex);
			final String rowName = 
				resultsModel.getRowName(rowIndex);
			final String[] paramNames = 
				resultsModel.getParamNames();
			
			StringBuilder sb = new StringBuilder();
			
			// Render parameters & values
			sb.append("<p><b>Column:</b><br>");
			sb.append(colName).append("</p>");
			sb.append("<p><b>Row:</b><br>");
			sb.append(rowName).append("</p>");
			
			for (int i = 0; i < paramNames.length; i++) {
				final String paramName = paramNames[i];
				final String value = Double.toString(
						resultsModel.getValue(colIndex, rowIndex, i));
				
				sb.append("<p><b>");
				sb.append(paramName);
				sb.append(":</b><br>");
				sb.append(value);
				sb.append("</p>");
			}
			
			resultsModel.setHtmlInfo(sb.toString());
		}
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
	
	public ResultsModel getResultsModel() {
		return resultsModel;
	}

	@Override
	public Object getModel() {
		return resultsModel;
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

	public int getSelectedParamIndex() {
		return configPanel.getSelectedParamIndex();
	}
}
