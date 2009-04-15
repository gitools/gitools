package es.imim.bg.ztools.ui.panels.decorator;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.decorator.impl.ZScoreElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.model.TableViewModel;

public class ZScoreDecoratorPanel extends JPanel {

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

	private List<ElementPropertyAdapter> valueProperties;
	private List<ElementPropertyAdapter> corrValueProperties;
	
	private ZScoreElementDecorator decorator;
	private ITable table;
	
	private JComboBox valueCb;
	private JCheckBox showCorrChkBox;
	private JComboBox corrValueCb;
	
	public ZScoreDecoratorPanel(TableViewModel model) {
		this.decorator = (ZScoreElementDecorator) model.getDecorator();
		this.table = model.getTable();
		
		final IElementAdapter adapter = decorator.getAdapter();
		
		int numProps = adapter.getPropertyCount();
		
		valueProperties = new ArrayList<ElementPropertyAdapter>();
		corrValueProperties = new ArrayList<ElementPropertyAdapter>();
		for (int i = 0; i < numProps; i++) {
			final IElementProperty property = adapter.getProperty(i);
			
			if (property.getId().endsWith("z-score"))
				valueProperties.add(new ElementPropertyAdapter(i, property));
			if (property.getId().startsWith("corrected"))
				corrValueProperties.add(new ElementPropertyAdapter(i, property));
		}
		
		if (valueProperties.size() == 0)
			valueProperties.add(new ElementPropertyAdapter(0, adapter.getProperty(0)));
		
		createComponents();
	}

	private void createComponents() {
		
		// value combo box
		
		valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));
		
		valueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueChanged();
				}
			}
		});
		
		// show correction check box
		
		showCorrChkBox = new JCheckBox();
		showCorrChkBox.setText("Filter significance by");
		showCorrChkBox.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				showCorrectionChecked();
			}
		});
		
		// corrected value combo box
		
		corrValueCb = new JComboBox(new DefaultComboBoxModel(corrValueProperties.toArray()));
		
		corrValueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					corrValueChanged();
				}
			}
		});
		
		refresh();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
		add(showCorrChkBox);
		add(corrValueCb);
	}

	private void refresh() {
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		table.setSelectedPropertyIndex(decorator.getValueIndex());
		
		showCorrChkBox.setSelected(decorator.getUseCorrection());
		corrValueCb.setEnabled(decorator.getUseCorrection());
		
		for (int i = 0; i < corrValueProperties.size(); i++)
			if (corrValueProperties.get(i).getIndex() == decorator.getCorrectedValueIndex())
				corrValueCb.setSelectedIndex(i);
	}

	private void valueChanged() {
		
		ElementPropertyAdapter propAdapter = 
			(ElementPropertyAdapter) valueCb.getSelectedItem();
		
		decorator.setValueIndex(propAdapter.getIndex());
		
		table.setSelectedPropertyIndex(propAdapter.getIndex());
	}
	
	private void showCorrectionChecked() {		
		decorator.setUseCorrection(
				showCorrChkBox.isSelected());
		
		corrValueCb.setEnabled(
				showCorrChkBox.isSelected());
	}
	
	protected void corrValueChanged() {
		ElementPropertyAdapter propAdapter = 
			(ElementPropertyAdapter) corrValueCb.getSelectedItem();
		
		decorator.setCorrectedValueIndex(propAdapter.getIndex());
	}
}
