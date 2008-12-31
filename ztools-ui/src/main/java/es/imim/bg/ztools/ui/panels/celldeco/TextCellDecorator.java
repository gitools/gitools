package es.imim.bg.ztools.ui.panels.celldeco;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.imim.bg.ztools.ui.model.celldeco.ITableDecoratorContext;
import es.imim.bg.ztools.ui.model.celldeco.TextCellDecoratorContext;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.panels.table.CellDecoration;

public class TextCellDecorator 
		implements ITableDecorator {
	
	private static final int defaultWidth = 90;
	private static final int defaultHeight = 30;
	
	private ITable table;
	
	protected TextCellDecoratorContext context;
	
	public TextCellDecorator(ITable table) {
		this(table, new TextCellDecoratorContext());
	}
	
	public TextCellDecorator(ITable table, TextCellDecoratorContext context) {
		this.table = table;
		this.context = context;
	}
	
	@Override
	public void decorate(CellDecoration decoration, Object value) {
		String txt = context.getFormat().format(value.toString());
		decoration.setText(txt);
		decoration.setTextAlign(context.getAlign());
		decoration.setToolTip(txt);
	}
	
	@Override
	public int getMinimumHeight() {
		return 0;
	}
	
	@Override
	public int getMaximumWidth() {
		return Integer.MAX_VALUE;
	}
	
	public int getPreferredWidth() {
		return defaultWidth;
	}

	@Override
	public int getMinimumWidth() {
		return 0;
	}
	
	@Override
	public int getMaximumHeight() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getPreferredHeight() {
		return defaultHeight;
	}

	@Override
	public Component createConfigurationComponent() {
		return createConfigurationPanel();
	}

	@Override
	public ITableDecoratorContext getContext() {
		return context;
	}

	@Override
	public void setContext(ITableDecoratorContext context) {
		this.context = (TextCellDecoratorContext) context;
	}
	
	private JPanel createConfigurationPanel() {
		final JComboBox justifCombo;
		final JTextField fmtTxtField;
		
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

	@Override
	public String toString() {
		return "Values";
	}
}

/*fmtTxtField.setText(config.textFormat.toPattern());

switch (config.textAlign) {
case left: justifCombo.setSelectedIndex(0); break;
case right: justifCombo.setSelectedIndex(1); break;
case center: justifCombo.setSelectedIndex(2); break;
}

valuesConfigPanel.setVisible(!config.showColors);
colorsConfigPanel.setVisible(config.showColors);*/
