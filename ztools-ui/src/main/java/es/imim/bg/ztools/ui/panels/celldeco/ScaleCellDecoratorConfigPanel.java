package es.imim.bg.ztools.ui.panels.celldeco;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.model.celldeco.ScaleCellDecoratorContext;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.utils.TableUtils;

public class ScaleCellDecoratorConfigPanel extends JPanel {

	private static final long serialVersionUID = -7443053984962647946L;

	private static class ElementPropertyAdapter {
		private int index;
		private IElementProperty property;
		public ElementPropertyAdapter(int index, IElementProperty property) {
			this.index = index;
			this.property = property;
		}
		public int getIndex() {
			return index;
		}
		public IElementProperty getProperty() {
			return property;
		}
		@Override
		public String toString() {
			return property.getName();
		}
	}
	
	private ITable table;
	private JComboBox valueCb;
	private JCheckBox showCorrChkBox;
	
	public ScaleCellDecoratorConfigPanel(ITable table) {
		this.table = table;
		
		createComponents();
		
		table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (ITable.CELL_DECORATION_CONTEXT_CHANGED.equals(evt.getPropertyName())) {
					/*if (oldValue != null)
						((IModel) oldValue).removePropertyChangeListener(decorationContextListener);
					
					((IModel) newValue).addPropertyChangeListener(decorationContextListener);*/
					
					refresh();
				}
			}
		});
	}

	private void createComponents() {
		
		// value combo box
		
		IElementAdapter cellFacade = table.getCellsFacade();
		
		int numProps = cellFacade.getPropertyCount();
		
		ElementPropertyAdapter[] props = 
			new ElementPropertyAdapter[numProps];
		
		for (int i = 0; i < numProps; i++)
			props[i] = new ElementPropertyAdapter(
					i, cellFacade.getProperty(i));
		
		valueCb = new JComboBox(new DefaultComboBoxModel(props));
		
		valueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueChanged();
				}
			}
		});
		
		// show correction check box
		
		showCorrChkBox = new JCheckBox();
		showCorrChkBox.setText("Show correction");
		showCorrChkBox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				showCorrectionChecked();
			}
		});
		
		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(showCorrChkBox);
	}
	
	private void refresh() {
		ScaleCellDecoratorContext context = 
			(ScaleCellDecoratorContext) table.getCellDecoratorContext();
		
		if (context != null) {
			valueCb.setSelectedIndex(context.getValueIndex());
			table.setSelectedPropertyIndex(context.getValueIndex());
			showCorrChkBox.setSelected(context.isUseCorrectedScale());
		}
	}

	private void valueChanged() {
		
		ElementPropertyAdapter propAdapter = 
			(ElementPropertyAdapter) valueCb.getSelectedItem();
		
		ScaleCellDecoratorContext context = 
			(ScaleCellDecoratorContext) table.getCellDecoratorContext();
		
		context.setValueIndex(propAdapter.getIndex());
		
		table.setSelectedPropertyIndex(propAdapter.getIndex());
		
		// search for corresponding corrected value
		
		int corrIndex = TableUtils.correctedValueIndex(
				table, propAdapter.getProperty());
		
		if (corrIndex >= 0)
			context.setCorrectedValueIndex(corrIndex);
		
		showCorrChkBox.setEnabled(corrIndex >= 0);
	}
	
	private void showCorrectionChecked() {
		ScaleCellDecoratorContext context = 
			(ScaleCellDecoratorContext) table.getCellDecoratorContext();
		
		ElementPropertyAdapter propAdapter = 
			(ElementPropertyAdapter) valueCb.getSelectedItem();
		
		if (propAdapter != null) {
			int corrIndex = TableUtils.correctedValueIndex(
					table, propAdapter.getProperty());
			
			if (corrIndex >= 0)
				context.setCorrectedValueIndex(corrIndex);
		}
		
		context.setUseCorrectedScale(
				showCorrChkBox.isSelected());
	}
}
