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

import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.impl.PValueElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.model.TableViewModel;

public class PValueDecoratorPanel extends AbstractDecoratorPanel {

	private static final long serialVersionUID = -7443053984962647946L;

	private List<IndexedProperty> valueProperties;
	
	private PValueElementDecorator decorator;
	
	private JComboBox valueCb;
	private JCheckBox showCorrChkBox;
	
	public PValueDecoratorPanel(TableViewModel model) {
		super(model);
		
		this.decorator = (PValueElementDecorator) getDecorator();
		
		final IElementAdapter adapter = decorator.getAdapter();
		
		int numProps = adapter.getPropertyCount();
		
		valueProperties = new ArrayList<IndexedProperty>();
		for (int i = 0; i < numProps; i++) {
			final IElementProperty property = adapter.getProperty(i);
			if (property.getId().endsWith("p-value")
					/*&& !property.getId().startsWith("corrected")*/)
				valueProperties.add(new IndexedProperty(i, property));
		}
		
		if (valueProperties.size() == 0)
			loadAllProperties(valueProperties, adapter);
		
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
		showCorrChkBox.setText("Filter by correction");
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
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		getTable().setSelectedPropertyIndex(decorator.getValueIndex());
		showCorrChkBox.setSelected(decorator.getUseCorrection());
	}

	private void valueChanged() {
		
		IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();
		
		decorator.setValueIndex(propAdapter.getIndex());
		
		getTable().setSelectedPropertyIndex(propAdapter.getIndex());
		
		// search for corresponding corrected value
		
		int corrIndex = TableUtils.correctedValueIndex(
				decorator.getAdapter(), propAdapter.getProperty());
		
		if (corrIndex >= 0)
			decorator.setCorrectedValueIndex(corrIndex);
		
		showCorrChkBox.setEnabled(corrIndex >= 0);
	}
	
	private void showCorrectionChecked() {
		IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();
		
		if (propAdapter != null) {
			int corrIndex = TableUtils.correctedValueIndex(
					decorator.getAdapter(), propAdapter.getProperty());
			
			if (corrIndex >= 0)
				decorator.setCorrectedValueIndex(corrIndex);
		}
		
		decorator.setUseCorrection(
				showCorrChkBox.isSelected());
	}
}
