package es.imim.bg.ztools.ui.views.results;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;
import es.imim.bg.ztools.ui.colormatrix.CellDecoration.TextAlignment;

public class ResultsConfigPanel extends JPanel {

	private static final long serialVersionUID = 7931473950086532892L;
	
	public interface ResultsConfigPanelListener {
		void paramChanged();
		void showModeChanged();
		void justificationChanged();
		void formatChanged();
	}
	
	private String[] paramNames;
	private int paramIndex;
	
	private Map<String, CellDecorationConfig> cellDecorationConfig;
	
	private List<ResultsConfigPanelListener> listeners;
	
	private JComboBox paramCombo;
	private JComboBox showCombo;
	private JPanel valuesConfigPanel;
	private JComboBox justifCombo;
	private JTextField fmtTxtField;
	private JPanel colorsConfigPanel;
	
	public ResultsConfigPanel(String[] paramNames, int paramIndex) {
		this.paramNames = paramNames;
		this.paramIndex = paramIndex;
		
		this.cellDecorationConfig = new HashMap<String, CellDecorationConfig>();
		
		this.listeners = new ArrayList<ResultsConfigPanelListener>(1);
		
		createComponents();
	}
	
	private void createComponents() {
		paramCombo = new JComboBox(paramNames);
		paramCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					paramIndex = getParamIndexFromName(e.getItem().toString());
					
					fireParamChanged();
					refresh();
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
					
					fireShowModeChanged();
					refresh();
				}
			}
		});
		
		valuesConfigPanel = createValuesConfigPanel();
		colorsConfigPanel = createColorsConfigPanel();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Parameter"));
		add(paramCombo);
		add(new JLabel("Show"));
		add(showCombo);
		add(valuesConfigPanel);
		add(colorsConfigPanel);	
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
					
					fireJustificationChanged();
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
					
					fireFormatChanged();
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

	public void addListener(ResultsConfigPanelListener listener) {
		listeners.add(listener);
	}
	
	public int getSelectedParamIndex() {
		return paramIndex;
	}
	
	public String getSelectedParamName() {
		return paramNames[paramIndex];
	}
	
	public CellDecorationConfig getCurrentDecorationConfig() {
		String paramName = getSelectedParamName();
		CellDecorationConfig config = 
			cellDecorationConfig.get(paramName);
		
		if (config == null) {
			config = new CellDecorationConfig();
			cellDecorationConfig.put(paramName, config);
		}
		return config;
	}
	
	public void refresh() {
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

	private int getParamIndexFromName(String name) {
		System.out.println(name + " in " + Arrays.toString(paramNames));
		
		int index = 0;
		while (index < paramNames.length 
				&& !paramNames[index].equals(name))
			index++;
		
		if (index >= paramNames.length)
			throw new RuntimeException("Parameter called '" + name + "' doesn't exists.");
		
		return index;
	}
	
	private void fireParamChanged() {
		for (ResultsConfigPanelListener listener : listeners)
			listener.paramChanged();
	}
	
	private void fireShowModeChanged() {
		for (ResultsConfigPanelListener listener : listeners)
			listener.showModeChanged();		
	}
	
	protected void fireJustificationChanged() {
		for (ResultsConfigPanelListener listener : listeners)
			listener.justificationChanged();
	}
	
	protected void fireFormatChanged() {
		for (ResultsConfigPanelListener listener : listeners)
			listener.formatChanged();
	}
}
