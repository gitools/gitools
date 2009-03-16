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

import es.imim.bg.ztools.table.TableUtils;
import es.imim.bg.ztools.table.decorator.ElementDecorator;
import es.imim.bg.ztools.table.decorator.pvalue.PValueElementDecorator;
import es.imim.bg.ztools.table.element.IElementAdapter;
import es.imim.bg.ztools.table.element.IElementProperty;

public class PValueDecoratorPanel extends JPanel {

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

	private List<ElementPropertyAdapter> props;
	
	private PValueElementDecorator decorator;
	
	private JComboBox valueCb;
	private JCheckBox showCorrChkBox;
	
	public PValueDecoratorPanel(ElementDecorator decorator) {
		this.decorator = (PValueElementDecorator) decorator;
		
		IElementAdapter adapter = decorator.getAdapter();
		
		int numProps = adapter.getPropertyCount();
		
		props =	new ArrayList<ElementPropertyAdapter>();
		
		for (int i = 0; i < numProps; i++) {
			final IElementProperty property = adapter.getProperty(i);
			if (property.getId().endsWith("p-value")
					&& !property.getId().startsWith("corrected"))
				props.add(new ElementPropertyAdapter(i, property));
		}
		
		createComponents();
	}

	private void createComponents() {
		
		// value combo box
		
		valueCb = new JComboBox(new DefaultComboBoxModel(props.toArray()));
		
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
		for (int i = 0; i < props.size(); i++)
			if (props.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		//TODO table.setSelectedPropertyIndex(decorator.getValueIndex());
		showCorrChkBox.setSelected(decorator.isUseCorrectedScale());
	}

	private void valueChanged() {
		
		ElementPropertyAdapter propAdapter = 
			(ElementPropertyAdapter) valueCb.getSelectedItem();
		
		decorator.setValueIndex(propAdapter.getIndex());
		
		//TODO table.setSelectedPropertyIndex(propAdapter.getIndex());
		
		// search for corresponding corrected value
		
		int corrIndex = TableUtils.correctedValueIndex(
				decorator.getAdapter(), propAdapter.getProperty());
		
		if (corrIndex >= 0)
			decorator.setCorrectedValueIndex(corrIndex);
		
		showCorrChkBox.setEnabled(corrIndex >= 0);
	}
	
	private void showCorrectionChecked() {
		ElementPropertyAdapter propAdapter = 
			(ElementPropertyAdapter) valueCb.getSelectedItem();
		
		if (propAdapter != null) {
			int corrIndex = TableUtils.correctedValueIndex(
					decorator.getAdapter(), propAdapter.getProperty());
			
			if (corrIndex >= 0)
				decorator.setCorrectedValueIndex(corrIndex);
		}
		
		decorator.setUseCorrectedScale(
				showCorrChkBox.isSelected());
	}
}
