package es.imim.bg.ztools.ui.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.imim.bg.ztools.ui.colormatrix.CellDecorationConfig;
import es.imim.bg.ztools.ui.colormatrix.CellDecoration.TextAlignment;
import es.imim.bg.ztools.ui.model.ITableModel;

public class TableViewConfigPanel extends JPanel {

	private static final long serialVersionUID = 7931473950086532892L;
	
	public interface TableViewConfigPanelListener {
		void showModeChanged();
		void justificationChanged();
		void formatChanged();
	}
	
	private ITableModel tableModel;
	
	private JComboBox showCombo;
	private JPanel valuesConfigPanel;
	private JComboBox justifCombo;
	private JTextField fmtTxtField;
	private JPanel colorsConfigPanel;
	
	public TableViewConfigPanel(ITableModel tableModel) {
		this.tableModel = tableModel;
		
		createComponents();
	}
	
	private void createComponents() {
		showCombo = new JComboBox(new String[] { "colors", "values" });
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					final CellDecorationConfig cellDeco = 
						tableModel.getCellDecoration();
					
					cellDeco.showColors =
						e.getItem().toString().equals("colors");
					
					tableModel.setCellDecoration(cellDeco);
					
					refresh();
				}
			}
		});
		
		valuesConfigPanel = createValuesConfigPanel();
		colorsConfigPanel = createColorsConfigPanel();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
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
					final CellDecorationConfig cellDeco = 
						tableModel.getCellDecoration();
					
					if (e.getItem().toString().equals("left"))
						cellDeco.textAlign = TextAlignment.left;
					else if (e.getItem().toString().equals("right"))
						cellDeco.textAlign = TextAlignment.right;
					else if (e.getItem().toString().equals("center"))
						cellDeco.textAlign = TextAlignment.center;
					
					tableModel.setCellDecoration(cellDeco);
					
					refresh();
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
					final CellDecorationConfig cellDeco = 
						tableModel.getCellDecoration();
					
					cellDeco.textFormat = 
						new DecimalFormat(fmtTxtField.getText());
					
					tableModel.setCellDecoration(cellDeco);
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
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		return panel;
	}
	
	public void refresh() {
		CellDecorationConfig config = tableModel.getCellDecoration();
		
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
}
