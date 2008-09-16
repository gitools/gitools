package es.imim.bg.ztools.zcalc.ui.panels.results;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import es.imim.bg.colorscale.ColorScale;
import es.imim.bg.colorscale.LogColorScale;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrix;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixCellDecoration;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixCellDecorator;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixModel;
import es.imim.bg.ztools.zcalc.ui.colormatrix.ColorMatrixCellDecoration.TextAlignment;

public class ResultsPanel extends JPanel {

	private static final long serialVersionUID = -540561086703759209L;

	private static final String defaultParamName = "right-p-value";

	private static final int defaultColorColumnsWidth = 30;
	private static final int defaultValueColumnsWidth = 90;

	private Results results;
	
	private class CellDecorationConfig {
		public boolean showColors = true;
		
		public DecimalFormat textFormat = new DecimalFormat("#.######");
		public ColorMatrixCellDecoration.TextAlignment textAlign = 
			ColorMatrixCellDecoration.TextAlignment.left;
		
		public ColorScale scale = new LogColorScale();
	}
	
	private int paramIndex;
	
	private Map<String, CellDecorationConfig> cellDecorationConfig;
	
	private JComboBox paramCombo;
	private JComboBox showCombo;
	private JPanel valuesConfigPanel;
	private JComboBox justifCombo;
	private JTextField fmtTxtField;
	private JPanel colorsConfigPanel;
	
	private JTable paramValuesTable;
	
	private ColorMatrix colorMatrix;
	
	private int selColIndex;
	private int selRowIndex;
	
	public ResultsPanel(Results results) {
		
		this.results = results;
		
		this.paramIndex = results.getParamIndex(defaultParamName);
		
		this.cellDecorationConfig = new HashMap<String, CellDecorationConfig>();
		
		this.selColIndex = this.selRowIndex = -1;
		
		createComponents();
	}

