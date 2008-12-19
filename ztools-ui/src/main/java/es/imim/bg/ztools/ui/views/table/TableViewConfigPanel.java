package es.imim.bg.ztools.ui.views.table;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.imim.bg.ztools.ui.model.table.ITable;

public class TableViewConfigPanel extends JPanel {

	private static final long serialVersionUID = 7931473950086532892L;
	
	public interface TableViewConfigPanelListener {
		void showModeChanged();
		void justificationChanged();
		void formatChanged();
	}
	
	private ITable table;
	private ITableDecorator[] decorators;
	
	private JComboBox showCombo;
	private JPanel valuesConfigPanel;
	private JComboBox justifCombo;
	private JTextField fmtTxtField;
	private JPanel colorsConfigPanel;
	
	public TableViewConfigPanel(ITable tableModel, ITableDecorator[] decorators) {
		this.table = tableModel;
		this.decorators = decorators;
		
		createComponents();
	}
	
	private void createComponents() {
		showCombo = new JComboBox();
		showCombo.setModel(
				new DefaultComboBoxModel(decorators));
		showCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					table.setCellDecoratorContext(
							((ITableDecorator) e.getItem()).getContext());
					
					refresh();
				}
			}
		});
		showCombo.setSelectedIndex(0);
		
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
		
		/*justifCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					final CellDecorationConfig cellDeco = 
						table.getCellDecoration(
								table.getCurrentProperty());
					
					if (e.getItem().toString().equals("left"))
						cellDeco.textAlign = TextAlignment.left;
					else if (e.getItem().toString().equals("right"))
						cellDeco.textAlign = TextAlignment.right;
					else if (e.getItem().toString().equals("center"))
						cellDeco.textAlign = TextAlignment.center;
					
					table.setCellDecoration(
							table.getCurrentProperty(), cellDeco);
					
					refresh();
				}
			}
		});*/
		
		fmtTxtField = new JTextField();
		fmtTxtField.setMinimumSize(new Dimension(90, 0));
		fmtTxtField.setMaximumSize(new Dimension(100, 100));
		//fmtTxtField.setPreferredSize(new Dimension(100, 40));
		/*fmtTxtField.getDocument().addDocumentListener(new DocumentListener() {
			private void update(DocumentEvent e) {
				try {					
					final CellDecorationConfig cellDeco = 
						table.getCellDecoration(
								table.getCurrentProperty());
					
					cellDeco.textFormat = 
						new DecimalFormat(fmtTxtField.getText());
					
					table.setCellDecoration(
							table.getCurrentProperty(), cellDeco);
				}
				catch (IllegalArgumentException ex) {
					//System.err.println(ex.toString());
				}
			}
			@Override public void changedUpdate(DocumentEvent e) { update(e); }
			@Override public void insertUpdate(DocumentEvent e) { update(e); }
			@Override public void removeUpdate(DocumentEvent e) { update(e); }
			
		});*/
		
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
		/*fmtTxtField.setText(config.textFormat.toPattern());
		
		switch (config.textAlign) {
		case left: justifCombo.setSelectedIndex(0); break;
		case right: justifCombo.setSelectedIndex(1); break;
		case center: justifCombo.setSelectedIndex(2); break;
		}
		
		valuesConfigPanel.setVisible(!config.showColors);
		colorsConfigPanel.setVisible(config.showColors);*/
		valuesConfigPanel.setVisible(false);
		colorsConfigPanel.setVisible(false);
	}
	
	public ITableDecorator getCellDecorator() {
		return (ITableDecorator) showCombo.getSelectedItem();
	}
}
