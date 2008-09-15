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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

	private Results results;
	
	private class CellDecorationConfig {
		public boolean showColorScale = true;
		
		public DecimalFormat textFormat = new DecimalFormat("#.########");
		public ColorMatrixCellDecoration.TextAlignment textAlign = 
			ColorMatrixCellDecoration.TextAlignment.left;
		
		public ColorScale scale = new LogColorScale();
	}
	
	private int paramIndex;
	
	private Map<String, CellDecorationConfig> cellDecorationConfig;
	
	private ColorMatrix colorMatrix;
	
	public ResultsPanel(Results results) {
		
		this.results = results;
		
		this.paramIndex = results.getParamIndex("right-p-value");
		
		this.cellDecorationConfig = new HashMap<String, CellDecorationConfig>();
		
		createComponents();
	}

	private void createComponents() {
		
		final JComboBox paramCombo = new JComboBox(results.getParamNames());
		paramCombo.setSelectedIndex(paramIndex);
		paramCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					paramIndex = results.getParamIndex(e.getItem().toString());
					colorMatrix.refresh();
				}
			}
		});
		
		final JComboBox showCombo = 
			new JComboBox(new String[] { "colors", "values" });
		showCombo.setSelectedIndex(
				getCurrentDecorationConfig().showColorScale ? 0 : 1);
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					getCurrentDecorationConfig().showColorScale = 
						e.getItem().toString().equals("colors");
					colorMatrix.refresh();
				}
			}
		});
		
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(new JLabel("Parameter"));
		topPanel.add(paramCombo);
		topPanel.add(new JLabel("Show"));
		topPanel.add(showCombo);
		topPanel.add(createDecorationConfigPanel());
		
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
				if (config.showColorScale) {
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

		setLayout(new BorderLayout());
		add(topPanel, BorderLayout.NORTH);
		add(colorMatrix, BorderLayout.CENTER);
	}

	private CellDecorationConfig getCurrentDecorationConfig() {
		String paramName = results.getParamNames()[paramIndex];
		CellDecorationConfig config = cellDecorationConfig.get(paramName);
		if (config == null) {
			config = new CellDecorationConfig();
			cellDecorationConfig.put(paramName, config);
		}
		return config;
	}

	private JPanel createDecorationConfigPanel() {
		
		final CellDecorationConfig config = getCurrentDecorationConfig();
		
		final JComboBox justifCombo = 
			new JComboBox(new String[] { "left", "right", "center" });
		
		switch (config.textAlign) {
		case left: justifCombo.setSelectedIndex(0); break;
		case right: justifCombo.setSelectedIndex(1); break;
		case center: justifCombo.setSelectedIndex(2); break;
		}
		
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
		
		final JTextField fmtTxtField = new JTextField(config.textFormat.toPattern());
		fmtTxtField.setMinimumSize(new Dimension(90, 0));
		fmtTxtField.setMaximumSize(new Dimension(100, 100));
		fmtTxtField.setPreferredSize(new Dimension(100, 40));
		fmtTxtField.getDocument().addDocumentListener(new DocumentListener() {
			private void update(DocumentEvent e) {
				try {
					System.out.println(fmtTxtField.getText());
					getCurrentDecorationConfig().textFormat = 
						new DecimalFormat(fmtTxtField.getText());
					
					colorMatrix.refresh();
				}
				catch (IllegalArgumentException ex) {
					System.err.println(ex.toString());
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
		panel.add(justifCombo);
		
		return panel;
	}
	
}