	private void createComponents() {
		
		/* Top panel */
		
		paramCombo = new JComboBox(results.getParamNames());
		paramCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					paramIndex = results.getParamIndex(e.getItem().toString());
					
					refreshConfigControls();
					refreshColorMatrixWidth();
					colorMatrix.refresh();
				}
			}
		});
		
		showCombo = new JComboBox(new String[] { "colors", "values" });
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					getCurrentDecorationConfig().showColors = 
						e.getItem().toString().equals("colors");
					
					refreshConfigControls();
					refreshColorMatrixWidth();
					colorMatrix.refresh();
				}
			}
		});
		
		valuesConfigPanel = createValuesConfigPanel();
		colorsConfigPanel = createColorsConfigPanel();
		
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(new JLabel("Parameter"));
		topPanel.add(paramCombo);
		topPanel.add(new JLabel("Show"));
		topPanel.add(showCombo);
		topPanel.add(valuesConfigPanel);
		topPanel.add(colorsConfigPanel);
		
		/* Left panel */
		
		paramValuesTable = new JTable();
		paramValuesTable.setFillsViewportHeight(true);
		paramValuesTable.setModel(new TableModel() {
			
			@Override public int getColumnCount() { return 2; }

			@Override public int getRowCount() { return results.getParamNames().length; }
			
			@Override public String getColumnName(int columnIndex) {
				switch(columnIndex) {
				case 0: return "name";
				case 1: return "value";
				}
				return null;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0: 
					return results.getParamNames()[rowIndex];
				case 1: 
					return selColIndex != -1 && selRowIndex != -1 ? 
							results.getDataValue(selColIndex, selRowIndex, rowIndex)
							: "";
				}
				return null;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) { return String.class; }
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

			@Override 
			public void addTableModelListener(TableModelListener l) { }
			
			@Override
			public void removeTableModelListener(TableModelListener l) { }

			@Override
			public void setValueAt(Object value, int rowIndex, int columnIndex) { }
			
		});
		
		final JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		
		final JScrollPane scrPanel = new JScrollPane(paramValuesTable);
		leftPanel.add(scrPanel, BorderLayout.CENTER);
		
		/* Color matrix */
		
		colorMatrix = new ColorMatrix();
		colorMatrix.setModel(new ColorMatrixModel() {
			@Override
			public int getColumnCount() {
				return results.getData().columns();
			}
			@Override
			public String getColumnName(int column) {
				return results.getColNames()[column];
			}
			@Override
			public int getRowCount() {
				return results.getData().rows();
			}
			@Override
			public String getRowName(int row) {
				return results.getRowNames()[row];
			}
			@Override
			public Double getValue(int row, int column) {
				return results.getDataValue(column, row, paramIndex);
			}
		});
		
		colorMatrix.setCellDecorator(new ColorMatrixCellDecorator() {
			@Override
			public void decorate(ColorMatrixCellDecoration decoration, Double value) {
				CellDecorationConfig config = getCurrentDecorationConfig();
				if (config.showColors) {
					Color c = config.scale.getColor(value);
					decoration.setBgColor(c);
					decoration.setToolTip(value.toString());
				}
				else {
					String txt = config.textFormat.format(value);
					decoration.setText(txt);
					decoration.setTextAlign(config.textAlign);
					decoration.setToolTip(txt);
				}
			}
			
		});

		final ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				selRowIndex = colorMatrix.getTableSelectionModel().getMinSelectionIndex();
				selColIndex = colorMatrix.getColumnSelectionModel().getMinSelectionIndex();
				paramValuesTable.repaint();
				colorMatrix.refresh();
				//System.err.println(selRowIndex + ", " + selColIndex);
			}
		};
		colorMatrix.getTableSelectionModel().addListSelectionListener(listener);
		colorMatrix.getColumnSelectionModel().addListSelectionListener(listener);
		
		refreshConfigControls();
		refreshColorMatrixWidth();
		
		final JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(topPanel, BorderLayout.NORTH);
		rightPanel.add(colorMatrix, BorderLayout.CENTER);
		
		setLayout(new BorderLayout());
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.CENTER);
	}

	private JPanel createValuesConfigPanel() {
		
		justifCombo = new JComboBox(new String[] { "left", "right", "center" });
		
		justifCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					CellDecorationConfig config = getCurrentDecorationConfig();
					if (e.getItem().toString().equals("left"))
						config.textAlign = TextAlignment.left;
					else if (e.getItem().toString().equals("right"))
						config.textAlign = TextAlignment.right;
					else if (e.getItem().toString().equals("center"))
						config.textAlign = TextAlignment.center;
					colorMatrix.refresh();
				}
			}
		});
		
		fmtTxtField = new JTextField();
		fmtTxtField.setMinimumSize(new Dimension(90, 0));
		fmtTxtField.setMaximumSize(new Dimension(100, 100));
		//fmtTxtField.setPreferredSize(new Dimension(100, 40));
		fmtTxtField.getDocument().addDocumentListener(new DocumentListener() {
			private void update(DocumentEvent e) {
				try {
					getCurrentDecorationConfig().textFormat = 
						new DecimalFormat(fmtTxtField.getText());
					
					colorMatrix.refresh();
				}
				catch (IllegalArgumentException ex) {
					//System.err.println(ex.toString());
				}
			}
			@Override public void changedUpdate(DocumentEvent e) { update(e); }
			@Override public void insertUpdate(DocumentEvent e) { update(e); }
			@Override public void removeUpdate(DocumentEvent e) { update(e); }
			
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel("Format"));
		panel.add(fmtTxtField);
		panel.add(new JLabel("Align"));
		panel.add(justifCombo);
		
		return panel;
	}
	
	private JPanel createColorsConfigPanel() {
		
		//final CellDecorationConfig config = getCurrentDecorationConfig();
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		return panel;
	}

	private void refreshConfigControls() {
		
		CellDecorationConfig config = getCurrentDecorationConfig();
		
		paramCombo.setSelectedIndex(paramIndex);
		
		showCombo.setSelectedIndex(config.showColors ? 0 : 1);
		
		fmtTxtField.setText(config.textFormat.toPattern());
		
		switch (config.textAlign) {
		case left: justifCombo.setSelectedIndex(0); break;
		case right: justifCombo.setSelectedIndex(1); break;
		case center: justifCombo.setSelectedIndex(2); break;
		}
		
		valuesConfigPanel.setVisible(!config.showColors);
		colorsConfigPanel.setVisible(config.showColors);
	}
	
	private void refreshColorMatrixWidth() {
		CellDecorationConfig config = cellDecorationConfig.get(
				getCurrentParamName());
		
		colorMatrix.setColumnsWidth(
				config.showColors ? 
						defaultColorColumnsWidth 
						: defaultValueColumnsWidth);
	}

	private CellDecorationConfig getCurrentDecorationConfig() {
		String paramName = getCurrentParamName();
		CellDecorationConfig config = cellDecorationConfig.get(paramName);
		if (config == null) {
			config = new CellDecorationConfig();
			cellDecorationConfig.put(paramName, config);
		}
		return config;
	}

	private String getCurrentParamName() {
		return results.getParamNames()[paramIndex];
	}
	
}
